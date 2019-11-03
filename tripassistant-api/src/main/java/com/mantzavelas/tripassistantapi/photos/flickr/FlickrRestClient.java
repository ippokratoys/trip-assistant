package com.mantzavelas.tripassistantapi.photos.flickr;

import com.mantzavelas.tripassistantapi.photos.common.AbstractRestClient;
import com.mantzavelas.tripassistantapi.photos.common.IntervalType;
import com.mantzavelas.tripassistantapi.photos.common.RestCall;
import com.mantzavelas.tripassistantapi.utils.DateUtils;
import com.mantzavelas.tripassistantapi.utils.PropertyUtil;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FlickrRestClient extends AbstractRestClient {

    private static final String apiKey = PropertyUtil.getProperty("apiClient.flickr.apiKey");
    private static final String secret = PropertyUtil.getProperty("apiClient.flickr.secret");

    private static FlickrRestClient instance;
    public static FlickrRestClient create() {
        if (instance == null) {
            instance = new FlickrRestClient();
        }

        return instance;
    }

    private RestCall<Void, FlickrSearchResponse> searchPhotos = new RestCall<>(HttpMethod.GET, null, FlickrSearchResponse.class);
    private RestCall<Void, FlickrPhotoInfoResponse> getPhotoInfo = new RestCall<>(HttpMethod.GET, null, FlickrPhotoInfoResponse.class);

    public FlickrSearchResponse searchPhotos() {
        return searchPhotos(null);
    }

    public FlickrSearchResponse searchPhotos(String page) {
        return searchPhotosFrom(null, page);
    }

    public FlickrSearchResponse searchPhotosFrom(Date startDate) {
        return searchPhotosFrom(startDate, null);
    }

    public FlickrSearchResponse searchPhotosFrom(Date startDate, String page) {
        FlickrUrlHelper url = new FlickrUrlHelper("flickr.photos.search")
                .withParam("lat", "40.618670")
                .withParam("lon", "22.973262")
                .withParam("has_geo", "1");

        Optional.ofNullable(page).ifPresent( s -> url.withParam("page",page));

        if (Optional.ofNullable(startDate).isPresent()) {
            url.withParam("min_upload_date", DateUtils.dateToUnixTimestamp(startDate));
        } else {
            url.withParam("min_upload_date", "1449867647");
        }

        searchPhotos.setUrl(url.build());

        return call(searchPhotos.getUrl(), searchPhotos.getHttpMethod(), null, searchPhotos.getClazz());

    }

    public FlickrPhotoInfoResponse getPhotoInfo(String photoId) {
        String finalUrl = new FlickrUrlHelper("flickr.photos.getInfo")
            .withParam("photo_id", photoId)
            .withParam("secret",secret)
            .build();

        getPhotoInfo.setUrl(finalUrl);

        return call(getPhotoInfo.getUrl(), getPhotoInfo.getHttpMethod(), null, getPhotoInfo.getClazz());
    }

    @Override
    public String baseUrl() {
        return "https://api.flickr.com/services/rest";
    }

    @Override
    public IntervalType interval() {
        return IntervalType.HOURLY;
    }

    @Override
    public int callsPerInterval() {
        return 3600;
    }

    class FlickrUrlHelper {
        private Map<String, String> params;
        private UriComponentsBuilder paramBuilder;

        //Need to add standard params needed in flickr calls
        FlickrUrlHelper(String flickrMethod) {
            params = new HashMap<>();
            paramBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl());

            params.put("format","json");
            params.put("nojsoncallback", "1");
            params.put("method", flickrMethod);
            params.put("api_key", apiKey);
        }

        FlickrUrlHelper withParam(String key, String value) {
            params.put(key, value);

            return this;
        }

        String build() {
            params.forEach((key,value) -> paramBuilder.queryParam(key, value));

            return paramBuilder.toUriString();
        }
    }

}
