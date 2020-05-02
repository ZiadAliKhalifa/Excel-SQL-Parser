package com.syngenta.portal.data.service.shortcut;


import com.syngenta.portal.data.model.Constants;
import com.syngenta.portal.data.model.DataSheet;
import com.syngenta.portal.data.model.category.CategoryDataSheet;
import com.syngenta.portal.data.model.shortcut.Shortcut;
import com.syngenta.portal.data.model.shortcut.ShortcutDataSheet;
import com.syngenta.portal.data.model.shortcut.ShortcutEnviromentLinksDataSheet;
import com.syngenta.portal.data.model.shortcut.ShortcutLinksDataSheet;
import com.syngenta.portal.data.service.JsonGenerator;
import com.syngenta.portal.data.service.SheetScriptGenerator;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
public class ShortcutScriptGenerator extends JsonGenerator implements SheetScriptGenerator<ShortcutDataSheet> {
    private static final String INSERT_SHORTCUT =
            "INSERT INTO shortcut (ID,DATA,active,creation_date,created_by,last_modification_date,last_modified_by) "
                    + "VALUES ('%s','%s',true,now(),'"+ Constants.SYSTEM +"',now(),'"+ Constants.SYSTEM +"');";


    @Override
    public List<String> generate(ShortcutDataSheet dataSheet, DataSheet... referenceSheets) {
        List<String> insertStatements = new ArrayList<>();
        CategoryDataSheet categoryDataSheet = (CategoryDataSheet) referenceSheets[0];
        ShortcutLinksDataSheet shortcutLinksDataSheet = (ShortcutLinksDataSheet) referenceSheets[1];
        ShortcutEnviromentLinksDataSheet shortcutEnviromentLinksDataSheet = (ShortcutEnviromentLinksDataSheet) referenceSheets[2];
        for (Shortcut cg : dataSheet.getShortcuts()) {

            cg.setId(UUID.randomUUID().toString());
            if(shortcutLinksDataSheet.getShortcutLinksMap().get(cg.getName().toUpperCase().trim())!=null) {
                cg.setLinks(shortcutLinksDataSheet.getShortcutLinksMap().get(cg.getName().toUpperCase().trim()));
            }
            if(shortcutEnviromentLinksDataSheet.getShortcutLinksMap().get(cg.getName().toUpperCase().trim())!=null) {
                cg.setEnviromentLinks(shortcutEnviromentLinksDataSheet.getShortcutLinksMap().get(cg.getName().toUpperCase().trim()));
            }
            insertStatements.add("-- Create New Shortcut " + cg.getName());

            String stmt = String.format(INSERT_SHORTCUT, cg.getId(), getData(cg, categoryDataSheet.getCategoryUUIDMap()));
            insertStatements.add(stmt);

            //update Sheets and maps
            dataSheet.getShortcutUUIDMap().put(cg.getName().toUpperCase().trim(), cg.getId());

        }
        return insertStatements;
    }

    public static String getData(Shortcut cg, Map<String, String> categoryUUIDMap) {
        JSONObject sampleObject = new JSONObject();
        sampleObject.put("name",  SheetScriptGenerator.formatForSql(cg.getName()));
        sampleObject.put("description",  SheetScriptGenerator.formatForSql(cg.getDescription()));
        sampleObject.put("categoryId", categoryUUIDMap.get(cg.getCategory().trim().toUpperCase()));
        sampleObject.put("imageUrl", SheetScriptGenerator.trim(cg.getImageURL()));
        sampleObject.put("defaultEnvironment", SheetScriptGenerator.trim(cg.getDefaultEnviroment()));
        sampleObject.put("accessInformation", SheetScriptGenerator.formatForSql(cg.getAccessInformation()));
        sampleObject.put("environmentLinks", createLinks(cg.getEnviromentLinks() ));
        sampleObject.put("tags", createTags(cg.getTags()));
        sampleObject.put("links", createLinks(cg.getLinks()));
        sampleObject.put("systemShortcut", true);
        if(cg.getName().toLowerCase().contains("gateway")||cg.getName().toLowerCase().contains("gateway")) {
            sampleObject.put("adminShortcut", true);
        }else{
            sampleObject.put("adminShortcut", false);
        }
        return sampleObject.toJSONString();
    }

}


