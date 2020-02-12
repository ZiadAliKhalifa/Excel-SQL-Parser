package com.syngenta.portal.data.model.workspace;

public class WorkspaceShortcut {


    public WorkspaceShortcut(String shortcutName, int order) {
        this.shortcutName = shortcutName;
        this.order = order;
    }

    String shortcutName;
    int order;
    boolean inhertedFromRole = true;


    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isInhertedFromRole() {
        return inhertedFromRole;
    }

    public void setInhertedFromRole(boolean inhertedFromRole) {
        this.inhertedFromRole = inhertedFromRole;
    }

    public String getShortcutName() {
        return shortcutName;
    }

    public void setShortcutName(String shortcutName) {
        this.shortcutName = shortcutName;
    }

    @Override
    public String toString() {

        return "Workspace Shortcut-->"+" "+this.shortcutName+" ";
    }
}
