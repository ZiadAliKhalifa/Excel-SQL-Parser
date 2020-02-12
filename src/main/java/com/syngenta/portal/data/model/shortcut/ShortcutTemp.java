package com.syngenta.portal.data.model.shortcut;

import com.syngenta.portal.data.model.Link;

import java.util.List;

public class ShortcutTemp {
    private int rowNum;
    private String name;
    private String description;
    private String tags;
    private String category;
    private String imageURL;
    private String defaultEnviroment;
    private String roles;
    private String accessInformation;

    public ShortcutTemp(int rowNum) {
        this.rowNum = rowNum;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
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

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public String getAccessInformation() {
        return accessInformation;
    }

    public void setAccessInformation(String accessInformation) {
        this.accessInformation = accessInformation;
    }
    @Override
    public String toString() {

        return "Shortcut-->" + " " + this.name + " ";
    }
}
