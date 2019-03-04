package com.mantzavelas.tripassistantapi.repositories;

import com.mantzavelas.tripassistantapi.models.Photo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    Photo findById(long photoId);
    Photo findByLatitudeAndLongitude(String lat, String lon);

    Optional<Photo> findByUrl(String url);

    @Query(value = "SELECT MAX(u.date_uploaded) AS " + "max_date_uploaded" + " FROM photo u",nativeQuery = true)
    Date findTheLatestPhoto();

    @Query(value = "SELECT * FROM photo u WHERE u.id NOT IN (SELECT x.photo_id FROM photo_categories x)" , nativeQuery = true)
    Slice<Photo> findAllUncategorized(Pageable pageable);
}
