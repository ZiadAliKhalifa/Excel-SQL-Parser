package com.syngenta.portal.data.model.user;

import com.syngenta.portal.data.model.Link;

import java.util.List;

public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String tags;
    private String region;
    private String businessUnit;
    private List<String> roles;
    private List<String> workspaces;
    private String taccount;
    private String imageURL;
    private String lastAccessedWorkspaceId;
    private String lastUsedWorkspaceViewType;

    public String getTaccount() {
        return taccount;
    }

    public void setTaccount(String taccount) {
        this.taccount = taccount;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getLastAccessedWorkspaceId() {
        return lastAccessedWorkspaceId;
    }

    public void setLastAccessedWorkspaceId(String lastAccessedWorkspaceId) {
        this.lastAccessedWorkspaceId = lastAccessedWorkspaceId;
    }

    public String getLastUsedWorkspaceViewType() {
        return lastUsedWorkspaceViewType;
    }

    public void setLastUsedWorkspaceViewType(String lastUsedWorkspaceViewType) {
        this.lastUsedWorkspaceViewType = lastUsedWorkspaceViewType;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getWorkspaces() {
        return workspaces;
    }

    public void setWorkspaces(List<String> workspaces) {
        this.workspaces = workspaces;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    @Override
    public String toString() {

        return "User-->" + " " + this.firstName + " " + this.lastName;
    }
}
