package com.syngenta.portal.data.model.shortcut;

import com.syngenta.portal.data.model.DataSheet;
import com.syngenta.portal.data.model.SheetType;

import java.util.*;


public class ShortcutDataSheet extends DataSheet {

    public List<Shortcut> shortcuts = new ArrayList<>();

    private Map<String, String> shortcutUUIDMap = new HashMap<>();

    public Map<String, List<String>> rolesMap = new HashMap<>();

    public Map<String, Shortcut> shortcutsMap = new HashMap<>();

    public List<Shortcut> getShortcuts() {
        return shortcuts;
    }

    public Map<String, Shortcut> getShortcutsMap() {
        return shortcutsMap;
    }

    public void setShortcutsMap(Map<String, Shortcut> shortcutsMap) {
        this.shortcutsMap = shortcutsMap;
    }

    public Map<String, String> getShortcutUUIDMap() {
        return shortcutUUIDMap;
    }

    public Map<String, List<String>> getRolesMap() {
        return rolesMap;
    }

    public void setRolesMap(Map<String, List<String>> rolesMap) {
        this.rolesMap = rolesMap;
    }

    @Override
    public String getSheetName() {
        return SheetType.SHORTCUTS.getSheetName();
    }

}
