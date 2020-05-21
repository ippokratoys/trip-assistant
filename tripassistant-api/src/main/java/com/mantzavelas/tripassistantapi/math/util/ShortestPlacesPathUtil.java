package com.mantzavelas.tripassistantapi.math.util;

import com.mantzavelas.tripassistantapi.dtos.PlaceDto;
import com.mantzavelas.tripassistantapi.utils.LocationUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class ShortestPlacesPathUtil {

	private ShortestPlacesPathUtil() { /* Prevent initialization since this is a utility class */}

	public static void sortPlacesForShortestPath(List<PlaceDto> places, PlaceDto startingPoint) {

		if (places == null || places.isEmpty()) {
			return;
		}
		List<PlaceDto> placeDtos = new ArrayList<>(places);
		List<PlaceDto> visitedPlaces = new LinkedList<>();

		double shortest = Integer.MAX_VALUE;
		PlaceDto shortestPlace = null;
		PlaceDto nextPlace = startingPoint;
		visitedPlaces.add(startingPoint);

		while (!placeDtos.isEmpty()) {

			for (PlaceDto placeDto : placeDtos) {
				double dist = LocationUtil.haversineDistanceInKm(nextPlace.getLatitude(), nextPlace.getLongitude(), placeDto.getLatitude(), placeDto.getLongitude());
				if (dist < shortest) {
					shortestPlace = placeDto;
					shortest = dist;
				}
			}
			placeDtos.remove(shortestPlace);
			visitedPlaces.add(shortestPlace);
			nextPlace = shortestPlace;
			shortest = Integer.MAX_VALUE;
		}

		places.clear();
		places.addAll(visitedPlaces);
	}
}
