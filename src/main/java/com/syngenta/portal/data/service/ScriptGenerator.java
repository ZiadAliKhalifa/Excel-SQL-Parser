package com.syngenta.portal.data.service;

import com.syngenta.portal.data.model.SeedsGatewayDataLoadSheets;
import com.syngenta.portal.data.service.category.CategoryScriptGenerator;
import com.syngenta.portal.data.service.role.RoleScriptGenerator;
import com.syngenta.portal.data.service.shortcut.ShortcutScriptGenerator;
import com.syngenta.portal.data.service.user.UserScriptGenerator;
import com.syngenta.portal.data.service.workspace.WorkspaceScriptGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Riham Fayez
 */
@Service
public class ScriptGenerator {

    @Autowired
    private CategoryScriptGenerator categoryScriptGenerator;

    @Autowired
    private ShortcutScriptGenerator shortcutScriptGenerator;
    @Autowired
    private RoleScriptGenerator roleScriptGenerator;

    @Autowired
    private UserScriptGenerator userScriptGenerator;

    @Autowired
    private WorkspaceScriptGenerator workspaceScriptGenerator;

    public List<String> generate(SeedsGatewayDataLoadSheets migrationSheets) {
        List<String> scripts = new ArrayList<>();
        System.out.println(migrationSheets.getCategoryDataSheet().getCategories().toString());
        scripts.addAll(categoryScriptGenerator.
                generate(migrationSheets.getCategoryDataSheet(),migrationSheets.getCategoryLinksDataSheet()));

        System.out.println(migrationSheets.getShortcutDataSheet().getShortcuts().toString());

        scripts.addAll(shortcutScriptGenerator.
                generate(migrationSheets.getShortcutDataSheet(), migrationSheets.getCategoryDataSheet(),migrationSheets.getShortcutLinksDataSheet(),migrationSheets.getShortcutEnviromentLinksDataSheet()));

        System.out.println(migrationSheets.getRoleDataSheet().getRoles().toString());
        scripts.addAll(roleScriptGenerator.
                generate(migrationSheets.getRoleDataSheet(), migrationSheets.getShortcutDataSheet()));
        System.out.println(migrationSheets.getUserDataSheet().getUsers().toString());
        scripts.addAll(userScriptGenerator.
                generate(migrationSheets.getUserDataSheet(), migrationSheets.getRoleDataSheet(), migrationSheets.getShortcutDataSheet()));
        System.out.println(migrationSheets.getWorkspaceDataSheet().getWorkspaces().toString());
        scripts.addAll(workspaceScriptGenerator.
                generate(migrationSheets.getWorkspaceDataSheet(), migrationSheets.getUserDataSheet(), migrationSheets.getShortcutDataSheet()));
        return scripts;
    }

}