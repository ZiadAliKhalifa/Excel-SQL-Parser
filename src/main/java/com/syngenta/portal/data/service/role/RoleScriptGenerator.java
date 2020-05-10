package com.syngenta.portal.data.service.role;


import com.syngenta.portal.data.model.Constants;
import com.syngenta.portal.data.model.DataSheet;
import com.syngenta.portal.data.model.role.Role;
import com.syngenta.portal.data.model.role.RoleDataSheet;
import com.syngenta.portal.data.model.shortcut.ShortcutDataSheet;
import com.syngenta.portal.data.service.SheetScriptGenerator;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.syngenta.portal.data.service.JsonGenerator.createTags;
import static com.syngenta.portal.data.service.role.RoleJsonGenerator.createShortcuts;
import static com.syngenta.portal.data.service.role.RoleJsonGenerator.createShortcutsObj;


@Service
public class RoleScriptGenerator implements SheetScriptGenerator<RoleDataSheet> {
    private static final String INSERT_ROLE =
            "INSERT INTO \"role\" (ID,DATA,active,creation_date,created_by,last_modification_date,last_modified_by) "
                    + "VALUES ('%s','%s',true,now() AT TIME ZONE 'UTC','" + Constants.SYSTEM + "',now() AT TIME ZONE 'UTC','" + Constants.SYSTEM + "');";


    @Override
    public List<String> generate(RoleDataSheet dataSheet, DataSheet... referenceSheets) {
        List<String> insertStatements = new ArrayList<>();
        ShortcutDataSheet shortcutDataSheet = (ShortcutDataSheet) referenceSheets[0];
        for (Role cg : dataSheet.getRoles()) {
            insertStatements.add("-- Create New Role " + cg.getName());
            cg.setId(UUID.randomUUID().toString());
            String stmt = String.format(INSERT_ROLE, cg.getId(), getData(cg, shortcutDataSheet));
            insertStatements.add(stmt);

            //update Sheets and maps
            dataSheet.getRoleUUIDMap().put(cg.getName().trim().toUpperCase(), cg.getId());

        }
        return insertStatements;
    }


    public String getData(Role cg, ShortcutDataSheet shortcutDataSheet) {
        JSONObject sampleObject = new JSONObject();
        sampleObject.put("name", SheetScriptGenerator.formatForSql(cg.getName()));
        sampleObject.put("description", SheetScriptGenerator.formatForSql(cg.getDescription()));
        sampleObject.put("tags", createTags( cg.getTags()));
        List<String> shortcuts = shortcutDataSheet.getRolesMap().get(cg.getName().trim().toUpperCase());
       // sampleObject.put("shortcutIds", createShortcuts(shortcuts, shortcutDataSheet.getShortcutUUIDMap()));
        sampleObject.put("shortcuts", createShortcutsObj(shortcuts, shortcutDataSheet.getShortcutUUIDMap()));

        return sampleObject.toJSONString();
    }

}
