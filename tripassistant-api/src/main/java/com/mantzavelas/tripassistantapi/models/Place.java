package com.mantzavelas.tripassistantapi.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "text")
    private String description;

    private String latitude;

    private String longitude;

    private int visits;

    @ManyToMany
    @JoinTable(
        name = "place_categories",
        joinColumns = @JoinColumn(name = "place_id"),
        inverseJoinColumns = @JoinColumn(name = "categories_id")
    )
    private List<PhotoCategory> categories;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> photoUrls;

    @Enumerated(EnumType.STRING)
    private City city;

    public Place() {
    }

    public Long getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLatitude() { return latitude; }
    public void setLatitude(String latitude) { this.latitude = latitude; }

    public String getLongitude() { return longitude; }
    public void setLongitude(String longitude) { this.longitude = longitude; }

    public int getVisits() { return visits; }
    public void setVisits(int visits) { this.visits = visits; }
    public void incrementVisits() { visits++; }

    public List<PhotoCategory> getCategories() { return categories; }
    public void setCategories(List<PhotoCategory> categories) { this.categories = categories; }

    public List<String> getPhotoUrls() { return photoUrls; }
    public void setPhotoUrls(List<String> photoUrls) { this.photoUrls = photoUrls; }
    public void addPhoto(String url) {
        if (photoUrls == null) {
            photoUrls = new ArrayList<>();
        }

        photoUrls.add(url);
    }

    public City getCity() { return city; }
    public void setCity(City city) { this.city = city; }
}
