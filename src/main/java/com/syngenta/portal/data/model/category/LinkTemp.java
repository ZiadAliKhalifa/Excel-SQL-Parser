package com.syngenta.portal.data.model.category;

public class LinkTemp {
    private int rowNum;
    String urlOwner;
    String name;
    String url;
    String category;
    String openInIframe;

    public String getUrlOwner() {
        return urlOwner;
    }

    public void setUrlOwner(String urlOwner) {
        this.urlOwner = urlOwner;
    }

    public LinkTemp(int rowNum) {
        this.rowNum = rowNum;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
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

    public String getOpenInIframe() {
        return openInIframe;
    }

    public void setOpenInIframe(String openInIframe) {
        this.openInIframe = openInIframe;
    }


}
