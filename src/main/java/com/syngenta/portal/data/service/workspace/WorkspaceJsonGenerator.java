package com.syngenta.portal.data.service.workspace;

import com.syngenta.portal.data.model.workspace.WorkspaceShortcut;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.Map;

public class WorkspaceJsonGenerator {
    public static JSONArray createWorkspaceShortcuts(List<WorkspaceShortcut> workspaceShortcuts,Map<String, String> shortcutUUIDMap) {
        JSONArray shortcuts = new JSONArray();
        if (workspaceShortcuts != null && workspaceShortcuts.size() > 0) {
            int i = 1;
            for (WorkspaceShortcut ws : workspaceShortcuts) {

                JSONObject shortcut = new JSONObject();
                shortcut.put("shortcutId", shortcutUUIDMap.get(ws.getShortcutName().toUpperCase().trim()));
                shortcut.put("order", i);
                shortcut.put("isInheritedFromRole", false);
                shortcuts.add(shortcut);
                i++;
            }
        }
        return shortcuts;
    }
}
