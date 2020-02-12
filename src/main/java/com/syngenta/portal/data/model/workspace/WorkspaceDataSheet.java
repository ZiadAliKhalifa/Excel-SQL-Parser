package com.syngenta.portal.data.model.workspace;

import com.syngenta.portal.data.model.DataSheet;
import com.syngenta.portal.data.model.SheetType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WorkspaceDataSheet extends DataSheet {

    public List<Workspace> workspaces = new ArrayList<>();

    private Map<String, String> workspaceUUIDMap = new HashMap<>();

    public List<Workspace> getWorkspaces() {
        return workspaces;
    }

    public Map<String, String> getWorkspaceUUIDMap() {
        return workspaceUUIDMap;
    }

    @Override
    public String getSheetName() {
        return SheetType.WORKSPACES.getSheetName();
    }

}
