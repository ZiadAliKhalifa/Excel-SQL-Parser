package com.syngenta.portal.data.model.workspace;

import java.util.List;

public class Workspace {
    private String id;
    private String name;
    private String description;
    private List<String> taccount;
    private List<WorkspaceShortcut> shortcuts;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<WorkspaceShortcut> getShortcuts() {
        return shortcuts;
    }

    public void setShortcuts(List<WorkspaceShortcut> shortcuts) {
        this.shortcuts = shortcuts;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTaccount() {
        return taccount;
    }

    public void setTaccount(List<String> taccount) {
        this.taccount = taccount;
    }

    @Override
    public String toString() {

        return "Workspace-->"+" "+this.name+" ";
    }
}
