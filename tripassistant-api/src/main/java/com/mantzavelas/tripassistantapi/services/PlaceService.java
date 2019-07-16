package com.mantzavelas.tripassistantapi.services;

import com.mantzavelas.tripassistantapi.converters.PlaceToPlaceDtoConverter;
import com.mantzavelas.tripassistantapi.dtos.PlaceDto;
import com.mantzavelas.tripassistantapi.exceptions.NoSuchCityException;
import com.mantzavelas.tripassistantapi.models.*;
import com.mantzavelas.tripassistantapi.repositories.PhotoRepository;
import com.mantzavelas.tripassistantapi.repositories.PlaceRepository;
import com.mantzavelas.tripassistantapi.utils.LocationUtil;
import com.mantzavelas.tripassistantapi.utils.StreamUtil;
import com.mantzavelas.tripassistantapi.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final PhotoRepository photoRepository;

    private PlaceToPlaceDtoConverter converter;

	@Autowired
	public PlaceService(PlaceRepository placeRepository, PhotoRepository photoRepository, PlaceToPlaceDtoConverter converter) {
		this.placeRepository = placeRepository;
		this.photoRepository = photoRepository;
		this.converter = converter;
	}

    public List<PlaceDto> searchPlacesForCategoryNear(String latitude, String longitude, Integer radiusInKm, String requestCategory) {

        validateLocation(latitude, longitude);

        if (radiusInKm == null || radiusInKm <=0) {
        	radiusInKm = 10; // default to 10km
		}

        Optional<City> city = City.getCityFromLatLon(latitude, longitude);
        List<Place> places = new ArrayList<>();
        if (city.isPresent()) {
            places = placeRepository.findByCity(city.get());
        }

		if (!StringUtil.empty(requestCategory)) {
			PhotoCategoryEnum category = PhotoCategoryEnum.resolveCategory(requestCategory);
			places = getPlacesForCategory(category, places);
		}

		return places.stream()
			.filter(getPlaceDistancePredicate(latitude, longitude, radiusInKm))
			.map(converter::convert)
			.collect(Collectors.toList());
    }

	private Predicate<Place> getPlaceDistancePredicate(String latitude, String longitude, int radiusInKm) {
		return pl -> LocationUtil.haversineDistanceInKm(latitude,longitude, pl.getLatitude(), pl.getLongitude()) <= radiusInKm;
	}

	@Transactional
    public List<Place> getPlacesForCategory(PhotoCategoryEnum category, List<Place> places) {
        return places.stream()
            .filter(p -> p.getCategories()
                          .stream()
                          .anyMatch(cat -> cat.getCategory().name().equalsIgnoreCase(category.name())))
            .collect(Collectors.toList());
    }

    public List<PlaceDto> getPopularPlacesBySeason(String latitude, String longitude) {
		validateLocation(latitude, longitude);

		return photoRepository.findNearByLatAndLon(latitude, longitude)
				.stream()
				.filter(photo -> Seasons.isInCurrentSeason(photo.getDateUploaded()))
				.filter(StreamUtil.distinctByKey(Photo::getLatitude).and(StreamUtil.distinctByKey(Photo::getLongitude)))
				.limit(25)
				.map(photo -> {
					List<Place> matchedPlaces = placeRepository.findNearByLatLon(photo.getLatitude(), photo.getLongitude(), 0.2F);

					if (matchedPlaces.isEmpty()) {
						return null;
					}
					return matchedPlaces.get(0);
				})
				.filter(Objects::nonNull)
				.map(converter::convert)
				.sorted(Comparator.comparingInt(PlaceDto::getVisits))
				.collect(Collectors.toList());
	}

    private void validateLocation(String lat, String lon) {
        Optional<City> cityOptional = City.getCityFromLatLon(lat, lon);

        if (cityOptional.isPresent()) {
            return;
        }

        throw new NoSuchCityException();
    }
}
