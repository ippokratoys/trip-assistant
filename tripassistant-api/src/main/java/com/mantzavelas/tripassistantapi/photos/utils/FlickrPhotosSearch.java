package com.mantzavelas.tripassistantapi.photos.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FlickrPhotosSearch {

    private int page;

    private int pages;

    @JsonProperty("perpage")
    private int perPage;

    private String total;

    private List<FlickrPhotoSearch> photo;

    public FlickrPhotosSearch() {
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<FlickrPhotoSearch> getPhoto() {
        return photo;
    }

    public void setPhoto(List<FlickrPhotoSearch> photo) {
        this.photo = photo;
    }
}
