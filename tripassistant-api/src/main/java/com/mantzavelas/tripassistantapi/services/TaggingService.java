package com.mantzavelas.tripassistantapi.services;

import com.mantzavelas.tripassistantapi.models.*;
import com.mantzavelas.tripassistantapi.photos.facebook.*;
import com.mantzavelas.tripassistantapi.repositories.PhotoCategoryRepository;
import com.mantzavelas.tripassistantapi.repositories.PhotoRepository;
import com.mantzavelas.tripassistantapi.repositories.PlaceRepository;
import com.mantzavelas.tripassistantapi.utils.BeanUtil;
import com.mantzavelas.tripassistantapi.utils.LocationUtil;
import com.mantzavelas.tripassistantapi.utils.StreamUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import java.text.Collator;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TaggingService {

    private PhotoRepository photoRepository;
    private PhotoCategoryRepository categoryRepository;
    private PlaceRepository placeRepository;
    private List<PhotoCategory> categories;
    private final Collator collatorInstance = Collator.getInstance();

    public TaggingService() {
        photoRepository = BeanUtil.getBean(PhotoRepository.class);
        categoryRepository = BeanUtil.getBean(PhotoCategoryRepository.class);
        placeRepository = BeanUtil.getBean(PlaceRepository.class);
        collatorInstance.setStrength(Collator.NO_DECOMPOSITION);
        categories = categoryRepository.findAll();
    }

    public TaggingService(PhotoRepository photoRepository, PhotoCategoryRepository categoryRepository, PlaceRepository placeRepository, List<PhotoCategory> categories) {
        this.photoRepository = photoRepository;
        this.categoryRepository = categoryRepository;
        this.placeRepository = placeRepository;
        this.categories = categories;
    }

    public void categorizePhotos() {
        Slice<Photo> photoBatchSlice = photoRepository.findAllUncategorized(PageRequest.of(0,100));//add here
        int slicePage = photoBatchSlice.getNumber();

        do {
            for (Photo photo : photoBatchSlice.getContent()) {
                getFacebookCurrentPlaceResult(photo).ifPresent(result -> {
					doPhotoCategorization(photo, result);
					//at this point, photo categories are assigned to the photo, so they can be fetched in persistPlaces
					persistPlaces(photo, result);
				});
            }
            slicePage++;
            photoBatchSlice = photoRepository.findAllUncategorized(PageRequest.of(slicePage, 100));
        } while (photoBatchSlice.hasNext());
    }

    private void persistPlaces(Photo photo, FacebookSearchPlaceResult result) {

        String latitude = Double.toString(result.getLocation().getLatitude());
        String longitude = Double.toString(result.getLocation().getLongitude());

        Place place = placeRepository.findByLatitudeAndLongitude(latitude, longitude);

        if (place != null) {
            //just increment the visits, do not create new entry
            place.incrementVisits();
            place.addPhoto(photo.getUrl());
        } else {
            place = new Place();

            String title = Optional.ofNullable(result.getName()).orElse(photo.getTitle());
            String description = Optional.ofNullable(result.getDescription()).orElse(photo.getDescription());

            place.setTitle(title);
            place.setDescription(description);
            place.setLatitude(latitude);
            place.setLongitude(longitude);
            place.setCategories(photo.getCategories());
            place.setVisits(result.getCheckins()); //facebook visits
            place.incrementVisits(); //flickr photo visit
            place.addPhoto(photo.getUrl());

            Optional<City> placeCity = Arrays.stream(City.values())
                    .filter(getCityPredicate(result, latitude, longitude))
                    .findFirst();

            if (placeCity.isPresent()) {
                place.setCity(placeCity.get());
            }
        }

        placeRepository.save(place);
    }

    private Predicate<City> getCityPredicate(FacebookSearchPlaceResult result, String latitude, String longitude) {
    	if (Optional.ofNullable(result)
				.map(FacebookSearchPlaceResult::getLocation).map(FacebookLocation::getCity).isPresent()) {
			return city -> collatorInstance.compare(city.name(), result.getLocation().getCity()) == 0;
		}
		return city ->
				LocationUtil.haversineDistanceInKm(city.getLatitude(), city.getLongitude(), latitude, longitude) < 20.0;
    }


    private void doPhotoCategorization(Photo photo, FacebookSearchPlaceResult result) {
        Set<String> photoTags = getPhotoTags(photo, result);

        photoTags.retainAll(categories.stream()
            .flatMap(cat -> cat.getTags().stream())
            .collect(Collectors.toList())
        );

        List<PhotoCategory> categoriesToAdd = photoTags.stream()
            .flatMap(tag -> categoryRepository.findByCategoryTag(tag).stream())
            .filter(StreamUtil.distinctByKey(PhotoCategory::getCategory))
            .collect(Collectors.toList());

        photo.setCategories(new ArrayList<>(categoriesToAdd));

        if (photo.getCategories().isEmpty()) {
            PhotoCategory otherCategory = categoryRepository.findByCategory(PhotoCategoryEnum.OTHER);
            photo.setCategories(Collections.singletonList(otherCategory));
        }

        if (result.getName() != null) {
            photo.setTitle(result.getName());
        }

        if (result.getDescription() != null) {
            photo.setDescription(result.getDescription());
        }

        photoRepository.save(photo);
    }

    private Optional<FacebookSearchPlaceResult> getFacebookCurrentPlaceResult(Photo photo) {
        FacebookSearchPlaceResponse response = FacebookRestClient.create().getCurrentPlace(photo.getLatitude(), photo.getLongitude());

        return Optional.ofNullable(response)
				.map(FacebookSearchPlaceResponse::getData)
				.map(Collection::stream)
				.flatMap(stream -> stream.min(Comparator.comparing(getDistanceFunction(photo))));
    }

    private Set<String> getPhotoTags(Photo photo, FacebookSearchPlaceResult result) {
        Set<String> photoTags = new HashSet<>();

        Optional.ofNullable(result.getDescription()).ifPresent(desc -> photoTags.addAll(Arrays.asList(desc.split(" "))));
        Optional.ofNullable(result.getName()).ifPresent(name -> photoTags.addAll(Arrays.asList(name.split(" "))));

        photoTags.addAll(result.getCategories()
            .stream()
            .map(FacebookCategory::getName)
            .collect(Collectors.toList())
        );

        Optional.ofNullable(photo.getTitle()).ifPresent(title -> photoTags.addAll(Arrays.asList(title.split(" "))));
		Optional.ofNullable(photo.getDescription()).ifPresent(desc -> photoTags.addAll(Arrays.asList(desc.split(" "))));

        return photoTags;
    }

    private Function<FacebookSearchPlaceResult, Double> getDistanceFunction(Photo photo) {
        return s -> LocationUtil.haversineDistanceInKm(Double.parseDouble(photo.getLatitude())
                , Double.parseDouble(photo.getLongitude()), s.getLocation().getLatitude(), s.getLocation().getLongitude());
    }

}
