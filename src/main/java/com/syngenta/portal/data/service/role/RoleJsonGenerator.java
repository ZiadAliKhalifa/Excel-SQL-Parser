package com.syngenta.portal.data.service.role;

import org.json.simple.JSONArray;

import java.util.List;
import java.util.Map;

public class RoleJsonGenerator {
    public static JSONArray createShortcuts(List<String> shortcuts, Map<String, String> shortcutUUIDMap) {
        JSONArray shortcutsIds = new JSONArray();
        if (shortcuts != null) {
            for (String s : shortcuts) {
                shortcutsIds.add(shortcutUUIDMap.get(s.toUpperCase()));
            }
        }
        return shortcutsIds;
    }
}
