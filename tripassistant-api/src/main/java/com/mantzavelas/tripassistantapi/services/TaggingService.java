package com.mantzavelas.tripassistantapi.services;

import com.mantzavelas.tripassistantapi.models.*;
import com.mantzavelas.tripassistantapi.photos.utils.FacebookCategory;
import com.mantzavelas.tripassistantapi.photos.utils.FacebookCurrentPlaceResponse;
import com.mantzavelas.tripassistantapi.photos.utils.FacebookCurrentPlaceResult;
import com.mantzavelas.tripassistantapi.photos.utils.FacebookRestClient;
import com.mantzavelas.tripassistantapi.repositories.PhotoCategoryRepository;
import com.mantzavelas.tripassistantapi.repositories.PhotoRepository;
import com.mantzavelas.tripassistantapi.repositories.PlaceRepository;
import com.mantzavelas.tripassistantapi.utils.BeanUtil;
import com.mantzavelas.tripassistantapi.utils.LocationUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import java.text.Collator;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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
                FacebookCurrentPlaceResult result= getFacebookCurrentPlaceResult(photo);
                doPhotoCategorization(photo, result);
                //at this point, photo categories are assigned to the photo, so they can be fetched in persistPlaces
                persistPlaces(photo, result);
            }
            slicePage++;
            photoBatchSlice = photoRepository.findAllUncategorized(PageRequest.of(slicePage, 100));
        } while (photoBatchSlice.hasNext());
    }

    private void persistPlaces(Photo photo, FacebookCurrentPlaceResult result) {

        String latitude = Double.toString(result.getLocation().getLatitude());
        String longitude = Double.toString(result.getLocation().getLongitude());

        Place place = placeRepository.findByLatitudeAndLongitude(latitude, longitude);

        if (place != null) {
            //just increment the visits, do not create new entry
            place.incrementVisits();
            place.addPhoto(photo.getUrl());
        } else {
            place = new Place();

            String title = result.getName() != null ? result.getName() : photo.getTitle();
            String description = result.getDescription() != null ? result.getDescription() : photo.getDescription();

            place.setTitle(title);
            place.setDescription(description);
            place.setLatitude(latitude);
            place.setLongitude(longitude);
            place.setCategories(photo.getCategories());
            place.setVisits(result.getCheckins()); //facebook visits
            place.incrementVisits(); //flickr photo visit
            place.addPhoto(photo.getUrl());

            Optional<City> placeCity = Arrays.stream(City.values())
                    .filter(getCityPredicate(result))
                    .findFirst();

            if (placeCity.isPresent()) {
                place.setCity(placeCity.get());
            }
        }

        placeRepository.save(place);
    }

    private Predicate<City> getCityPredicate(FacebookCurrentPlaceResult result) {
        return city -> collatorInstance.compare(city.name(), result.getLocation().getCity()) == 0;
    }


    private void doPhotoCategorization(Photo photo, FacebookCurrentPlaceResult result) {
        Set<String> photoTags = getPhotoTags(photo, result);

        photoTags.retainAll(categories.stream()
            .flatMap(cat -> cat.getTags().stream())
            .collect(Collectors.toList())
        );

        List<PhotoCategory> categoriesToAdd = photoTags.stream()
            .flatMap(tag -> categoryRepository.findByCategoryTag(tag).stream())
            .filter(distinctByKey(PhotoCategory::getCategory))
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

    private FacebookCurrentPlaceResult getFacebookCurrentPlaceResult(Photo photo) {
        FacebookCurrentPlaceResponse response = FacebookRestClient.create().getCurrentPlace(photo.getLatitude(), photo.getLongitude());

        return response.getData()
            .stream()
            .sorted(Comparator.comparing(getDistanceFunction(photo)))
            .findFirst()
            .get();
    }

    private Set<String> getPhotoTags(Photo photo, FacebookCurrentPlaceResult result) {
        Set<String> photoTags = new HashSet<>();

        if (result.getDescription() != null) {
            photoTags.addAll(Arrays.asList(result.getDescription().split(" ")));
        }

        if (result.getName() != null) {
            photoTags.addAll(Arrays.asList(result.getName().split(" ")));
        }

        photoTags.addAll(result.getCategories()
            .stream()
            .map(FacebookCategory::getName)
            .collect(Collectors.toList())
        );

        if (photo.getTitle() != null) {
            photoTags.addAll(Arrays.asList(photo.getTitle().split(" ")));
        }

        if (photo.getDescription() != null) {
            photoTags.addAll(Arrays.asList(photo.getDescription().split(" ")));
        }
        return photoTags;
    }

    private Function<FacebookCurrentPlaceResult, Double> getDistanceFunction(Photo photo) {
        return s -> LocationUtil.haversineDistanceInKm(Double.parseDouble(photo.getLatitude())
                , Double.parseDouble(photo.getLongitude()), s.getLocation().getLatitude(), s.getLocation().getLongitude());
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> key) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(key.apply(t), Boolean.TRUE) == null;
    }

}
