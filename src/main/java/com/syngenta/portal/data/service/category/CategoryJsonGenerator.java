package com.syngenta.portal.data.service.category;


import com.syngenta.portal.data.model.Constants;
import com.syngenta.portal.data.model.DataSheet;
import com.syngenta.portal.data.model.Link;
import com.syngenta.portal.data.model.category.Category;
import com.syngenta.portal.data.model.category.CategoryDataSheet;
import com.syngenta.portal.data.model.category.CategoryLinksDataSheet;
import com.syngenta.portal.data.service.SheetScriptGenerator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class CategoryJsonGenerator {

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
                linkObj.put("category", SheetScriptGenerator.trim(link.getCategory()));
                linkObj.put("name", SheetScriptGenerator.trim(link.getName()));
                linkObj.put("url", SheetScriptGenerator.trim(link.getUrl()));
                linkObj.put("openInIframe", link.getOpenInIframe());
                links.add(linkObj);
            }

        }
        return links;
    }

}
