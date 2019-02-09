package com.mantzavelas.tripassistantapi.models;

import javax.persistence.*;
import java.util.List;

@Entity
public class PhotoCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PhotoCategoryEnum category;

    @ElementCollection( fetch = FetchType.EAGER)
    private List<String> tags;

    @ManyToMany(mappedBy = "categories")
    private List<Photo> photos;

    public PhotoCategory() {
    }

    public Long getId() { return id; }

    public PhotoCategoryEnum getCategory() { return category; }
    public void setCategory(PhotoCategoryEnum category) { this.category = category; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public List<Photo> getPhotos() { return photos; }
    public void setPhotos(List<Photo> photos) { this.photos = photos; }
}
