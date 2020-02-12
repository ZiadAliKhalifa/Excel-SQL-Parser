package com.syngenta.portal.data.service.user;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.Map;

public class UserJsonGenerator {
    final static String VIEW_TYPE = "Module";
    final static String BGD_IMAGE = "background";
    final static String DEFAULT_COLOR = "main";

    public static JSONObject createProfile(String lastAccessedWorkspaceId) {

        JSONObject theme = new JSONObject();
        theme.put("lastSelectedBackgroundImage", BGD_IMAGE);
        theme.put("lastUsedSidebarColorName", DEFAULT_COLOR);

        JSONObject profile = new JSONObject();
        profile.put("lastAccessedWorkspaceId", lastAccessedWorkspaceId);
        profile.put("lastUsedWorkspaceViewType", VIEW_TYPE);
        profile.put("lastAccessDate", "");
        profile.put("theme", theme);
        return profile;
    }

    public static JSONArray createWorkspacesForUser(List<String> workspaces) {
        JSONArray workspacesJson = new JSONArray();
        for (String wrs : workspaces) {
            workspacesJson.add(wrs);
        }
        return workspacesJson;
    }

    public static JSONObject createIdentifiers() {
        JSONObject identifiers = new JSONObject();
        identifiers.put("xrd_oid", "");
        identifiers.put("azureAdSid", "");
        return identifiers;
    }


    public static JSONArray createRoleWorkspacesForUser(List<String> shortcuts,
                                                        Map<String, String> shortcutUUIDMap) {
        JSONArray shortcutsJson = new JSONArray();
        int i = 1;
        if (shortcuts != null) {
            for (String ws : shortcuts) {
                JSONObject s = new JSONObject();
                s.put("shortcutId", shortcutUUIDMap.get(ws.toUpperCase().trim()));
                s.put("order", i);
                s.put("isInheritedFromRole", true);
                shortcutsJson.add(s);
                i++;
            }

        }
        return shortcutsJson;
    }

}
