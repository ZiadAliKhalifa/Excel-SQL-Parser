package com.syngenta.portal.data.model;

/**
 * @author Riham Fayez
 */
public enum SheetType {
    USERS("Users"),
    WORKSPACES("Workspaces"),
    CATEGORY("Category"),
    CATEGORY_LINKS("CategoryLinks"),
    SHORTCUTS("Shortcuts"),
    SHORTCUTS_LINKS("ShortcutsLinks"),
    SHORTCUTS_ENVIRONMENT_LINKS("ShortcutsEnvironmentLinks"),
    ROLES("Roles");

    private String sheetName;

    SheetType(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }
}
