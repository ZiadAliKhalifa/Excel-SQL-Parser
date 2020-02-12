package com.syngenta.portal.data.model.role;

import com.syngenta.portal.data.model.DataSheet;
import com.syngenta.portal.data.model.SheetType;
import com.syngenta.portal.data.model.category.Category;

import java.util.*;


public class RoleDataSheet extends DataSheet {

    public List<Role> roles = new ArrayList<>();

    private Map<String, String> roleUUIDMap = new HashMap<>();
    private Set<String> rolesNames = new HashSet<>();

    public Set<String> getRolesNames() {
        return rolesNames;
    }

    public void setRolesNames(Set<String> rolesNames) {
        this.rolesNames = rolesNames;
    }


    public List<Role> getRoles() {
        return roles;
    }


    public Map<String, String> getRoleUUIDMap() {
        return roleUUIDMap;
    }

    @Override
    public String getSheetName() {
        return SheetType.ROLES.getSheetName();
    }

}
