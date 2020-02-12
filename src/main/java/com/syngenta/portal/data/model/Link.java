package com.syngenta.portal.data.model;

public class Link {
    String name;
    String url;
    String category;
    boolean openInIframe;

    public boolean getOpenInIframe() {
        return openInIframe;
    }

    public void setOpenInIframe(boolean openInIframe) {
        this.openInIframe = openInIframe;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    @Override
    public String toString() {
        return this.name + " " + this.url + " " + this.category + " " + this.openInIframe;
    }
}
