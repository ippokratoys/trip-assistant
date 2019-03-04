package com.mantzavelas.tripassistantapi.repositories;

import com.mantzavelas.tripassistantapi.models.City;
import com.mantzavelas.tripassistantapi.models.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    Place findByLatitudeAndLongitude(String latitude, String longitude);

    List<Place> findByCity(City city);
}
