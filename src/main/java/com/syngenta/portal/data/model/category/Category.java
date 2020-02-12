package com.syngenta.portal.data.model.category;

import com.syngenta.portal.data.model.Link;

import java.util.List;

public class Category {
    private String id;
    private String name;
    private String description;
    private String tags;
    private String imageURL;
    private List<Link> links;
    private String color;

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {

        return "Category-->"+" "+this.name+" ";
    }
}
