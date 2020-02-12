package com.syngenta.portal.data.model.workspace;

public class WorkspaceTemp {
    private int rowNum;
    private String name;
    private String description;
    private String taccounts;
    private String shortcuts;

    public WorkspaceTemp(int rowNum) {
        this.rowNum = rowNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public String getShortcuts() {
        return shortcuts;
    }

    public void setShortcuts(String shortcuts) {
        this.shortcuts = shortcuts;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTaccounts() {
        return taccounts;
    }

    public void setTaccounts(String taccount) {
        this.taccounts = taccount;
    }

    @Override
    public String toString() {

        return "Workspace-->" + " " + this.name + " ";
    }
}
