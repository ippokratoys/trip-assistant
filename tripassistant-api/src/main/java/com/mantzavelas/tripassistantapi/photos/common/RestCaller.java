package com.mantzavelas.tripassistantapi.photos.common;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

public class RestCaller {

    private RestTemplate restTemplate;

    public RestCaller() { restTemplate = new RestTemplate(); }

    public RestCaller(RestTemplate restTemplate) { this.restTemplate = restTemplate; }

    public <T>ResponseEntity<T> call(String url, HttpMethod method, HttpEntity entity, Class<T> clazz) {
        return restTemplate.exchange(url, method, entity, clazz);
    }

    public <T>ResponseEntity<T> call(String url, HttpMethod method, HttpEntity entity, Class<T> clazz, Map<String,?> uriVariables) {
        try {
            restTemplate.setDefaultUriVariables(uriVariables);
            return restTemplate.exchange(url, method, entity, clazz);
        } finally {
            restTemplate.setDefaultUriVariables(Collections.emptyMap());
        }
    }

}
