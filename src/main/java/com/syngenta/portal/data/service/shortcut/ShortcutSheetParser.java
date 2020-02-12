package com.syngenta.portal.data.service.shortcut;

import com.syngenta.portal.data.model.*;
import com.syngenta.portal.data.model.category.Category;
import com.syngenta.portal.data.model.category.CategoryDataSheet;
import com.syngenta.portal.data.model.role.Role;
import com.syngenta.portal.data.model.role.RoleDataSheet;
import com.syngenta.portal.data.model.shortcut.Shortcut;
import com.syngenta.portal.data.model.shortcut.ShortcutDataSheet;
import com.syngenta.portal.data.model.shortcut.ShortcutTemp;
import com.syngenta.portal.data.service.common.AbstractSheetParser;
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
public class ShortcutSheetParser extends AbstractSheetParser {
    private static FieldDefinition NAME_DEFINITION =
            new FieldDefinition("Shortcut Name", Boolean.TRUE, 100, FieldType.TEXT);
    private static FieldDefinition DESCR_DEFINITION =
            new FieldDefinition("Shortcut Description", Boolean.FALSE, 1024, FieldType.TEXT);
    private static FieldDefinition CATEGORY_DEFINITION =
            new FieldDefinition("Category", Boolean.TRUE, 50, FieldType.TEXT);
    private static FieldDefinition IMAGE_URL_DEFINITION =
            new FieldDefinition("Image url", Boolean.FALSE, 2000, FieldType.TEXT);
    private static FieldDefinition DEFAULT_ENV_DEFINITION =
            new FieldDefinition("Default Enviroment", Boolean.TRUE, 1024, FieldType.TEXT);
    private static FieldDefinition ACCESS_INFO_DEFINITION =
            new FieldDefinition("Access Information", Boolean.FALSE, 1024, FieldType.TEXT);



    public ShortcutDataSheet parseDetailsSheet(Workbook workbook, DataSheet... referenceSheets) {
        Sheet shortcutDataSheet = readSheet(workbook, SheetType.SHORTCUTS);
        CategoryDataSheet categoryDataSheet = (CategoryDataSheet) referenceSheets[0];
        RoleDataSheet roleDataSheet = (RoleDataSheet) referenceSheets[1];
        return parseSheet(shortcutDataSheet, categoryDataSheet, roleDataSheet);
    }

    private ShortcutDataSheet parseSheet(Sheet sheet, CategoryDataSheet categoryDataSheet, RoleDataSheet roleDataSheet) {
        List<ShortcutTemp> parsedData = new ArrayList<>();
        // parsing rows
        rowsLoop:
        for (Row currentRow : sheet) {
            if (currentRow.getRowNum() == 0 || currentRow.getRowNum() == 1) {
                continue; // just skip the rows if row number is 0
            }

            ShortcutTemp shortcut = new ShortcutTemp(currentRow.getRowNum()+1);
            // parsing cells
            cellsLoop:
            for (Cell currentCell : currentRow) {
                String value = getCellValue(currentCell);
                switch (currentCell.getColumnIndex()) {
                    case 0:
                        shortcut.setName(value);
                        break;
                    case 1:
                        shortcut.setDescription(value);
                        break;
                    case 2:
                        shortcut.setCategory(value);
                        break;
                    case 3:
                        shortcut.setRoles(value);
                        break;
                    case 4:
                        shortcut.setTags(value);
                        break;
                    case 5:
                        shortcut.setImageURL(value);
                        break;
                    case 6:
                        shortcut.setDefaultEnviroment(value);
                        break;
                    case 7:
                        shortcut.setAccessInformation(value);
                        break;
                    default:
                        break cellsLoop;
                }

            }
            if (!isEmpty(shortcut)) {
                parsedData.add(shortcut);
            } else {
                break rowsLoop;
            }

        }
        return parse(parsedData, categoryDataSheet, roleDataSheet);
    }

    private ShortcutDataSheet parse(List<ShortcutTemp> parsedData, CategoryDataSheet categoryDataSheet, RoleDataSheet roleDataSheet) {
        List<String> errors = new ArrayList<>();
        Set<String> shortcutSet = new HashSet<>();
        ShortcutDataSheet shortcutDataSheet = new ShortcutDataSheet();
        List<String> roles = new ArrayList<>();
        // parsing rows
        for (ShortcutTemp row : parsedData) {
            Shortcut shortcut = new Shortcut();
            parseName(shortcut, row.getName(), errors, row.getRowNum(),shortcutSet);
            parseDescription(shortcut, row.getDescription(), errors, row.getRowNum());
            parseCategory(shortcut, row.getCategory(), errors, row.getRowNum(), categoryDataSheet.getCategories());
            roles = parseRoles(shortcut, row.getRoles(), errors, row.getRowNum(), roleDataSheet.getRolesNames());
            parseTags(shortcut, row.getTags(), errors, row.getRowNum());
            parseImageUrl(shortcut, row.getImageURL(), errors, row.getRowNum());
            parseDefaultEnviroment(shortcut, row.getDefaultEnviroment(), errors, row.getRowNum());


            shortcutDataSheet.getShortcuts().add(shortcut);
            for (String role : roles) {
                String roleFrmt=role.trim().toUpperCase();
                if (null == shortcutDataSheet.getRolesMap().get(roleFrmt)) {
                    shortcutDataSheet.getRolesMap().put(roleFrmt, new ArrayList<String>());
                }
                shortcutDataSheet.getRolesMap().get(roleFrmt).add(shortcut.getName());
            }
        }
        if (errors.size() > 0)

        {
            shortcutDataSheet.getErrors().addAll(errors);
            shortcutDataSheet.setValidSheet(Boolean.FALSE);
        }
        return shortcutDataSheet;
    }

    private boolean isEmpty(ShortcutTemp shortcut) {
        return StringUtils.isEmpty(shortcut.getDescription()) &&
                StringUtils.isEmpty(shortcut.getName()) &&
                StringUtils.isEmpty(shortcut.getCategory()) &&
                StringUtils.isEmpty(shortcut.getDefaultEnviroment())
                && StringUtils.isEmpty(shortcut.getImageURL()) &&
                StringUtils.isEmpty(shortcut.getTags()) &&
                StringUtils.isEmpty(shortcut.getRoles());

    }

    private void parseName(Shortcut shortcut, String value, List<String> errors, int rowNumber,Set<String> shortcutSet) {
        shortcut.setName(formatString(value));
        validateField(NAME_DEFINITION, value, errors, rowNumber);
        if (value != null && !value.isEmpty() && !shortcutSet.add(value.trim().toUpperCase())) {
            errors.add(String.format("Row %s, Shortcut %s already exist", rowNumber, value));
        }
    }

    private void parseDefaultEnviroment(Shortcut shortcut, String value, List<String> errors, int rowNumber) {
        shortcut.setDefaultEnviroment(formatString(value));
        validateField(DEFAULT_ENV_DEFINITION, value, errors, rowNumber);
    }

    private void parseDescription(Shortcut shortcut, String value, List<String> errors, int rowNumber) {
        shortcut.setDescription(formatString(value));
        validateField(DESCR_DEFINITION, value, errors, rowNumber);
    }

    private void parseCategory(Shortcut shortcut, String value, List<String> errors, int rowNumber, List<
            Category> categories) {
        shortcut.setCategory(formatString(value));
        validateField(CATEGORY_DEFINITION, value, errors, rowNumber);
        if (value != null && !value.isEmpty()) {
            if (categories == null || !categories.stream().filter(r -> r.getName() != null && r.getName().trim().toUpperCase().equals(value.trim().toUpperCase()))
                    .findFirst().isPresent()) {
                errors.add(String.format("Row %s, Category %s does not exist", rowNumber, value));
            }
        }
    }

    private List<String> parseRoles(Shortcut shortcut, String value, List<String> errors, int rowNumber, Set<
            String> roleNamesSet) {
        List<String> roles;
        final List<String> parsed = new ArrayList<>();
        if (value == null || value.isEmpty()) {
            errors.add(String.format("Row %s, Shortcuts should be associated with the Role.", rowNumber));
        } else {
            roles = Stream.of(value.split(",")).map(elem -> new String(elem.trim()))
                    .collect(Collectors.toList());

            roles.forEach(r -> {
                if (roleNamesSet == null || !roleNamesSet.contains(r.toUpperCase().trim())) {
                    errors.add(String.format("Row %s, Role %s is not found in list of loaded roles", rowNumber, r));
                } else {
                    parsed.add(r.trim());
                }
            });

        }
        return parsed;
    }


    private void parseImageUrl(Shortcut shortcut, String value, List<String> errors, int rowNumber) {
        shortcut.setImageURL(value!=null?value.trim():null);
        validateField(IMAGE_URL_DEFINITION, value, errors, rowNumber);
    }


    private void parseTags(Shortcut shortcut, String value, List<String> errors, int rowNumber) {
        ParseResults<String> parseResults = super.parseTags(value, rowNumber);
        errors.addAll(parseResults.getErrors());
        shortcut.setTags(parseResults.getParsedData());
    }


}
