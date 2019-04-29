package com.mantzavelas.tripassistantapi.converters;

import com.mantzavelas.tripassistantapi.dtos.PlaceDto;
import com.mantzavelas.tripassistantapi.models.Place;
import com.mantzavelas.tripassistantapi.models.Trip;
import com.mantzavelas.tripassistantapi.repositories.PlaceRepository;
import com.mantzavelas.tripassistantapi.resources.TripResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class TripResourceToTripConverter implements Converter<TripResource, Trip> {

	private final PlaceRepository placeRepository;

	@Autowired
	public TripResourceToTripConverter(PlaceRepository placeRepository) {this.placeRepository = placeRepository;}

	@Override
	public Trip convert(TripResource source) {
		Trip trip = new Trip();

		trip.setTitle(source.getTitle());
		trip.setDescription(source.getDescription());
		trip.setPlaces(resolvePlaces(source.getPlaces()));
		trip.setStatus(source.getStatus());
		trip.setScheduledFor(source.getScheduledFor());

		return trip;
	}

	private List<Place> resolvePlaces(List<PlaceDto> placesDto) {
		List<Place> places;

		places = placesDto.stream()
			.filter(Objects::nonNull)
			.map(pl -> placeRepository.findByLatitudeAndLongitude(pl.getLatitude(), pl.getLongitude()))
			.filter(Objects::nonNull)
			.collect(Collectors.toList());

		return places;
	}
}
