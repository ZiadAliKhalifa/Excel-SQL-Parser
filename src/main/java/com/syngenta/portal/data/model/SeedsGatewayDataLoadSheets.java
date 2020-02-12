package com.syngenta.portal.data.model;

import com.syngenta.portal.data.model.category.CategoryDataSheet;
import com.syngenta.portal.data.model.category.CategoryLinksDataSheet;
import com.syngenta.portal.data.model.role.RoleDataSheet;
import com.syngenta.portal.data.model.shortcut.ShortcutDataSheet;
import com.syngenta.portal.data.model.shortcut.ShortcutEnviromentLinksDataSheet;
import com.syngenta.portal.data.model.shortcut.ShortcutLinksDataSheet;
import com.syngenta.portal.data.model.user.UserDataSheet;
import com.syngenta.portal.data.model.workspace.WorkspaceDataSheet;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Riham Fayez
 */
public class SeedsGatewayDataLoadSheets {

    private CategoryDataSheet categoryDataSheet;
    private ShortcutDataSheet shortcutDataSheet;
    private ShortcutLinksDataSheet shortcutLinksDataSheet;
    private ShortcutEnviromentLinksDataSheet shortcutEnviromentLinksDataSheet;
    private CategoryLinksDataSheet categoryLinksDataSheet;
    private WorkspaceDataSheet workspaceDataSheet;
    private UserDataSheet userDataSheet;
    private RoleDataSheet roleDataSheet;

    public boolean isValid() {
        return
                categoryDataSheet.isValidSheet() && categoryLinksDataSheet.isValidSheet() &&
                        shortcutDataSheet.isValidSheet() && roleDataSheet.isValidSheet() && userDataSheet.isValidSheet() &&
                        workspaceDataSheet.isValidSheet() && shortcutLinksDataSheet.isValidSheet() && shortcutEnviromentLinksDataSheet.isValidSheet();

    }

    public CategoryDataSheet getCategoryDataSheet() {
        return categoryDataSheet;
    }

    public void setCategoryDataSheet(CategoryDataSheet categoryDataSheet) {
        this.categoryDataSheet = categoryDataSheet;
    }


    public CategoryLinksDataSheet getCategoryLinksDataSheet() {
        return categoryLinksDataSheet;
    }

    public void setCategoryLinksDataSheet(CategoryLinksDataSheet categoryLinksDataSheet) {
        this.categoryLinksDataSheet = categoryLinksDataSheet;
    }


    public ShortcutDataSheet getShortcutDataSheet() {
        return shortcutDataSheet;
    }

    public void setShortcutDataSheet(ShortcutDataSheet shortcutDataSheet) {
        this.shortcutDataSheet = shortcutDataSheet;
    }

    public WorkspaceDataSheet getWorkspaceDataSheet() {
        return workspaceDataSheet;
    }

    public void setWorkspaceDataSheet(WorkspaceDataSheet workspaceDataSheet) {
        this.workspaceDataSheet = workspaceDataSheet;
    }

    public UserDataSheet getUserDataSheet() {
        return userDataSheet;
    }

    public void setUserDataSheet(UserDataSheet userDataSheet) {
        this.userDataSheet = userDataSheet;
    }

    public RoleDataSheet getRoleDataSheet() {
        return roleDataSheet;
    }

    public void setRoleDataSheet(RoleDataSheet roleDataSheet) {
        this.roleDataSheet = roleDataSheet;
    }

    public ShortcutLinksDataSheet getShortcutLinksDataSheet() {
        return shortcutLinksDataSheet;
    }

    public void setShortcutLinksDataSheet(ShortcutLinksDataSheet shortcutLinksDataSheet) {
        this.shortcutLinksDataSheet = shortcutLinksDataSheet;
    }

    public ShortcutEnviromentLinksDataSheet getShortcutEnviromentLinksDataSheet() {
        return shortcutEnviromentLinksDataSheet;
    }

    public void setShortcutEnviromentLinksDataSheet(ShortcutEnviromentLinksDataSheet shortcutEnviromentLinksDataSheet) {
        this.shortcutEnviromentLinksDataSheet = shortcutEnviromentLinksDataSheet;
    }

    public void printErrors() {
        System.out.println("----------------------------");
        System.out.println("Sheets Error List:");
        System.out.println("----------------------------");

        if (!isValid()) {
            categoryDataSheet.printErrors();
            categoryLinksDataSheet.printErrors();
            shortcutDataSheet.printErrors();
            shortcutLinksDataSheet.printErrors();
            shortcutEnviromentLinksDataSheet.printErrors();
            workspaceDataSheet.printErrors();
            userDataSheet.printErrors();
            roleDataSheet.printErrors();

        } else {
            System.out.println("No Errors Found");
        }

    }

    public List<String> getErrors() {
        List<String> errors = new ArrayList<>();
        if (!isValid()) {
            errors.add("Error List:" + categoryDataSheet.getSheetName());
            errors.addAll(categoryDataSheet.getErrors());
            errors.add("-----------------------------------------------\n");

            errors.add("Error List:" + categoryLinksDataSheet.getSheetName());
            errors.addAll(categoryLinksDataSheet.getErrors());
            errors.add("-----------------------------------------------\n");

            errors.add("Error List:" + shortcutDataSheet.getSheetName());
            errors.addAll(shortcutDataSheet.getErrors());
            errors.add("-----------------------------------------------\n");

            errors.add("Error List:" + shortcutLinksDataSheet.getSheetName());
            errors.addAll(shortcutLinksDataSheet.getErrors());
            errors.add("-----------------------------------------------\n");

            errors.add("Error List:" + shortcutEnviromentLinksDataSheet.getSheetName());
            errors.addAll(shortcutEnviromentLinksDataSheet.getErrors());
            errors.add("-----------------------------------------------\n");

            errors.add("Error List:" + workspaceDataSheet.getSheetName());
            errors.addAll(workspaceDataSheet.getErrors());
            errors.add("-----------------------------------------------\n");

            errors.add("Error List for " + userDataSheet.getSheetName());
            errors.addAll(userDataSheet.getErrors());
            errors.add("-----------------------------------------------\n");

            errors.add("Error List for " + roleDataSheet.getSheetName() + " :");
            errors.addAll(roleDataSheet.getErrors());
            errors.add("-----------------------------------------------\n");
        } else {
            System.out.println("No Errors Found");
        }
        return errors;
    }
}
