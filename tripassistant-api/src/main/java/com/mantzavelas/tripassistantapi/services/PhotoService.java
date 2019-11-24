package com.mantzavelas.tripassistantapi.services;

import com.mantzavelas.tripassistantapi.models.Photo;
import com.mantzavelas.tripassistantapi.models.PopularPlace;
import com.mantzavelas.tripassistantapi.photos.flickr.FlickrPhotoInfo;
import com.mantzavelas.tripassistantapi.photos.flickr.FlickrPhotoSearch;
import com.mantzavelas.tripassistantapi.photos.flickr.FlickrRestClient;
import com.mantzavelas.tripassistantapi.photos.flickr.FlickrSearchResponse;
import com.mantzavelas.tripassistantapi.photos.utils.PhotoInfoToPhotoConverter;
import com.mantzavelas.tripassistantapi.repositories.PhotoRepository;
import com.mantzavelas.tripassistantapi.repositories.PopularPlaceRepository;
import com.mantzavelas.tripassistantapi.utils.BeanUtil;
import com.mantzavelas.tripassistantapi.utils.DateUtils;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.clustering.DoublePoint;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PhotoService {

    private int currentPage = 1;

    private PhotoInfoToPhotoConverter converter = new PhotoInfoToPhotoConverter();

    private PhotoRepository photoRepository;
    private PopularPlaceRepository placeRepository;
    private FlickrRestClient flickrRestClient;

    public PhotoService() {
        photoRepository = BeanUtil.getBean(PhotoRepository.class);
        placeRepository = BeanUtil.getBean(PopularPlaceRepository.class);
        flickrRestClient = FlickrRestClient.create();
    }

    public PhotoService(PhotoRepository photoRepository, PopularPlaceRepository placeRepository, FlickrRestClient client) {
        this.photoRepository = photoRepository;
        this.placeRepository = placeRepository;
        this.flickrRestClient = client;
    }

    public void persistPhotosFromFlickr() throws InterruptedException {
        Date startDate = photoRepository.findTheLatestPhoto();

        FlickrSearchResponse photos = flickrRestClient.searchPhotosFrom(DateUtils.addSecondsToDate(startDate,1));
        int totalPages = (photos!=null && photos.getPhotos()!=null) ? photos.getPhotos().getPages() : 0;

        if(!isPhotoResponseEmptyOrNull(photos)) {
            while (currentPage <= totalPages) {
                currentPage = photos.getPhotos().getPage();

                List<Photo> photosToAdd = photos.getPhotos()
                    .getPhoto()
                    .stream()
                    .map(getPhotoInfoMapper())
                    .map(converter::convert)
                    .filter(Objects::nonNull)
                    .sorted(Comparator.comparing(Photo::getDateUploaded))
                    .collect(Collectors.toList());

                Photo latestPhoto = photosToAdd.get(0);

                if (!photoRepository.findByUrl(latestPhoto.getUrl()).isEmpty()) {
                    break;
                }

                photoRepository.saveAll(photosToAdd);

                currentPage++;
                photos = flickrRestClient.searchPhotosFrom(startDate, Integer.toString(currentPage));
            }
        }
    }

    private boolean isPhotoResponseEmptyOrNull(FlickrSearchResponse photos) {
        return photos==null || photos.getPhotos()==null
            || photos.getPhotos().getPhoto()==null || photos.getPhotos().getPhoto().isEmpty();
    }

    private Function<FlickrPhotoSearch, FlickrPhotoInfo> getPhotoInfoMapper() {
        return photoSearch -> flickrRestClient.getPhotoInfo(photoSearch.getId())
                                              .getPhoto();
    }

    public void clusterPhotosBasedOnLocation() {
        List<Photo> photos = photoRepository.findAll();

        if(!photos.isEmpty()) {
            List<DoublePoint> points = getDoublePointsFromPhotoLatLon(photos);

            List<Cluster<DoublePoint>> firstClusteringResult = executeAlgorithm(points, 4, 0.0001953125);
            List<Cluster<DoublePoint>> clustersToRecluster;

            do{
                clustersToRecluster = firstClusteringResult.stream()
                    .filter(getBigClusterFilter(photos))
                    .collect(Collectors.toList());

                if (!clustersToRecluster.isEmpty()) {
                    List<DoublePoint> pointsToAdd = new ArrayList<>();

                    for (Cluster cluster : clustersToRecluster) {
                        for (Object clusterPoint : cluster.getPoints()) {
                            double[] latLon = ((DoublePoint) clusterPoint).getPoint();
                            DoublePoint newDoublePoint = new DoublePoint(latLon);
                            pointsToAdd.add(newDoublePoint);
                        }
                    }

                    List<Cluster<DoublePoint>> newClusterResult = executeAlgorithm(pointsToAdd, 2, 0.00001);

                    if(newClusterResult.isEmpty()) {
                        break;
                    }
                    firstClusteringResult.removeAll(clustersToRecluster); //remove the clusters which will got reclustered.
                    firstClusteringResult.addAll(newClusterResult);
                }
            } while (!clustersToRecluster.isEmpty() && clustersToRecluster.stream().anyMatch(getBigClusterFilter(photos)));

            firstClusteringResult.sort((o1, o2) -> o2.getPoints().size() - o1.getPoints().size());

            List<PopularPlace> popularPlaces = firstClusteringResult.stream()
                    .limit(10)
                    .map(clusterToPlaceMapper())
                    .collect(Collectors.toList());

            saveTop10PlacesFrom(popularPlaces);
        }
    }

    private Predicate<Cluster<DoublePoint>> getBigClusterFilter(List<Photo> photos) {
        //check if cluster has more points than 10% of the total photos. if so, recluster this one.
        return cl -> cl.getPoints().size() > photos.size() * 0.1;
    }

    private Function<Cluster<DoublePoint>, PopularPlace> clusterToPlaceMapper() {
        return c -> {
            PopularPlace place = new PopularPlace();
            place.setLatitude(Double.toString(c.getPoints().get(0).getPoint()[0]));
            place.setLongitude(Double.toString(c.getPoints().get(0).getPoint()[1]));
            place.setTimesMentioned(c.getPoints().size());

            return place; };
    }

    private void saveTop10PlacesFrom(List<PopularPlace> popularPlaces) {
        if(!popularPlaces.isEmpty()) {
            placeRepository.deleteAll();
            placeRepository.saveAll(popularPlaces);
        }
    }

    private List<Cluster<DoublePoint>> executeAlgorithm(List<DoublePoint> points, int minPts, double eps) {
        DBSCANClusterer clusterer = new DBSCANClusterer(eps, minPts);
        return clusterer.cluster(points);
    }

    private List<DoublePoint> getDoublePointsFromPhotoLatLon(List<Photo> photos) {
        return photos.stream()
                     .map(photo -> {
                         double[] latLon = new double[2];
                         latLon[0] = Double.parseDouble(photo.getLatitude());
                         latLon[1] = Double.parseDouble(photo.getLongitude());

                         return new DoublePoint(latLon); })
                     .collect(Collectors.toList());
    }
}
