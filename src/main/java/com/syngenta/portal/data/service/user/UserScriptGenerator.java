package com.syngenta.portal.data.service.user;


import com.syngenta.portal.data.model.Constants;
import com.syngenta.portal.data.model.DataSheet;
import com.syngenta.portal.data.model.role.Role;
import com.syngenta.portal.data.model.role.RoleDataSheet;
import com.syngenta.portal.data.model.shortcut.ShortcutDataSheet;
import com.syngenta.portal.data.model.user.User;
import com.syngenta.portal.data.model.user.UserDataSheet;
import com.syngenta.portal.data.service.SheetScriptGenerator;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.syngenta.portal.data.service.user.UserJsonGenerator.*;


@Service
public class UserScriptGenerator implements SheetScriptGenerator<UserDataSheet> {
    private static final String INSERT_USER =
            "INSERT INTO \"user\" (ID,DATA,active,creation_date,created_by,last_modification_date,last_modified_by) "
                    + "VALUES ('%s','%s',true,now(),'"+ Constants.SYSTEM +"',now(),'"+ Constants.SYSTEM +"');";

    private static final String INSERT_WORKSPACE =
            "INSERT INTO workspace (ID,DATA,active,creation_date,created_by,last_modification_date,last_modified_by) "
                    + "VALUES ('%s','%s',true,now(),'"+ Constants.SYSTEM +"',now(),'"+ Constants.SYSTEM +"');";


    final static String FIRST_USER_UUID = "6ef1467e-7fe0-4c34-a5e9-5baded575c44";

    @Override
    public List<String> generate(UserDataSheet dataSheet, DataSheet... referenceSheets) {
        List<String> insertStatements = new ArrayList<>();
        int index = 0;
        RoleDataSheet roleDataSheet = (RoleDataSheet) referenceSheets[0];
        ShortcutDataSheet shortcutDataSheet = (ShortcutDataSheet) referenceSheets[1];
        for (User cg : dataSheet.getUsers()) {
            if (index == 0) {
                cg.setId(FIRST_USER_UUID);
            } else {
                cg.setId(UUID.randomUUID().toString());
            }
            insertStatements.add("-- Create New User " + cg.getFirstName() + " " + cg.getLastName());
            insertStatements.add("-- Create User Workspaces based on assigned roles");
            List<String> workspaces = new ArrayList<>();
            List<String> workspacesStatments = getWorkspaces(cg, roleDataSheet, workspaces, shortcutDataSheet);
            insertStatements.addAll(workspacesStatments);
            String stmt = String.format(INSERT_USER, cg.getId(), getData(cg, workspaces));

            insertStatements.add(stmt);
            dataSheet.getUserUUIDMap().put(cg.getTaccount().toUpperCase().trim(), cg.getId());
            index++;
        }
        return insertStatements;
    }

    private List<String> getWorkspaces(User cg, RoleDataSheet roleDataSheet, List<String> workspaces, ShortcutDataSheet shortcutDataSheet) {
        List<String> stmts = new ArrayList<>();
        for (String roleName : cg.getRoles()) {
            Role role = roleDataSheet.getRoles().stream()
                    .filter(r -> r.getName().trim().toUpperCase().equals(roleName.toUpperCase().trim()))
                    .findFirst()
                    .get();
            List<String> shortcuts = shortcutDataSheet.getRolesMap().get(role.getName().toUpperCase().trim());
            String id = UUID.randomUUID().toString();
            stmts.add(String.format(INSERT_WORKSPACE, id,
                    getWorkspaceData(role.getName(), role.getId(), shortcuts, shortcutDataSheet)));
            workspaces.add(id);
        }
        return stmts;

    }

    private static String getWorkspaceData(String name, String roleId, List<String> shortcuts, ShortcutDataSheet shortcutDataSheet) {
        JSONObject sampleObject = new JSONObject();
        sampleObject.put("name", SheetScriptGenerator.trim(name));
        sampleObject.put("roleId", SheetScriptGenerator.trim(roleId));
        sampleObject.put("shortcuts", createRoleWorkspacesForUser(shortcuts,shortcutDataSheet.getShortcutUUIDMap()));
        return sampleObject.toJSONString();
    }

    public static String getData(User cg, List<String> workspaces) {
        JSONObject sampleObject = new JSONObject();
        sampleObject.put("firstName", SheetScriptGenerator.formatForSql(cg.getFirstName()));
        sampleObject.put("lastName", SheetScriptGenerator.formatForSql(cg.getLastName()));
        sampleObject.put("email", SheetScriptGenerator.trim(cg.getEmail()));
        sampleObject.put("imageUrl", SheetScriptGenerator.trim(cg.getImageURL()));
        sampleObject.put("taccount", SheetScriptGenerator.trim(cg.getTaccount()));
        sampleObject.put("profile", createProfile(workspaces.iterator().next()));
        sampleObject.put("workspaceIds", createWorkspacesForUser(workspaces));
        sampleObject.put("identifiers", createIdentifiers());

        return sampleObject.toJSONString();
    }

}
