package com.mantzavelas.tripassistantapi.repositories;

import com.mantzavelas.tripassistantapi.models.Trip;
import com.mantzavelas.tripassistantapi.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

	List<Trip> findByUser(User user);

	Slice<Trip> findByStatus(Trip.Status status, Pageable pageable);
}
