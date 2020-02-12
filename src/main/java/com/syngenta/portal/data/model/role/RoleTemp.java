package com.syngenta.portal.data.model.role;

public class RoleTemp {

    private int rowNum;


    private String name;
    private String description;
    private String tags;

    public RoleTemp(int rowNum) {
        this.rowNum = rowNum;
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

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    @Override
    public String toString() {

        return "Role-->" + " " + this.name + " ";
    }
}
