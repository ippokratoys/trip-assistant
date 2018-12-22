package com.mantzavelas.tripassistantapi.controllers;

import com.mantzavelas.tripassistantapi.models.PopularPlace;
import com.mantzavelas.tripassistantapi.services.PopularPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/places")
public class PlaceController {

    @Autowired
    private PopularPlaceService popularPlaceService;

    @GetMapping
    public ResponseEntity<List<PopularPlace>> getTop10Places() {
        return new ResponseEntity<>(popularPlaceService.getTop10Places(), HttpStatus.OK);
    }
}
