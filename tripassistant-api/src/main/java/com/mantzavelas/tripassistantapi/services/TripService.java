package com.mantzavelas.tripassistantapi.services;

import com.mantzavelas.tripassistantapi.converters.TripResourceToTripConverter;
import com.mantzavelas.tripassistantapi.converters.TripToTripDtoConverter;
import com.mantzavelas.tripassistantapi.dtos.TripDto;
import com.mantzavelas.tripassistantapi.exceptions.EmptyResourceFieldException;
import com.mantzavelas.tripassistantapi.exceptions.PastDateException;
import com.mantzavelas.tripassistantapi.exceptions.ResourceNotFoundException;
import com.mantzavelas.tripassistantapi.exceptions.UnauthorizedException;
import com.mantzavelas.tripassistantapi.models.Trip;
import com.mantzavelas.tripassistantapi.models.User;
import com.mantzavelas.tripassistantapi.repositories.TripRepository;
import com.mantzavelas.tripassistantapi.resources.TripResource;
import com.mantzavelas.tripassistantapi.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class TripService {

	private final TripRepository tripRepository;
	private final TripResourceToTripConverter resourceConverter;
	private final TripToTripDtoConverter dtoConverter;

	@Autowired
	public TripService(TripRepository tripRepository, TripResourceToTripConverter resourceConverter, TripToTripDtoConverter dtoConverter) {this.tripRepository = tripRepository;
		this.resourceConverter = resourceConverter;
		this.dtoConverter = dtoConverter;
	}

	public TripDto createUserTrip(User user, TripResource resource) {
		validateResource(resource);

		Trip trip = resourceConverter.convert(resource);

		trip.setUser(user);
		trip.setStatus(Trip.Status.FUTURE);

		return dtoConverter.convert(tripRepository.save(trip));
	}

	public TripDto editUserTrip(User user, TripResource resource, Long tripId) {
		validateResource(resource);


		Trip trip = tripRepository.findById(tripId).orElse(null);

		if (trip == null) {
			throw new ResourceNotFoundException("Trip", tripId);
		}

		if (!user.equals(trip.getUser())) {
			throw new UnauthorizedException();
		}
		trip = resourceConverter.convert(resource);

		return dtoConverter.convert(tripRepository.save(trip));
	}

	public void deleteUserTrip(User user, Long tripId) {

		Trip trip = tripRepository.findById(tripId).orElse(null);

		if (trip == null) {
			throw new ResourceNotFoundException("Trip", tripId);
		}

		if (!user.equals(trip.getUser())) {
			throw new UnauthorizedException();
		}

		tripRepository.delete(trip);
	}

	private void validateResource(TripResource resource) {
		if (resource.getTitle() == null || resource.getTitle().isEmpty()) {
			System.out.println(resource.getTitle());
			throw new EmptyResourceFieldException("Title can't be empty.");
		}

		if (resource.getPlaces() == null || resource.getPlaces().isEmpty()) {
			throw new EmptyResourceFieldException("Places can't be empty.");
		}

		if (resource.getScheduledFor() == null || resource.getScheduledFor().before(new Date())) {
			throw new PastDateException("Date can't be in the past.");
		}
	}

	public List<TripDto> getUserTrips(User user, String status) {

		List<Trip> userTrips = tripRepository.findByUser(user);

		if (userTrips == null) {
			return Collections.emptyList();
		}

		userTrips.stream()
			.filter(getTripStatusPredicate(Trip.Status.UPCOMING.name()).negate())
			.forEach(getTransitionUpcomingTripConsumer());

		return userTrips.stream()
			.filter(getTripStatusPredicate(status))
			.map(dtoConverter::convert)
			.collect(Collectors.toList());
	}

	private Predicate<Trip> getTripStatusPredicate(String status) {
		return trip -> status == null
				|| "".equals(status)
				|| status.equalsIgnoreCase(trip.getStatus().name());
	}

	@Transactional(rollbackFor = {Exception.class})
	Consumer<Trip> getTransitionUpcomingTripConsumer() {
		return trip -> {
			if (DateUtils.isDateWithinAWeek(trip.getScheduledFor())) {
				trip.setStatus(Trip.Status.UPCOMING);
				tripRepository.save(trip);
			}
		};
	}
}
