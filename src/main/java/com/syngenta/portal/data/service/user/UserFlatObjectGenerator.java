package com.syngenta.portal.data.service.user;


import com.syngenta.portal.data.model.DataSheet;
import com.syngenta.portal.data.model.role.Role;
import com.syngenta.portal.data.model.role.RoleDataSheet;
import com.syngenta.portal.data.model.shortcut.ShortcutDataSheet;
import com.syngenta.portal.data.model.user.User;
import com.syngenta.portal.data.model.user.UserDataSheet;
import com.syngenta.portal.data.service.JsonGenerator;
import com.syngenta.portal.data.service.SheetScriptGenerator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.syngenta.portal.data.service.user.UserJsonGenerator.*;


@Service
public class UserFlatObjectGenerator extends JsonGenerator implements SheetScriptGenerator<UserDataSheet> {
    final static String FIRST_USER_UUID = "6ef1467e-7fe0-4c34-a5e9-5baded575c44";

    @Override
    public List<String> generate(UserDataSheet dataSheet, DataSheet... referenceSheets) {
        List<String> data = new ArrayList<String>();
        JSONArray users = new JSONArray();
        int index = 0;
        JSONArray workspacesJson = new JSONArray();
        RoleDataSheet roleDataSheet = (RoleDataSheet) referenceSheets[0];
        ShortcutDataSheet shortcutDataSheet = (ShortcutDataSheet) referenceSheets[1];
        for (User cg : dataSheet.getUsers()) {
            if (index == 0) {
                cg.setId(FIRST_USER_UUID);
            } else {
                cg.setId(UUID.randomUUID().toString());
            }
            List<String> workspaces = new ArrayList<>();
            for (String roleName : cg.getRoles()) {
                Role role = roleDataSheet.getRoles().stream()
                        .filter(r -> r.getName().toUpperCase().equals(roleName.toUpperCase().trim()))
                        .findFirst()
                        .get();
                List<String> shortcuts = role.getShortcuts();
                String id = UUID.randomUUID().toString();
                workspacesJson.add(getWorkspaceData(id, role.getName(), role.getId(), shortcuts, shortcutDataSheet.getShortcutUUIDMap()));
                workspaces.add(id);
            }
            index++;
            users.add(getData(cg, workspaces));

        }
        data.add("//const workspaces=");
        data.add(workspacesJson.toJSONString());
        data.add("//;");

        data.add("//const users=");
        data.add(users.toJSONString());
        data.add("//;");
        return data;
    }

    private static JSONObject getWorkspaceData(String id, String name, String roleId, List<String> shortcuts,
                                               Map<String, String> shortcutUUIDMap) {
        JSONObject sampleObject = new JSONObject();
        sampleObject.put("id", id);
        sampleObject.put("name", name);
        sampleObject.put("roleId", roleId);
        sampleObject.put("shortcuts", createRoleWorkspacesForUser(shortcuts, shortcutUUIDMap));

        return sampleObject;
    }

    public JSONObject getData(User cg, List<String> workspaces) {
        JSONObject sampleObject = new JSONObject();
        sampleObject.put("id", cg.getId());
        sampleObject.put("firstName", SheetScriptGenerator.trim(cg.getFirstName()));
        sampleObject.put("lastName", SheetScriptGenerator.trim(cg.getLastName()));
        sampleObject.put("email", SheetScriptGenerator.trim(cg.getEmail()));
        sampleObject.put("imageUrl", SheetScriptGenerator.trim(cg.getImageURL()));
        sampleObject.put("taccount", SheetScriptGenerator.trim(cg.getTaccount()));
        sampleObject.put("tags", createTags(cg.getTags()));
        sampleObject.put("profile", createProfile(workspaces.iterator().next()));
        sampleObject.put("workspaceIds", createWorkspacesForUser(workspaces));
        sampleObject.put("identifiers", createIdentifiers());
        addAuditValues(sampleObject);
        addStatusValue(sampleObject);
        return sampleObject;
    }

}
