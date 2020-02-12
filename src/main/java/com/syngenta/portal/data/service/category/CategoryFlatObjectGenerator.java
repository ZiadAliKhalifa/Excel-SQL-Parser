package com.syngenta.portal.data.service.category;


import com.syngenta.portal.data.model.DataSheet;
import com.syngenta.portal.data.model.Link;
import com.syngenta.portal.data.model.category.Category;
import com.syngenta.portal.data.model.category.CategoryDataSheet;
import com.syngenta.portal.data.model.category.CategoryLinksDataSheet;
import com.syngenta.portal.data.service.JsonGenerator;
import com.syngenta.portal.data.service.SheetScriptGenerator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class CategoryFlatObjectGenerator extends JsonGenerator implements SheetScriptGenerator<CategoryDataSheet> {

    @Override
    public List<String> generate(CategoryDataSheet dataSheet, DataSheet... referenceSheets) {

        JSONArray categories = new JSONArray();
        CategoryLinksDataSheet categoryLinks = (CategoryLinksDataSheet) referenceSheets[0];
        for (Category cg : dataSheet.getCategories()) {
            cg.setId(UUID.randomUUID().toString());
            categories.add(getData(cg, categoryLinks));
        }
        List<String> data = new ArrayList<String>();
        data.add("//const referanceData=");
        data.add(categories.toJSONString());
        data.add("//;");
        return data;
    }

    public JSONObject getData(Category cg, CategoryLinksDataSheet categoryLinks) {
        JSONObject sampleObject = new JSONObject();
        sampleObject.put("id", cg.getId());
        sampleObject.put("type", "CATEGORY");
        sampleObject.put("name", SheetScriptGenerator.trim(cg.getName()));
        sampleObject.put("description", SheetScriptGenerator.trim(cg.getDescription()));
        sampleObject.put("color", SheetScriptGenerator.trim(cg.getColor()));
        sampleObject.put("imageUrl", SheetScriptGenerator.trim(cg.getImageURL()));
        sampleObject.put("tags", createTags(cg.getTags()));
        List<Link> cgLinks = categoryLinks.getCategoryLinksMap().get(cg.getName().toUpperCase().trim());
        sampleObject.put("links", createLinks(cgLinks));
        addAuditValues(sampleObject);
        addStatusValue(sampleObject);


        return sampleObject;
    }

}
