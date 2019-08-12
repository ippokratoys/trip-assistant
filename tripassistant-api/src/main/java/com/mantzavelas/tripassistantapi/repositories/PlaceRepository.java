package com.mantzavelas.tripassistantapi.repositories;

import com.mantzavelas.tripassistantapi.models.City;
import com.mantzavelas.tripassistantapi.models.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

    Place findByLatitudeAndLongitude(String latitude, String longitude);

    List<Place> findByCity(City city);

    @Query(value = "SELECT * FROM PLACE u WHERE (" + FIND_NEAR_QUERY_WHERE + ") < :range ORDER BY " + FIND_NEAR_QUERY_WHERE + " ASC" , nativeQuery = true)
	List<Place> findNearByLatLon(@Param("latitude") String lat, @Param("longitude") String lon, @Param("range") float range);

    String FIND_NEAR_QUERY_WHERE = "6372.8 * 2 * ASIN(SQRT(POWER(SIN(RADIANS(CAST(u.Latitude AS FLOAT) - CAST(:latitude AS FLOAT))/2),2) "
            + " + POWER(SIN(RADIANS(CAST(u.Longitude AS FLOAT) - CAST(:longitude AS FLOAT))/2),2) "
            + "* COS(RADIANS(CAST(:latitude AS FLOAT))) * COS(RADIANS(CAST(u.Latitude AS FLOAT)))))";

}
