package com.mantzavelas.tripassistantapi.controllers;

import com.mantzavelas.tripassistantapi.dtos.PlaceDto;
import com.mantzavelas.tripassistantapi.models.PopularPlace;
import com.mantzavelas.tripassistantapi.services.PlaceService;
import com.mantzavelas.tripassistantapi.services.PopularPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/places")
public class PlaceController {

    private final PopularPlaceService popularPlaceService;

    private final PlaceService placeService;

    @Autowired
    public PlaceController(PopularPlaceService popularPlaceService, PlaceService placeService) {
        this.popularPlaceService = popularPlaceService;
        this.placeService = placeService;
    }

    @GetMapping
    public ResponseEntity<List<PopularPlace>> getTop10Places() {
        return new ResponseEntity<>(popularPlaceService.getTop10Places(), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PlaceDto>> searchPlaces(@RequestParam String lat, @RequestParam String lon
            , @RequestParam(required = false) Integer radius, @RequestParam(required = false) String category) {
        return new ResponseEntity<>(placeService.searchPlacesForCategoryNear(lat, lon, radius, category), HttpStatus.OK);
    }
}
