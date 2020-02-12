package com.syngenta.portal.data.service.role;


import com.syngenta.portal.data.model.DataSheet;
import com.syngenta.portal.data.model.role.Role;
import com.syngenta.portal.data.model.role.RoleDataSheet;
import com.syngenta.portal.data.model.shortcut.ShortcutDataSheet;
import com.syngenta.portal.data.service.JsonGenerator;
import com.syngenta.portal.data.service.SheetScriptGenerator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.syngenta.portal.data.service.role.RoleJsonGenerator.createShortcuts;


@Service
public class RoleFlatObjectGenerator extends JsonGenerator implements SheetScriptGenerator<RoleDataSheet> {
    @Override
    public List<String> generate(RoleDataSheet dataSheet, DataSheet... referenceSheets) {
        JSONArray roles = new JSONArray();
        ShortcutDataSheet shortcutDataSheet = (ShortcutDataSheet) referenceSheets[0];
        for (Role cg : dataSheet.getRoles()) {
            roles.add(getData(cg, shortcutDataSheet));
        }
        List<String> data = new ArrayList<String>();
        data.add("//const roles=");
        data.add(roles.toJSONString());
        data.add("//;");
        return data;
    }


    public JSONObject getData(Role cg, ShortcutDataSheet shortcutDataSheet) {
        JSONObject sampleObject = new JSONObject();
        sampleObject.put("id", cg.getId());
        sampleObject.put("name", SheetScriptGenerator.trim(cg.getName()));
        sampleObject.put("description", SheetScriptGenerator.trim(cg.getDescription()));
        sampleObject.put("tags", createTags(cg.getTags()));

        List<String> shortcuts = shortcutDataSheet.getRolesMap().get(cg.getName().trim().toUpperCase());
        sampleObject.put("shortcutIds", createShortcuts(shortcuts, shortcutDataSheet.getShortcutUUIDMap()));
        addAuditValues(sampleObject);
        addStatusValue(sampleObject);


        return sampleObject;
    }

}
