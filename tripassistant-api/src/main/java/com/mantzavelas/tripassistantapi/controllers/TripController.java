package com.mantzavelas.tripassistantapi.controllers;

import com.mantzavelas.tripassistantapi.annotations.AuthenticatedUser;
import com.mantzavelas.tripassistantapi.dtos.LocationDto;
import com.mantzavelas.tripassistantapi.dtos.TripDto;
import com.mantzavelas.tripassistantapi.models.User;
import com.mantzavelas.tripassistantapi.resources.TripResource;
import com.mantzavelas.tripassistantapi.services.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user/trip")
public class TripController {

	private final TripService tripService;

	@Autowired
	public TripController(TripService tripService) {this.tripService = tripService;}

	@PostMapping
	public TripDto createTrip(@AuthenticatedUser User user, @RequestBody TripResource resource) {
		return tripService.createUserTrip(user, resource);
	}

	@PostMapping("/{tripId}")
	public TripDto editTrip(@AuthenticatedUser User user, TripResource resource, @PathVariable("tripId") Long tripId) {
		return tripService.editUserTrip(user, resource, tripId);
	}

	@DeleteMapping("/{tripId}")
	public ResponseEntity deleteTrip(@AuthenticatedUser User user, @PathVariable("tripId") Long tripId) {
		tripService.deleteUserTrip(user, tripId);

		return ResponseEntity.ok().build();
	}

	@GetMapping
	public List<TripDto> getTrips(@AuthenticatedUser User user, @RequestParam("status") String status) {
		return tripService.getUserTrips(user, status);
	}

	@PostMapping("/{tripId}/start")
	public TripDto startTrip(@AuthenticatedUser User user, @PathVariable("tripId") Long tripId, @RequestBody LocationDto dto) {
		return tripService.startUserTrip(user, tripId, dto);
	}

	@PostMapping("/{tripId}/stop")
	public ResponseEntity<Void> stopTrip(@AuthenticatedUser User user, @PathVariable("tripId") Long tripId) {
		tripService.stopTrip(user, tripId);
		return ResponseEntity.ok().build();
	}
}
