package com.mantzavelas.tripassistantapi.photos.facebook;

import com.mantzavelas.tripassistantapi.photos.common.AbstractRestClient;
import com.mantzavelas.tripassistantapi.photos.common.IntervalType;
import com.mantzavelas.tripassistantapi.utils.PropertyUtil;
import org.springframework.web.util.UriComponentsBuilder;

public class FacebookRestClient extends AbstractRestClient {

	private static final String CLIENT_ID = PropertyUtil.getProperty("apiClient.facebook.clientId");
    private static final String APP_SECRET = PropertyUtil.getProperty("apiClient.facebook.secret");
    private static final String API_VERSION = "3.2";
	private static final int DEFAULT_DISTANCE = 50;

	private FacebookRestClient() {}

    private static FacebookRestClient instance;
    public static FacebookRestClient create() {
        if (instance == null) {
            instance = new FacebookRestClient();
        }

        return instance;
    }

    public FacebookSearchPlaceResponse getCurrentPlace(String latitude, String longitude) {
        String endpoint = baseUrl() + "/search?type=place";

        final String fields = "id,name,about,description,checkins,location,category_list";

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(endpoint)
            .queryParam("center", latitude + "," + longitude)
            .queryParam("fields", fields)
            .queryParam("distance", DEFAULT_DISTANCE)
            .queryParam("access_token", CLIENT_ID + "|" + APP_SECRET);

        return getForObject(uriBuilder.build().toUri(), FacebookSearchPlaceResponse.class);
    }

    @Override
    public IntervalType interval() {
        return IntervalType.HOURLY;
    }

    @Override
    public int callsPerInterval() {
        return 180;
    }

    @Override
    public String baseUrl() {
        return "https://graph.facebook.com/v" + API_VERSION;
    }

}
