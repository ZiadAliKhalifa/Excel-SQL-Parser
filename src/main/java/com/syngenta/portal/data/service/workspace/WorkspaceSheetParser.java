package com.syngenta.portal.data.service.workspace;

import com.syngenta.portal.data.model.DataSheet;
import com.syngenta.portal.data.model.FieldDefinition;
import com.syngenta.portal.data.model.FieldType;
import com.syngenta.portal.data.model.SheetType;
import com.syngenta.portal.data.model.shortcut.Shortcut;
import com.syngenta.portal.data.model.shortcut.ShortcutDataSheet;
import com.syngenta.portal.data.model.user.User;
import com.syngenta.portal.data.model.user.UserDataSheet;
import com.syngenta.portal.data.model.workspace.Workspace;
import com.syngenta.portal.data.model.workspace.WorkspaceDataSheet;
import com.syngenta.portal.data.model.workspace.WorkspaceShortcut;
import com.syngenta.portal.data.model.workspace.WorkspaceTemp;
import com.syngenta.portal.data.service.common.AbstractSheetParser;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class WorkspaceSheetParser extends AbstractSheetParser {
    private static FieldDefinition NAME_DEFINITION =
            new FieldDefinition("Shortcut Name", Boolean.TRUE, 100, FieldType.TEXT);
    private static FieldDefinition DESCR_DEFINITION =
            new FieldDefinition("Shortcut Description", Boolean.FALSE, 1024, FieldType.TEXT);
    private static FieldDefinition TACCOUNT_DEFINITION =
            new FieldDefinition("taccount", Boolean.TRUE, FieldType.TEXT);

    public WorkspaceDataSheet parseDetailsSheet(Workbook workbook, DataSheet... referenceSheets) {
        Sheet workspaceDataSheet = readSheet(workbook, SheetType.WORKSPACES);
        UserDataSheet userDataSheet = (UserDataSheet) referenceSheets[0];
        ShortcutDataSheet shortcutDataSheet = (ShortcutDataSheet) referenceSheets[1];
        return parseSheet(workspaceDataSheet, userDataSheet, shortcutDataSheet);
    }

    private WorkspaceDataSheet parseSheet(Sheet sheet, UserDataSheet userDataSheet, ShortcutDataSheet shortcutDataSheet) {
        List<WorkspaceTemp> parsedResults = new ArrayList<>();
        // parsing rows
        rowsLoop:
        for (Row currentRow : sheet) {
            if (currentRow.getRowNum() == 0 || currentRow.getRowNum() == 1) {
                continue; // just skip the rows if row number is 0
            }

            WorkspaceTemp workspace = new WorkspaceTemp(currentRow.getRowNum()+1);
            // parsing cells
            cellsLoop:
            for (Cell currentCell : currentRow) {
                String value = getCellValue(currentCell);
                switch (currentCell.getColumnIndex()) {
                    case 0:
                        workspace.setName(value);
                        break;
                    case 1:
                        workspace.setDescription(value);
                        break;
                    case 2:
                        workspace.setShortcuts(value);
                        break;
                    case 3:
                        workspace.setTaccounts(value);
                        break;
                    default:
                        break cellsLoop;
                }

            }
            if (!isEmpty(workspace)) {
                parsedResults.add(workspace);
            } else {
                break rowsLoop;
            }
        }

        return parseSheet(parsedResults, userDataSheet, shortcutDataSheet);
    }


    private WorkspaceDataSheet parseSheet(List<WorkspaceTemp> parsedResults, UserDataSheet userDataSheet, ShortcutDataSheet shortcutDataSheet) {
        List<String> errors = new ArrayList<>();
        WorkspaceDataSheet workspaceDataSheet = new WorkspaceDataSheet();
        // parsing rows
        rowsLoop:
        for (WorkspaceTemp currentRow : parsedResults) {
            Workspace workspace = new Workspace();
            parseName(workspace, currentRow.getName(), errors, currentRow.getRowNum());
            parseDescription(workspace, currentRow.getDescription(), errors, currentRow.getRowNum());
            parseShortcuts(workspace, currentRow.getShortcuts(), errors, currentRow.getRowNum(), shortcutDataSheet.getShortcutsMap());
            parseUser(workspace, currentRow.getTaccounts(), errors, currentRow.getRowNum(), userDataSheet.getUsersTaccounts());
            workspaceDataSheet.getWorkspaces().add(workspace);
        }

        if (errors.size() > 0) {
            workspaceDataSheet.getErrors().addAll(errors);
            workspaceDataSheet.setValidSheet(Boolean.FALSE);
        }
        return workspaceDataSheet;
    }


    private boolean isEmpty(WorkspaceTemp workspace) {
        return StringUtils.isEmpty(workspace.getDescription()) && StringUtils.isEmpty(workspace.getName())
                && StringUtils.isEmpty(workspace.getShortcuts())
                && StringUtils.isEmpty(workspace.getTaccounts());

    }

    private void parseName(Workspace workspace, String value, List<String> errors, int rowNumber) {
        workspace.setName(formatString(value));
        validateField(NAME_DEFINITION, value, errors, rowNumber);
    }


    private void parseDescription(Workspace workspace, String value, List<String> errors, int rowNumber) {
        workspace.setDescription(formatString(value));
        validateField(DESCR_DEFINITION, value, errors, rowNumber);
    }

    private void parseShortcuts(Workspace workspace, String value, List<String> errors, int rowNumber, Map<String, Shortcut> shortcuts) {
        if (value == null || value.isEmpty()) {
            errors.add(String.format("Row %s, Workspace should be associated with shortcuts", rowNumber));
        } else {
            List<WorkspaceShortcut> wsShortcuts = new ArrayList<>();
            Stream.of(value.split(",")).forEach(v -> {
                int i = 1;
                if (shortcuts.get(v.trim().toUpperCase()) != null) {
                    errors.add(String.format("Row %s, Shortcut %s does not exist", rowNumber, value));
                } else {
                    wsShortcuts.add(new WorkspaceShortcut(v.toUpperCase().trim(), i));
                    i += 1;
                }
            });
            workspace.setShortcuts(wsShortcuts);
        }

    }
    private void parseUser(Workspace workspace, String value, List<String> errors, int rowNumber, Set<String> users) {
        List<String> parsedUsers = new ArrayList<>();
        validateField(TACCOUNT_DEFINITION, value, errors, rowNumber);
        if (value != null && !value.isEmpty()) {
            Stream.of(value.split(",")).forEach(v -> {
                if (!users.contains(v.trim().toUpperCase())) {
                    errors.add(String.format("Row %s, User %s does not exist", rowNumber, v));
                }
                parsedUsers.add(v);
            });

        }
        workspace.setTaccount(parsedUsers);
    }

}
