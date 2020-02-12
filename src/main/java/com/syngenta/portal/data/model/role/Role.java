package com.syngenta.portal.data.model.role;

import java.util.List;

public class Role {
    private String id;
    private String name;
    private String description;
    private List<String> shortcuts;
    private String tags;

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

    public List<String> getShortcuts() {
        return shortcuts;
    }

    public void setShortcuts(List<String> shortcuts) {
        this.shortcuts = shortcuts;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {

        return "Role-->"+" "+this.name+" ";
    }
}
