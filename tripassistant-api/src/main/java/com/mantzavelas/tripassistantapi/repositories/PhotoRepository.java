package com.mantzavelas.tripassistantapi.repositories;

import com.mantzavelas.tripassistantapi.models.Photo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    Photo findById(long photoId);
    Photo findByLatitudeAndLongitude(String lat, String lon);

    List<Photo> findByUrl(String url);

    @Query(value = "SELECT MAX(u.date_uploaded) AS " + "max_date_uploaded" + " FROM photo u",nativeQuery = true)
    Date findTheLatestPhoto();

    @Query(value = "SELECT * FROM photo u WHERE u.id NOT IN (SELECT x.photo_id FROM photo_categories x)" , nativeQuery = true)
    Slice<Photo> findAllUncategorized(Pageable pageable);

    @Query(value = "SELECT * FROM PHOTO u WHERE (" + FIND_NEAR_QUERY_WHERE + ") < 50", nativeQuery = true)
    List<Photo> findNearByLatAndLon(@Param("latitude") String lat, @Param("longitude") String lon);

    String FIND_NEAR_QUERY_WHERE = "6372.8 * 2 * ASIN(SQRT(POWER(SIN(RADIANS(CAST(u.Latitude AS FLOAT) - CAST(:latitude AS FLOAT))/2),2) "
			+ " + POWER(SIN(RADIANS(CAST(u.Longitude AS FLOAT) - CAST(:longitude AS FLOAT))/2),2) "
			+ "* COS(RADIANS(CAST(:latitude AS FLOAT))) * COS(RADIANS(CAST(u.Latitude AS FLOAT)))))";
}
