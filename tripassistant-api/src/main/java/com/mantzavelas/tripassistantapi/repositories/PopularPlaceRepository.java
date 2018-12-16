package com.mantzavelas.tripassistantapi.repositories;

import com.mantzavelas.tripassistantapi.models.PopularPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PopularPlaceRepository extends JpaRepository<PopularPlace, Long> {


}
