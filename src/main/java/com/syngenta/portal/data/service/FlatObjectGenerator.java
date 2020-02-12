package com.syngenta.portal.data.service;

import com.syngenta.portal.data.model.SeedsGatewayDataLoadSheets;
import com.syngenta.portal.data.service.category.CategoryFlatObjectGenerator;
import com.syngenta.portal.data.service.role.RoleFlatObjectGenerator;
import com.syngenta.portal.data.service.shortcut.ShortcutFlatObjectGenerator;
import com.syngenta.portal.data.service.user.UserFlatObjectGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Riham Fayez
 */
@Service
public class FlatObjectGenerator {

    @Autowired
    private CategoryFlatObjectGenerator categoryFlatObjectGenerator;

    @Autowired
    private ShortcutFlatObjectGenerator shortcutFlatObjectGenerator;

    @Autowired
    private RoleFlatObjectGenerator roleFlatObjectGenerator;

    @Autowired
    private UserFlatObjectGenerator userFlatObjectGenerator;

    public List<String> generate(SeedsGatewayDataLoadSheets migrationSheets) {
        List<String> objects = new ArrayList<>();

        objects.addAll(categoryFlatObjectGenerator.
                generate(migrationSheets.getCategoryDataSheet(),migrationSheets.getCategoryLinksDataSheet()));
        objects.addAll(shortcutFlatObjectGenerator.
                generate(migrationSheets.getShortcutDataSheet(), migrationSheets.getCategoryDataSheet(),migrationSheets.getShortcutLinksDataSheet(),migrationSheets.getShortcutEnviromentLinksDataSheet()));
        objects.addAll(roleFlatObjectGenerator.
                generate(migrationSheets.getRoleDataSheet(), migrationSheets.getShortcutDataSheet()));
        objects.addAll(userFlatObjectGenerator.
                generate(migrationSheets.getUserDataSheet(), migrationSheets.getRoleDataSheet(), migrationSheets.getShortcutDataSheet()));

        return objects;
    }

}