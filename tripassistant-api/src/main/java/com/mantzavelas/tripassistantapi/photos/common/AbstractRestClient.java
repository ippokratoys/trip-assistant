package com.mantzavelas.tripassistantapi.photos.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

public abstract class AbstractRestClient implements RestClient{

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRestClient.class);

    private RestTemplate restTemplate = new RestTemplate();
    private RestCaller restCaller = new RestCaller();

    private int totalCalls;

	public abstract String baseUrl();

    private boolean canCall() {
        return IntervalType.UNLIMITED.equals(interval()) || totalCalls <= callsPerInterval();
    }

    private void checkCallAbility() {
        try {
            if (canCall()) {
                totalCalls++;
                //always leave 1 sec between each call. some APIs consider sequential calls as abuse
                Thread.sleep(1000);
            } else {
                totalCalls = 0;
                Thread.sleep(interval().getValue() * 1000L);
            }
        } catch (InterruptedException e) {
            LOGGER.error("Interrupted!", e);
            Thread.currentThread().interrupt();
        }
    }

    protected <T>T call(String url, HttpMethod method, HttpEntity entity, Class<T> clazz){
        checkCallAbility();
        return restCaller.call(url, method, entity, clazz).getBody();
    }

    protected <T>T getForObject(URI uri, Class<T> clazz) {
        checkCallAbility();
        return restTemplate.getForObject(uri, clazz);
    }
}
