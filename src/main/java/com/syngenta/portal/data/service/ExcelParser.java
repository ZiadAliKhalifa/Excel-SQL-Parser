package com.syngenta.portal.data.service;

import com.syngenta.portal.data.model.SeedsGatewayDataLoadSheets;
import com.syngenta.portal.data.service.category.CategoryLinksSheetParser;
import com.syngenta.portal.data.service.category.CategorySheetParser;
import com.syngenta.portal.data.service.role.RoleSheetParser;
import com.syngenta.portal.data.service.shortcut.ShortcutEnviromentLinksSheetParser;
import com.syngenta.portal.data.service.shortcut.ShortcutLinksSheetParser;
import com.syngenta.portal.data.service.shortcut.ShortcutSheetParser;
import com.syngenta.portal.data.service.user.UserSheetParser;
import com.syngenta.portal.data.service.workspace.WorkspaceSheetParser;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Riham Fayez
 */
@Service
public class ExcelParser {

    @Autowired
    private CategorySheetParser categorySheetParser;
    @Autowired
    private CategoryLinksSheetParser categoryLinksSheetParser;
    @Autowired
    private RoleSheetParser roleSheetParser;
    @Autowired
    private ShortcutSheetParser shortcutSheetParser;
    @Autowired
    private ShortcutLinksSheetParser shortcutLinksSheetParser;
    @Autowired
    private ShortcutEnviromentLinksSheetParser shortcutEnviromentLinksSheetParser;
    @Autowired
    private UserSheetParser userSheetParser;
    @Autowired
    private WorkspaceSheetParser workspaceSheetParser;

    public SeedsGatewayDataLoadSheets parseExcelWorkBook(Workbook workbook) {

        SeedsGatewayDataLoadSheets sheets = new SeedsGatewayDataLoadSheets();
        sheets.setCategoryDataSheet(categorySheetParser.parseDetailsSheet(workbook));
        sheets.setCategoryLinksDataSheet(categoryLinksSheetParser.parseDetailsSheet(workbook, sheets.getCategoryDataSheet()));

        sheets.setRoleDataSheet(roleSheetParser.parseDetailsSheet(workbook, sheets.getShortcutDataSheet()));

        sheets.setShortcutDataSheet(shortcutSheetParser.parseDetailsSheet(workbook, sheets.getCategoryDataSheet(), sheets.getRoleDataSheet()));
        sheets.setShortcutLinksDataSheet(shortcutLinksSheetParser.parseDetailsSheet(workbook, sheets.getShortcutDataSheet()));
        sheets.setShortcutEnviromentLinksDataSheet(shortcutEnviromentLinksSheetParser.parseDetailsSheet(workbook, sheets.getShortcutDataSheet()));

        sheets.setUserDataSheet(userSheetParser.parseDetailsSheet(workbook, sheets.getRoleDataSheet()));
        sheets.setWorkspaceDataSheet(workspaceSheetParser.parseDetailsSheet(workbook, sheets.getUserDataSheet(), sheets.getShortcutDataSheet()));
        return sheets;
    }
}
