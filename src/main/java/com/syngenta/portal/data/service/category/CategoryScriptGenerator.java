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

import static com.syngenta.portal.data.service.category.CategoryJsonGenerator.createLinks;


@Service
public class CategoryScriptGenerator implements SheetScriptGenerator<CategoryDataSheet> {
    private static final String INSERT_CATEGORY =
            "INSERT INTO reference_data (ID,TYPE,DATA,active,creation_date,created_by,last_modification_date,last_modified_by) "
                    + "VALUES ('%s','CATEGORY','%s',true,now() AT TIME ZONE 'UTC','"+ Constants.SYSTEM +"',now() AT TIME ZONE 'UTC','"+ Constants.SYSTEM +"');";


    @Override
    public List<String> generate(CategoryDataSheet dataSheet, DataSheet... referenceSheets) {
        List<String> insertStatements = new ArrayList<>();
        CategoryLinksDataSheet categoryLinks = (CategoryLinksDataSheet) referenceSheets[0];
        for (Category cg : dataSheet.getCategories()) {

            cg.setId(UUID.randomUUID().toString());

            insertStatements.add("-- Create New Shortcut " + cg.getName());
            String stmt = String.format(INSERT_CATEGORY, cg.getId(), getData(cg, categoryLinks));
            insertStatements.add(stmt);

            //update Sheets and maps
            dataSheet.getCategoryUUIDMap().put(cg.getName().trim().toUpperCase(), cg.getId());

        }
        return insertStatements;
    }

    public static String getData(Category cg, CategoryLinksDataSheet categoryLinks) {
        JSONObject sampleObject = new JSONObject();
        sampleObject.put("name", SheetScriptGenerator.formatForSql(cg.getName()));
        sampleObject.put("description",  SheetScriptGenerator.formatForSql(cg.getDescription()));
        sampleObject.put("color",SheetScriptGenerator.trim(cg.getColor()));
        sampleObject.put("imageUrl", SheetScriptGenerator.trim(cg.getImageURL()));

        JSONObject tags = new JSONObject();
        if (cg.getTags() != null && cg.getTags().length() > 0) {
            tags.put("keywords", String.join(",", cg.getTags()));
        } else {
            tags.put("keywords", null);
        }
        sampleObject.put("tags", tags);


        List<Link> cgLinks = categoryLinks.getCategoryLinksMap().get(cg.getName().toUpperCase().trim());

        sampleObject.put("links", createLinks(cgLinks));

        return sampleObject.toJSONString();
    }

}
