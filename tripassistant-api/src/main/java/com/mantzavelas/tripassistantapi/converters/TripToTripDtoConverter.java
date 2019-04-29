package com.mantzavelas.tripassistantapi.converters;

import com.mantzavelas.tripassistantapi.dtos.TripDto;
import com.mantzavelas.tripassistantapi.models.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TripToTripDtoConverter implements Converter<Trip, TripDto> {

	private final PlaceToPlaceDtoConverter converter;

	@Autowired
	public TripToTripDtoConverter(PlaceToPlaceDtoConverter converter) {this.converter = converter;}

	@Override
	public TripDto convert(Trip source) {
		TripDto dto = new TripDto();

		dto.setId(source.getId());
		dto.setTitle(source.getTitle());
		dto.setDescription(source.getDescription());
		dto.setStatus(source.getStatus());
		dto.setScheduledFor(source.getScheduledFor());
		dto.setPlaces(source.getPlaces()
							.stream()
							.map(converter::convert)
							.collect(Collectors.toList()));

		return dto;
	}
}
