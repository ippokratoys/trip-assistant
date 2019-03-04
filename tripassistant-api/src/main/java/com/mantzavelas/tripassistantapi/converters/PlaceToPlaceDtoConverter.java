package com.mantzavelas.tripassistantapi.converters;

import com.mantzavelas.tripassistantapi.dtos.PlaceDto;
import com.mantzavelas.tripassistantapi.models.Place;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PlaceToPlaceDtoConverter implements Converter<Place, PlaceDto> {

	@Override
	public PlaceDto convert(Place source) {
		PlaceDto dto = new PlaceDto();

		dto.setTitle(source.getTitle());
		dto.setDescription(source.getDescription());
		dto.setLatitude(source.getLatitude());
		dto.setLongitude(source.getLongitude());
		dto.setVisits(source.getVisits());
		dto.setPhotoUrls(source.getPhotoUrls());

		return dto;
	}
}
