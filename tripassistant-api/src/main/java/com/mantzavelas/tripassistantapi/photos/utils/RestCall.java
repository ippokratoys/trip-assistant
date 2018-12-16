package com.mantzavelas.tripassistantapi.photos.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RestCall<T,R> {

    private static RestTemplate restTemplate = new RestTemplate();

    private String url;

    private HttpMethod httpMethod;

    private HttpEntity<T> requestEntity;

    private Class<R> clazz;

    public RestCall(HttpMethod httpMethod, HttpEntity<T> requestEntity, Class<R> clazz) {
        this.httpMethod = httpMethod;
        this.requestEntity = requestEntity;
        this.clazz = clazz;
    }

    public RestCall(String url, HttpMethod httpMethod, HttpEntity<T> requestEntity, Class<R> clazz) {
        this.url = url;
        this.httpMethod = httpMethod;
        this.requestEntity = requestEntity;
        this.clazz = clazz;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ResponseEntity<R> call() {
        return restTemplate.exchange(url, httpMethod, requestEntity, clazz);
    }

}
