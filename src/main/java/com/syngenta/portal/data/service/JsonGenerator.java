package com.syngenta.portal.data.service;


import com.syngenta.portal.data.model.Link;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;


public class JsonGenerator {

    public static JSONObject createTags(String tagsStr) {
        JSONObject tags = new JSONObject();
        if (tagsStr != null && tagsStr.length() > 0) {
            tags.put("keywords", String.join(",", tagsStr));
        } else {
            tags.put("keywords", null);
        }
        return tags;
    }

    public static JSONArray createLinks(List<Link> cgLinks) {
        JSONArray links = new JSONArray();
        if (cgLinks != null && cgLinks.size() > 0) {
            for (Link link : cgLinks) {
                JSONObject linkObj = new JSONObject();
                linkObj.put("category", link.getCategory());
                linkObj.put("name", link.getName());
                linkObj.put("url", link.getUrl());
                linkObj.put("openInIframe", link.getOpenInIframe());
                links.add(linkObj);
            }

        }
        return links;
    }

    public void addAuditValues(JSONObject jsonObj) {
        jsonObj.put("createdBy", "system");
        jsonObj.put("lastModifiedBy", "system");
        jsonObj.put("creationDate", "2012-04-23T18:25:43.511Z");
        jsonObj.put("lastModificationDate", "2012-04-23T18:25:43.511Z");
    }

    public void addStatusValue(JSONObject jsonObj) {
        jsonObj.put("active", true);
    }

}
