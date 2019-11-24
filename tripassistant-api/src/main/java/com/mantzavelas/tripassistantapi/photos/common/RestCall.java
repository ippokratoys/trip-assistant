package com.mantzavelas.tripassistantapi.photos.common;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

public class RestCall<T,R> {

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

    public String getUrl() { return url; }
    public void setUrl(String url) {
        this.url = url;
    }

    public HttpMethod getHttpMethod() { return httpMethod; }
    public void setHttpMethod(HttpMethod httpMethod) { this.httpMethod = httpMethod; }

    public HttpEntity<T> getRequestEntity() { return requestEntity; }
    public void setRequestEntity(HttpEntity<T> requestEntity) { this.requestEntity = requestEntity; }

    public Class<R> getClazz() { return clazz; }
    public void setClazz(Class<R> clazz) { this.clazz = clazz; }
}
