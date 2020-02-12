package com.syngenta.portal.data.model.user;

import com.syngenta.portal.data.model.DataSheet;
import com.syngenta.portal.data.model.SheetType;

import java.util.*;


public class UserDataSheet extends DataSheet {

    public List<User> users = new ArrayList<>();



    public Set<String> usersTaccounts = new HashSet<>();

    private Map<String, String> userUUIDMap = new HashMap<>();

    public List<User> getUsers() {
        return users;
    }

    @Override
    public String getSheetName() {
        return SheetType.USERS.getSheetName();
    }

    public Map<String, String> getUserUUIDMap() {
        return userUUIDMap;
    }

    public void setUserUUIDMap(Map<String, String> userUUIDMap) {
        this.userUUIDMap = userUUIDMap;
    }

    public Set<String> getUsersTaccounts() {
        return usersTaccounts;
    }

    public void setUsersTaccounts(Set<String> usersTaccounts) {
        this.usersTaccounts = usersTaccounts;
    }
}
