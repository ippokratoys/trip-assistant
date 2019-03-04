package com.mantzavelas.tripassistantapi.repositories;

import com.mantzavelas.tripassistantapi.models.PhotoCategory;
import com.mantzavelas.tripassistantapi.models.PhotoCategoryEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoCategoryRepository extends JpaRepository<PhotoCategory, Long> {

    PhotoCategory findByCategory(PhotoCategoryEnum category);

    boolean existsByCategory(PhotoCategoryEnum category);

    @Query(value = "SELECT * FROM photo_category a WHERE a.id IN (SELECT b.photo_category_id FROM photo_category_tags b WHERE b.tags = :tagName)" , nativeQuery = true)
    List<PhotoCategory> findByCategoryTag(@Param("tagName") String tag);
}
