package com.syngenta.portal.data.service.role;

import com.syngenta.portal.data.model.*;
import com.syngenta.portal.data.model.category.Category;
import com.syngenta.portal.data.model.role.Role;
import com.syngenta.portal.data.model.role.RoleDataSheet;
import com.syngenta.portal.data.model.role.RoleTemp;
import com.syngenta.portal.data.model.shortcut.ShortcutDataSheet;
import com.syngenta.portal.data.service.common.AbstractSheetParser;
import com.syngenta.portal.data.service.common.ParserBusinessException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RoleSheetParser extends AbstractSheetParser {
    private static FieldDefinition NAME_DEFINITION =
            new FieldDefinition("Role Name", Boolean.TRUE, 40, FieldType.TEXT);
    private static FieldDefinition DESCR_DEFINITION =
            new FieldDefinition("Role Description", Boolean.FALSE, 500, FieldType.TEXT);

    public RoleDataSheet parseDetailsSheet(Workbook workbook, DataSheet... referenceSheets) {
        Sheet roleDataSheet = readSheet(workbook, SheetType.ROLES);
        return parseSheet(roleDataSheet);
    }

    private RoleDataSheet parseSheet(Sheet sheet) {
        List<RoleTemp> tempParsedResults = new ArrayList<>();
        // parsing rows
        rowsLoop:
        for (Row currentRow : sheet) {
            if (currentRow.getRowNum() == 0 || currentRow.getRowNum() == 1) {
                continue; // just skip the rows if row number is 0
            }

            RoleTemp role = new RoleTemp(currentRow.getRowNum()+1);
            // parsing cells
            cellsLoop:
            for (Cell currentCell : currentRow) {
                String value = getCellValue(currentCell);
                switch (currentCell.getColumnIndex()) {
                    case 0:
                        role.setName(value);
                        break;
                    case 1:
                        role.setDescription(value);
                        break;
                    case 2:
                        role.setTags(value);
                        break;
                    default:
                        break cellsLoop;
                }
            }

            if (!isEmpty(role)) {
                tempParsedResults.add(role);

            } else {
                break rowsLoop;
            }

        }
        return parse(tempParsedResults);
    }

    private RoleDataSheet parse(List<RoleTemp> tempParsedResults) {
        Set<String> rolesSet = new HashSet<>();
        List<String> errors = new ArrayList<>();
        RoleDataSheet roleDataSheet = new RoleDataSheet();

        for (RoleTemp row : tempParsedResults) {
            Role role = new Role();
            parseName(role, row.getName(), errors, row.getRowNum(),rolesSet);
            parseDescription(role, row.getDescription(), errors, row.getRowNum());
            parseTags(role, row.getTags(), errors, row.getRowNum());
            roleDataSheet.getRoles().add(role);
            if (role.getName() != null) {
                roleDataSheet.getRolesNames().add(role.getName().trim().toUpperCase());
            }


        }


        if (errors.size() > 0) {
            roleDataSheet.getErrors().addAll(errors);
            roleDataSheet.setValidSheet(Boolean.FALSE);
        }
        return roleDataSheet;
    }


    private boolean isEmpty(RoleTemp role) {
        return StringUtils.isEmpty(role.getDescription()) && StringUtils.isEmpty(role.getName())
                && (role.getTags() == null || role.getTags().isEmpty());

    }

    private void parseName(Role role, String value, List<String> errors, int rowNumber, Set<String> rolesSet) {
        role.setName(formatString(value));
        //role.setId(formatId(value));
        validateField(NAME_DEFINITION, value, errors, rowNumber);
        if (value != null && !value.isEmpty() && !rolesSet.add(value.trim().toUpperCase())) {
            errors.add(String.format("Row %s, Role %s already exist", rowNumber, value));
        }
    }

    private String formatId(String name) {
        if (name == null || name.isEmpty())
            return "";
        String value = name.trim().replaceAll(" ", "_");
        value = value.toUpperCase();
        return value;
    }


    private void parseDescription(Role role, String value, List<String> errors, int rowNumber) {
        role.setDescription(formatString(value));
        validateField(DESCR_DEFINITION, value, errors, rowNumber);
    }

    private void parseTags(Role role, String value, List<String> errors, int rowNumber) {
        ParseResults<String> parseResults = super.parseTags(value, rowNumber);
        errors.addAll(parseResults.getErrors());
        role.setTags(parseResults.getParsedData());
    }


}
