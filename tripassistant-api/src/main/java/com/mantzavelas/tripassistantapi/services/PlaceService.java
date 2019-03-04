package com.mantzavelas.tripassistantapi.services;

import com.mantzavelas.tripassistantapi.converters.PlaceToPlaceDtoConverter;
import com.mantzavelas.tripassistantapi.dtos.PlaceDto;
import com.mantzavelas.tripassistantapi.exceptions.NoSuchCategoryException;
import com.mantzavelas.tripassistantapi.exceptions.NoSuchCityException;
import com.mantzavelas.tripassistantapi.models.City;
import com.mantzavelas.tripassistantapi.models.PhotoCategoryEnum;
import com.mantzavelas.tripassistantapi.models.Place;
import com.mantzavelas.tripassistantapi.repositories.PlaceRepository;
import com.mantzavelas.tripassistantapi.utils.LocationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class PlaceService {

    private PlaceRepository placeRepository;

    private PlaceToPlaceDtoConverter converter;

	@Autowired
	public PlaceService(PlaceRepository placeRepository, PlaceToPlaceDtoConverter converter) {
		this.placeRepository = placeRepository;
		this.converter = converter;
	}

    public List<PlaceDto> searchPlacesForCategoryNear(String latitude, String longitude, Integer radiusInKm, String requestCategory) {

        validateLocation(latitude, longitude);

        if (radiusInKm == null || radiusInKm <=0) {
        	radiusInKm = 10; // default to 10km
		}

        Optional<City> city = getCityFromLatLon(latitude, longitude);
        List<Place> places = new ArrayList<>();
        if (city.isPresent()) {
            places = placeRepository.findByCity(city.get());
        }

		if (requestCategory != null) {
			PhotoCategoryEnum category = resolveCategory(requestCategory);
			places = getPlacesForCategory(category, places);
		}

		return places.stream()
			.filter(getPlaceDistancePredicate(latitude, longitude, radiusInKm))
			.map(converter::convert)
			.collect(Collectors.toList());
    }

	private PhotoCategoryEnum resolveCategory(String category) {
		return Arrays.stream(PhotoCategoryEnum.values())
			.filter(categoryEnum -> categoryEnum.name().equalsIgnoreCase(category))
    		.findFirst()
			.orElseThrow(NoSuchCategoryException::new);
	}

	private Predicate<Place> getPlaceDistancePredicate(String latitude, String longitude, int radiusInKm) {
		return pl -> LocationUtil.haversineDistanceInKm(latitude,longitude, pl.getLatitude(), pl.getLongitude()) <= radiusInKm;
	}

	@Transactional
    List<Place> getPlacesForCategory(PhotoCategoryEnum category, List<Place> places) {
        return places.stream()
            .filter(p -> p.getCategories()
                          .stream()
                          .anyMatch(cat -> cat.getCategory().name().equalsIgnoreCase(category.name())))
            .collect(Collectors.toList());
    }

    private void validateLocation(String lat, String lon) {
        Optional<City> cityOptional = getCityFromLatLon(lat, lon);

        if (cityOptional.isPresent()) {
            return;
        }

        throw new NoSuchCityException();
    }

    private Optional<City> getCityFromLatLon(String lat, String lon) {
        return Arrays.stream(City.values())
			.filter(getCityDistancePredicate(lat, lon))
            .findFirst();
    }

	private Predicate<City> getCityDistancePredicate(String lat, String lon) {
		return city -> LocationUtil.haversineDistanceInKm(lat, lon, city.getLatitude(), city.getLongitude()) < 100.0;
	}
}
