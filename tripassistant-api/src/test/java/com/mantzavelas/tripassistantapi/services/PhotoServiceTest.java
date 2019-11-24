package com.mantzavelas.tripassistantapi.services;

import com.mantzavelas.tripassistantapi.models.Photo;
import com.mantzavelas.tripassistantapi.photos.flickr.*;
import com.mantzavelas.tripassistantapi.repositories.PhotoRepository;
import com.mantzavelas.tripassistantapi.repositories.PopularPlaceRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PhotoServiceTest {

    private PopularPlaceRepository mockPlaceRepository;
    private PhotoRepository mockPhotoRepository;
    private FlickrRestClient mockClient;

    private PhotoService photoService;

    private static List<Photo> photoList = Arrays.asList(
            new Photo(1L, "t1", "d1", 0, new Date(), "url1", "40.625815", "22.947585"),
            new Photo(2L, "t2", "d2", 0, new Date(), "url2", "40.634896", "22.936706"),
            new Photo(3L, "t3", "d3", 0, new Date(), "url3", "40.626491", "22.948347"),
            new Photo(4L, "t4", "d4", 0, new Date(), "url4", "40.627377", "22.947341"),
            new Photo(1L, "t1", "d1", 0, new Date(), "url1", "40.613127", "22.953875"),
            new Photo(2L, "t2", "d2", 0, new Date(), "url2", "40.613097", "22.954061"),
            new Photo(3L, "t3", "d3", 0, new Date(), "url3", "40.602058", "22.972844"),
            new Photo(4L, "t4", "d4", 0, new Date(), "url4", "40.602058", "22.972844"),
            new Photo(1L, "t1", "d1", 0, new Date(), "url1", "40.602100", "22.972866"),
            new Photo(2L, "t2", "d2", 0, new Date(), "url2", "40.601969", "22.973088"),
            new Photo(3L, "t3", "d3", 0, new Date(), "url3", "40.601772", "22.972983"),
            new Photo(4L, "t4", "d4", 0, new Date(), "url4", "40.601997", "22.972869"),
            new Photo(1L, "t1", "d1", 0, new Date(), "url1", "40.602030", "22.972941"),
            new Photo(2L, "t2", "d2", 0, new Date(), "url2", "40.601975", "22.972880"),
            new Photo(3L, "t3", "d3", 0, new Date(), "url3", "40.602080", "22.972936"),
            new Photo(4L, "t4", "d4", 0, new Date(), "url4", "40.632437", "22.941309")
    );

    @Before
    public void setUp() {
        mockPhotoRepository = Mockito.mock(PhotoRepository.class);
        mockPlaceRepository = Mockito.mock(PopularPlaceRepository.class);
        mockClient = Mockito.mock(FlickrRestClient.class);
        photoService = new PhotoService(mockPhotoRepository, mockPlaceRepository, mockClient);
    }

    @Test
    public void successfullPhotoClustering() {
        Mockito.when(mockPhotoRepository.findAll()).thenReturn(photoList);

        photoService.clusterPhotosBasedOnLocation();

        Mockito.verify(mockPlaceRepository).deleteAll();
        Mockito.verify(mockPlaceRepository).saveAll(Mockito.anyList());
    }

    @Test
    public void abortPhotoClusteringOnEmptyList() {
        Mockito.when(mockPhotoRepository.findAll()).thenReturn(Collections.emptyList());

        photoService.clusterPhotosBasedOnLocation();

        Mockito.verify(mockPlaceRepository, Mockito.never()).deleteAll();
        Mockito.verify(mockPlaceRepository, Mockito.never()).saveAll(Mockito.anyList());
    }

    @Test
    public void testFlickrPhotoPersistenceNoNewPhoto() throws InterruptedException {
        Mockito.when(mockClient.searchPhotosFrom(Mockito.any(Date.class))).thenReturn(constructMockEmptyResponse());

        photoService.persistPhotosFromFlickr();

        Mockito.verify(mockPhotoRepository, Mockito.never()).findByUrl(Mockito.anyString());
        Mockito.verify(mockPhotoRepository, Mockito.never()).saveAll(Mockito.anyList());
    }

    @Test
    public void testFlickrPhotoPersistenceSavePhotos() throws InterruptedException {
        Mockito.when(mockPhotoRepository.findTheLatestPhoto()).thenReturn(new Date());
        Mockito.when(mockClient.searchPhotosFrom(Mockito.any(Date.class))).thenReturn(constructMockResponse());
        Mockito.when(mockClient.searchPhotosFrom(Mockito.any(Date.class), Mockito.anyString())).thenReturn(null);
        Mockito.when(mockClient.getPhotoInfo(Mockito.anyString())).thenReturn(constructMockPhotoInfoResponse());

        photoService.persistPhotosFromFlickr();

        Mockito.verify(mockPhotoRepository).findByUrl(Mockito.anyString());
        Mockito.verify(mockPhotoRepository).saveAll(Mockito.anyList());
    }

    @Test
    public void testFlickrPhotoPersistenceNoSave() throws InterruptedException {
        Mockito.when(mockPhotoRepository.findTheLatestPhoto()).thenReturn(new Date());
        Mockito.when(mockClient.searchPhotosFrom(Mockito.any(Date.class))).thenReturn(constructMockResponse());
        Mockito.when(mockClient.searchPhotosFrom(Mockito.any(Date.class), Mockito.anyString())).thenReturn(null);
        Mockito.when(mockClient.getPhotoInfo(Mockito.anyString())).thenReturn(constructMockPhotoInfoResponse());
        Mockito.when(mockPhotoRepository.findByUrl(Mockito.anyString())).thenReturn(new ArrayList<>(photoList));

        photoService.persistPhotosFromFlickr();

        Mockito.verify(mockPhotoRepository).findByUrl(Mockito.anyString());
        Mockito.verify(mockPhotoRepository, Mockito.never()).saveAll(Mockito.anyList());
    }

    private FlickrSearchResponse constructMockResponse() {
        FlickrSearchResponse fakeResponse = new FlickrSearchResponse();
        FlickrPhotosSearch fakePhotosSearchResponse = new FlickrPhotosSearch();
        FlickrPhotoSearch fakePhotoSearchResponse = new FlickrPhotoSearch("1","a","123","3","1","title", 1, 1, 1);

        fakePhotosSearchResponse.setPhoto(Arrays.asList(fakePhotoSearchResponse));
        fakePhotosSearchResponse.setPage(1);
        fakePhotosSearchResponse.setPages(1);
        fakePhotosSearchResponse.setPerPage(1);
        fakePhotosSearchResponse.setTotal("5");

        fakeResponse.setPhotos(fakePhotosSearchResponse);

        return fakeResponse;
    }

    private FlickrPhotoInfoResponse constructMockPhotoInfoResponse() {
        FlickrPhotoInfoResponse fakeResponse = new FlickrPhotoInfoResponse();
        FlickrPhotoInfo fakePhotoInfoResponse = new FlickrPhotoInfo("1","s","s",1,"123456",1,"os","of","t","d","l","l");

        fakeResponse.setPhoto(fakePhotoInfoResponse);

        return fakeResponse;
    }

    private FlickrSearchResponse constructMockEmptyResponse() {
        return new FlickrSearchResponse(null,null);
    }
}