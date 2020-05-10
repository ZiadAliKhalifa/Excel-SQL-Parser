package com.syngenta.portal.data.service.workspace;


import com.syngenta.portal.data.model.DataSheet;
import com.syngenta.portal.data.model.shortcut.ShortcutDataSheet;
import com.syngenta.portal.data.model.user.UserDataSheet;
import com.syngenta.portal.data.model.workspace.Workspace;
import com.syngenta.portal.data.model.workspace.WorkspaceDataSheet;
import com.syngenta.portal.data.service.SheetScriptGenerator;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.syngenta.portal.data.service.workspace.WorkspaceJsonGenerator.createWorkspaceShortcuts;


@Service
public class WorkspaceScriptGenerator implements SheetScriptGenerator<WorkspaceDataSheet> {
    private static final String INSERT_WORKSPACE =
            "INSERT INTO workspace (ID,DATA,active,creation_date,created_by,last_modification_date,last_modified_by) "
                    + "VALUES ('%s','%s',true,now() AT TIME ZONE 'UTC','%s',now() AT TIME ZONE 'UTC','%s');";
    private static final String UPDATE_USER_WORKSPACE =
            "UPDATE \"user\" SET data = jsonb_set(data, '{workspaceIds,99999}', '\"%s\"', TRUE) WHERE id = '%s';";


    @Override
    public List<String> generate(WorkspaceDataSheet dataSheet, DataSheet... referenceSheets) {
        List<String> insertStatements = new ArrayList<>();
        UserDataSheet userDataSheet = (UserDataSheet) referenceSheets[0];
        ShortcutDataSheet shortcutDataSheet = (ShortcutDataSheet) referenceSheets[1];
        for (Workspace ws : dataSheet.getWorkspaces()) {
            for (String taccount : ws.getTaccount()) {
                ws.setId(UUID.randomUUID().toString());
                String userId = userDataSheet.getUserUUIDMap().get(taccount.trim().toUpperCase());
                insertStatements.add("-- Create New Workspace " + ws.getName());
                String stmt = String.format(INSERT_WORKSPACE, ws.getId(), getData(ws, shortcutDataSheet.getShortcutUUIDMap()), userId, userId);

                insertStatements.add(stmt);
                insertStatements.add("-- Associate workspace to user " + ws.getName());
                String updateUserWkStat = String.format(UPDATE_USER_WORKSPACE, ws.getId(), userId);
                insertStatements.add(updateUserWkStat);
                //update Sheets and maps
                dataSheet.getWorkspaceUUIDMap().put(ws.getName().toUpperCase().trim(), ws.getId());
            }
        }
        return insertStatements;
    }

    public static String getData(Workspace cg, Map<String, String> shortcutUUIDMap) {
        JSONObject sampleObject = new JSONObject();
        sampleObject.put("name", SheetScriptGenerator.formatForSql(cg.getName()));
        sampleObject.put("roleId", null);
        sampleObject.put("shortcuts", createWorkspaceShortcuts(cg.getShortcuts(),shortcutUUIDMap));

        return sampleObject.toJSONString();
    }

}
