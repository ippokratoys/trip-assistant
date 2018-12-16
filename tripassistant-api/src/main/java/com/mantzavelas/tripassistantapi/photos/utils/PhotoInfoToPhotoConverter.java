package com.mantzavelas.tripassistantapi.photos.utils;

import com.mantzavelas.tripassistantapi.models.Photo;
import org.springframework.core.convert.converter.Converter;

import java.time.Instant;
import java.util.Date;

public class PhotoInfoToPhotoConverter implements Converter<FlickrPhotoInfo, Photo> {

    @Override
    public Photo convert(FlickrPhotoInfo source) {
        Photo photo = new Photo();

        photo.setTitle(source.getTitle());
        photo.setDescription(source.getDescription());
        photo.setDateUploaded(Date.from(Instant.ofEpochSecond(Long.parseLong(source.getDateUploaded()))));
        photo.setRotation(source.getRotation());
        photo.setUrl(constructPhotoUrl(source));
        photo.setLatitude(source.getLatitude());
        photo.setLongitude(source.getLongitude());

        return photo;
    }

    private static String constructPhotoUrl(FlickrPhotoInfo photoInfo) {
        return "https://" +
                "farm" +
                photoInfo.getFarm() +
                ".staticflickr.com/" +
                photoInfo.getServer() +
                "/" +
                photoInfo.getId() +
                "_" +
                photoInfo.getSecret() +
                "." +
                (photoInfo.getOriginalFormat() == null ? ".jpg" : photoInfo.getOriginalFormat());
    }
}
