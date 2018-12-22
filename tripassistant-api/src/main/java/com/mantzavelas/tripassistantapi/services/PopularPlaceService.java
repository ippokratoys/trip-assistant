package com.mantzavelas.tripassistantapi.services;

import com.mantzavelas.tripassistantapi.models.PopularPlace;
import com.mantzavelas.tripassistantapi.repositories.PopularPlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PopularPlaceService {

    private final PopularPlaceRepository repository;

    @Autowired
    public PopularPlaceService(PopularPlaceRepository repository) {
        this.repository = repository;
    }

    public List<PopularPlace> getTop10Places() {
        List<PopularPlace> popularPlaces = repository.findAll();

        if(popularPlaces.size() > 10) {
            popularPlaces = popularPlaces.stream()
                .sorted(Comparator.comparing(PopularPlace::getTimesMentioned).reversed())
                .limit(10)
                .collect(Collectors.toList());
        }

        return popularPlaces;
    }
}
