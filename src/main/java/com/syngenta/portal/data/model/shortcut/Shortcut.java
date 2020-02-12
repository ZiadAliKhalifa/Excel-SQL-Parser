package com.syngenta.portal.data.model.shortcut;

import com.syngenta.portal.data.model.Link;

import java.util.List;

public class Shortcut {
    private String id;
    private String name;
    private String description;
    private String tags;
    private List<Link> enviromentLinks;
    private String category;
    private List<Link> links;
    private String imageURL;
    private String defaultEnviroment;
    private String accessInformation;

    public String getId() {
        return id;
    }

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getDefaultEnviroment() {
        return defaultEnviroment;
    }

    public void setDefaultEnviroment(String defaultEnviroment) {
        this.defaultEnviroment = defaultEnviroment;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


    public List<Link> getEnviromentLinks() {
        return enviromentLinks;
    }

    public void setEnviromentLinks(List<Link> enviromentLinks) {
        this.enviromentLinks = enviromentLinks;
    }

    public String getAccessInformation() {
        return accessInformation;
    }


    @Override
    public String toString() {

        return "Shortcut-->" + " " + this.name + " ";
    }
}
