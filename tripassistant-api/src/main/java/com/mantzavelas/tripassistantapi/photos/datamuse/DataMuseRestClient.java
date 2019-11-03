package com.mantzavelas.tripassistantapi.photos.datamuse;

import com.mantzavelas.tripassistantapi.photos.common.AbstractRestClient;
import com.mantzavelas.tripassistantapi.photos.common.IntervalType;
import com.mantzavelas.tripassistantapi.photos.common.RestCall;
import org.springframework.http.HttpMethod;

import java.util.Arrays;
import java.util.List;

public class DataMuseRestClient extends AbstractRestClient {

    private RestCall<Void, RelativeWord[]> RELATIVE_WORDS = new RestCall<>(HttpMethod.GET, null, RelativeWord[].class);

    private static DataMuseRestClient instance;
     public static DataMuseRestClient create() {
        if (instance == null) {
            instance = new DataMuseRestClient();
        }

        return instance;
    }

    public List<RelativeWord> getRelativeWords(String word) {
        String urlBuilder = baseUrl() + "words?" +
            "ml=" + word;

        RELATIVE_WORDS.setUrl(urlBuilder);

        return Arrays.asList(call(RELATIVE_WORDS.getUrl(), RELATIVE_WORDS.getHttpMethod(), null, RELATIVE_WORDS.getClazz()));
    }

    @Override
    public String baseUrl() {
        return "https://api.datamuse.com/";
    }

    @Override
    public IntervalType interval() {
        return IntervalType.UNLIMITED;
    }

    @Override
    public int callsPerInterval() {
        return 0;
    }
}
