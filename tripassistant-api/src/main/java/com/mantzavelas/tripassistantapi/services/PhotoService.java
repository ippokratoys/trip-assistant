package com.mantzavelas.tripassistantapi.services;

import com.mantzavelas.tripassistantapi.models.Photo;
import com.mantzavelas.tripassistantapi.models.PopularPlace;
import com.mantzavelas.tripassistantapi.photos.utils.*;
import com.mantzavelas.tripassistantapi.repositories.PhotoRepository;
import com.mantzavelas.tripassistantapi.repositories.PopularPlaceRepository;
import com.mantzavelas.tripassistantapi.utils.BeanUtil;
import com.mantzavelas.tripassistantapi.utils.DateUtils;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.clustering.DoublePoint;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PhotoService {

    private List<Photo> photoList = new ArrayList<>();
    private int currentPage = 1;
    private int totalPages;
    private PhotoInfoToPhotoConverter converter = new PhotoInfoToPhotoConverter();
    private PhotoRepository photoRepository = BeanUtil.getBean(PhotoRepository.class);
    private PopularPlaceRepository placeRepository = BeanUtil.getBean(PopularPlaceRepository.class);

    public void persistPhotosFromFlickr() throws InterruptedException {
        Date startDate = photoRepository.findTheLatestPhoto();

        FlickrSearchResponse photos = FlickrRestClient.create().searchPhotosFrom(DateUtils.addSecondsToDate(startDate,1));
        totalPages = photos.getPhotos().getPages();

        if(!photos.getPhotos().getPhoto().isEmpty()) {
            while (currentPage <= totalPages) {
                currentPage = photos.getPhotos().getPage();

                photoList = photos.getPhotos()
                        .getPhoto()
                        .stream()
                        .map(getPhotoInfoMapper())
                        .map(converter::convert)
                        .filter(Objects::nonNull)
                        .sorted(Comparator.comparing(Photo::getDateUploaded))
                        .collect(Collectors.toList());

                Photo latestPhoto = photoList.get(0);

                if (photoRepository.findByUrl(latestPhoto.getUrl()).isPresent()) {
                    break;
                }

                photoRepository.saveAll(photoList);

                Thread.sleep(120000);

                currentPage++;
                photos = FlickrRestClient.create().searchPhotosFrom(startDate, Integer.toString(currentPage));
            }
        }

        System.out.println("Exited PhotoService");
    }

    private Function<FlickrPhotoSearch, FlickrPhotoInfo> getPhotoInfoMapper() {
        return photoSearch -> FlickrRestClient.create()
                .getPhotoInfo(photoSearch.getId())
                .getPhoto();
    }

    public void clusterPhotosBasedOnLocation() {
        List<Photo> photos = photoRepository.findAll();
        List<DoublePoint> points = getDoublePointsFromPhotoLatLon(photos);

        List<Cluster<DoublePoint>> originalClusters = executeAlgorithm(points, 4, 0.0001953125);
        List<Cluster<DoublePoint>> clustersToRecluster = originalClusters.stream().filter(cl -> cl.getPoints().size() > photos.size()* 0.1).collect(Collectors.toList());
        originalClusters.removeAll(clustersToRecluster);

        while(!clustersToRecluster.isEmpty() && clustersToRecluster.stream().anyMatch(c -> c.getPoints().size() > photos.size()*0.1)) {
            List<DoublePoint> pointsToAdd = new ArrayList<>();
            if (!clustersToRecluster.isEmpty()) {
                for (Cluster cluster : clustersToRecluster) {
                    for (Object clusterPoint : cluster.getPoints()) {
                        double[] latLon = ((DoublePoint) clusterPoint).getPoint();
                        DoublePoint newDoublePoint = new DoublePoint(latLon);
                        pointsToAdd.add(newDoublePoint);
                    }
                }
            }
            List<Cluster<DoublePoint>> newClusterResult = executeAlgorithm(pointsToAdd, 2, 0.00001);
            originalClusters.addAll(newClusterResult);
            clustersToRecluster = newClusterResult.stream().filter(cl -> cl.getPoints().size() > photos.size()* 0.1).collect(Collectors.toList());
            originalClusters.removeAll(clustersToRecluster);
        }

        originalClusters.sort((o1, o2) -> o2.getPoints().size() - o1.getPoints().size());

        List<PopularPlace> popularPlaces = originalClusters.stream()
                .limit(10)
                .map(clusterToPlaceMapper())
                .collect(Collectors.toList());

        keepTop10PlacesInDB(popularPlaces);
    }

    private Function<Cluster<DoublePoint>, PopularPlace> clusterToPlaceMapper() {
        return c -> {
            PopularPlace place = new PopularPlace();
            place.setLatitude(Double.toString(c.getPoints().get(0).getPoint()[0]));
            place.setLongitude(Double.toString(c.getPoints().get(0).getPoint()[1]));
            place.setTimesMentioned(c.getPoints().size());

            return place; };
    }

    public void keepTop10PlacesInDB(List<PopularPlace> popularPlaces) {
        placeRepository.deleteAll();
        placeRepository.saveAll(popularPlaces);
    }

    public List<Cluster<DoublePoint>> executeAlgorithm(List<DoublePoint> points, int minPts, double eps) {
        DBSCANClusterer clusterer = new DBSCANClusterer(eps, minPts);
        return clusterer.cluster(points);
    }

    public List<DoublePoint> getDoublePointsFromPhotoLatLon(List<Photo> photos) {
        return photos.stream()
                     .map(photo -> {
                         double[] latLon = new double[2];
                         latLon[0] = Double.parseDouble(photo.getLatitude());
                         latLon[1] = Double.parseDouble(photo.getLongitude());

                         return new DoublePoint(latLon); })
                     .collect(Collectors.toList());
    }
}
