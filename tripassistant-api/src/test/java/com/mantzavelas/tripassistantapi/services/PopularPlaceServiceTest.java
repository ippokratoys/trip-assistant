package com.mantzavelas.tripassistantapi.services;

import com.mantzavelas.tripassistantapi.models.PopularPlace;
import com.mantzavelas.tripassistantapi.repositories.PopularPlaceRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PopularPlaceServiceTest {

    @MockBean
    private PopularPlaceRepository popularPlaceRepository;

    @Autowired
    private PopularPlaceService popularPlaceService;

    private List<PopularPlace> popularPlaces = Arrays.asList(
        new PopularPlace(1L,"10.01","11.01","test",50),
        new PopularPlace(2L,"10.01","11.01","test",10),
        new PopularPlace(3L,"10.01","11.01","test",13),
        new PopularPlace(4L,"10.01","11.01","test",2),
        new PopularPlace(5L,"10.01","11.01","test",5),
        new PopularPlace(6L,"10.01","11.01","test",25),
        new PopularPlace(7L,"10.01","11.01","test",34),
        new PopularPlace(8L,"10.01","11.01","test",29),
        new PopularPlace(9L,"10.01","11.01","test",18),
        new PopularPlace(10L,"10.01","11.01","test",54)
    );

    private List<PopularPlace> popularPlacesBigList = Arrays.asList(
            new PopularPlace(1L,"10.01","11.01","test",50),
            new PopularPlace(2L,"10.01","11.01","test",10),
            new PopularPlace(3L,"10.01","11.01","test",13),
            new PopularPlace(4L,"10.01","11.01","test",2),
            new PopularPlace(5L,"10.01","11.01","test",5),
            new PopularPlace(6L,"10.01","11.01","test",25),
            new PopularPlace(7L,"10.01","11.01","test",34),
            new PopularPlace(8L,"10.01","11.01","test",29),
            new PopularPlace(9L,"10.01","11.01","test",18),
            new PopularPlace(10L,"10.01","11.01","test",255),
            new PopularPlace(11L,"10.01","11.01","test",102),
            new PopularPlace(12L,"10.01","11.01","test",103),
            new PopularPlace(13L,"10.01","11.01","test",131),
            new PopularPlace(14L,"10.01","11.01","test",21)
    );

    @Test
    public void getTop10Places() {
        Mockito.when(popularPlaceRepository.findAll()).thenReturn(popularPlaces);

        List<PopularPlace> actualPlaces = popularPlaceService.getTop10Places();

        assertEquals(popularPlaces,actualPlaces);
        assertEquals(popularPlaces.size(), actualPlaces.size());
    }

    @Test
    public void getTop10PlacesFromBigList() {
        Mockito.when(popularPlaceRepository.findAll()).thenReturn(popularPlacesBigList);

        List<PopularPlace> actualPlaces = popularPlaceService.getTop10Places();
        int expectedMaxTimesMentioned = 255;
        int actualMaxTimesMentioned = actualPlaces.get(0).getTimesMentioned();
        int expectedListSize = 10;

        assertEquals(expectedListSize, actualPlaces.size());
        assertEquals(expectedMaxTimesMentioned, actualMaxTimesMentioned);
    }
}