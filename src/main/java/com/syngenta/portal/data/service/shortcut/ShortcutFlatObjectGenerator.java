package com.syngenta.portal.data.service.shortcut;


import com.syngenta.portal.data.model.DataSheet;
import com.syngenta.portal.data.model.Link;
import com.syngenta.portal.data.model.category.CategoryDataSheet;
import com.syngenta.portal.data.model.shortcut.Shortcut;
import com.syngenta.portal.data.model.shortcut.ShortcutDataSheet;
import com.syngenta.portal.data.model.shortcut.ShortcutEnviromentLinksDataSheet;
import com.syngenta.portal.data.model.shortcut.ShortcutLinksDataSheet;
import com.syngenta.portal.data.service.JsonGenerator;
import com.syngenta.portal.data.service.SheetScriptGenerator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
public class ShortcutFlatObjectGenerator extends JsonGenerator implements SheetScriptGenerator<ShortcutDataSheet> {

    @Override
    public List<String> generate(ShortcutDataSheet dataSheet, DataSheet... referenceSheets) {
        JSONArray shortcuts = new JSONArray();
        CategoryDataSheet categoryDataSheet = (CategoryDataSheet) referenceSheets[0];
        ShortcutLinksDataSheet shortcutLinksDataSheet = (ShortcutLinksDataSheet) referenceSheets[1];
        ShortcutEnviromentLinksDataSheet enviromentLinksDataSheet = (ShortcutEnviromentLinksDataSheet) referenceSheets[2];
        for (Shortcut cg : dataSheet.getShortcuts()) {

            cg.setId(UUID.randomUUID().toString());
            shortcuts.add(getData(cg, categoryDataSheet.getCategoryUUIDMap(), shortcutLinksDataSheet, enviromentLinksDataSheet));
        }
        List<String> data = new ArrayList<String>();
        data.add("//const shortcuts=");
        data.add(shortcuts.toJSONString());
        data.add("//;");
        return data;
    }

    public JSONObject getData(Shortcut cg, Map<String, String> categoryUUIDMap, ShortcutLinksDataSheet shortcutLinksDataSheet, ShortcutEnviromentLinksDataSheet enviromentLinksDataSheet) {
        JSONObject sampleObject = new JSONObject();
        sampleObject.put("id", cg.getId());
        sampleObject.put("name", SheetScriptGenerator.trim(cg.getName()));
        sampleObject.put("description", SheetScriptGenerator.trim(cg.getDescription()));
        sampleObject.put("categoryId", categoryUUIDMap.get(cg.getCategory().trim()));
        sampleObject.put("imageUrl", SheetScriptGenerator.trim(cg.getImageURL()));
        sampleObject.put("defaultEnvironment", SheetScriptGenerator.trim(cg.getDefaultEnviroment()));
        sampleObject.put("accessInformation", SheetScriptGenerator.trim(cg.getAccessInformation()));
        sampleObject.put("tags", createTags(cg.getTags()));
        sampleObject.put("environmentLinks", createLinks(cg.getEnviromentLinks() ));
        sampleObject.put("links", createLinks(cg.getLinks()));
        addAuditValues(sampleObject);
        addStatusValue(sampleObject);
        return sampleObject;
    }
}
