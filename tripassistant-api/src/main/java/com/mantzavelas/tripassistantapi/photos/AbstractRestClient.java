package com.mantzavelas.tripassistantapi.photos;

import com.mantzavelas.tripassistantapi.photos.utils.RestCaller;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

public abstract class AbstractRestClient implements RestClient{

    private RestTemplate restTemplate = new RestTemplate();
    private RestCaller restCaller = new RestCaller();

    public int totalCalls;

    public abstract String baseUrl();

    public boolean canCall() {
        return IntervalType.UNLIMITED.equals(interval()) || totalCalls <= callsPerInterval();
    }

    public void checkCallAbility() {
        try {
            if (canCall()) {
                totalCalls++;
                //always leave 1 sec between each call. some APIs consider sequential calls as abuse
                Thread.sleep(1000);
            } else {
                totalCalls = 0;
                Thread.sleep(interval().getValue() * 1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public <T>T call(String url, HttpMethod method, HttpEntity entity, Class<T> clazz){
        checkCallAbility();
        return restCaller.call(url, method, entity, clazz).getBody();
    }

    public <T>T getForObject(URI uri, Class<T> clazz) {
        checkCallAbility();
        return restTemplate.getForObject(uri, clazz);
    }
}
