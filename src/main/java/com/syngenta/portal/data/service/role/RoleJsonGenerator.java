package com.syngenta.portal.data.service.role;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

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

    public static JSONArray createShortcutsObj(List<String> shortcuts, Map<String, String> shortcutUUIDMap) {
        JSONArray shortcutsObjArr = new JSONArray();
        if (shortcuts != null) {
            int order = 1;
            String now = getDateTimeNow();
            for (String s : shortcuts) {
                shortcutsObjArr.add(createShortcut(order, shortcutUUIDMap.get(s.toUpperCase()), now));
                order++;
            }
        }
        return shortcutsObjArr;
    }


    private static String getDateTimeNow() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }

    private static JSONObject createShortcut(int order, String shortcutId, String now) {
        JSONObject s = new JSONObject();
        s.put("shortcutId", shortcutId);
        s.put("order", order);
        s.put("addedOnDate", now);
        return s;
    }
}
