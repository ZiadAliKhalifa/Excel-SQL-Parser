package com.syngenta.portal.data.service.user;

import com.syngenta.portal.data.model.*;
import com.syngenta.portal.data.model.role.Role;
import com.syngenta.portal.data.model.role.RoleDataSheet;
import com.syngenta.portal.data.model.shortcut.Shortcut;
import com.syngenta.portal.data.model.user.User;
import com.syngenta.portal.data.model.user.UserDataSheet;
import com.syngenta.portal.data.model.user.UserTemp;
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
public class UserSheetParser extends AbstractSheetParser {
    private static FieldDefinition FIRST_NAME_DEFINITION =
            new FieldDefinition("First Name", Boolean.TRUE, 100, FieldType.TEXT);

    private static FieldDefinition LAST_NAME_DEFINITION =
            new FieldDefinition("Last Name", Boolean.TRUE, 100, FieldType.TEXT);

    private static FieldDefinition EMAIL_DEFINITION =
            new FieldDefinition("email", Boolean.TRUE, 100, FieldType.TEXT);
    private static FieldDefinition IMAGE_URL_DEFINITION =
            new FieldDefinition("Image url", Boolean.FALSE, 2000, FieldType.TEXT);
    private static FieldDefinition TACCOUNT_DEFINITION =
            new FieldDefinition("t-account", Boolean.TRUE, 50, FieldType.TEXT);
    private static FieldDefinition REGION_DEFINITION =
            new FieldDefinition("region ", Boolean.FALSE, 50, FieldType.TEXT);
    private static FieldDefinition BUSSINESSuNIT_DEFINITION =
            new FieldDefinition("Bussiness unit", Boolean.FALSE, 50, FieldType.TEXT);
    private static FieldDefinition ROLESDEFINITION =
            new FieldDefinition("roles", Boolean.TRUE, 5000, FieldType.TEXT);



    public UserDataSheet parseDetailsSheet(Workbook workbook, DataSheet... referenceSheets) {
        Sheet userDataSheet = readSheet(workbook, SheetType.USERS);
        RoleDataSheet roleDataSheet = (RoleDataSheet) referenceSheets[0];
        return parseSheet(userDataSheet, roleDataSheet);
    }

    private UserDataSheet parseSheet(Sheet sheet, RoleDataSheet roleDataSheet) {

        List<UserTemp> parsedResults = new ArrayList<>();
        // parsing rows
        rowsLoop:
        for (Row currentRow : sheet) {
            if (currentRow.getRowNum() == 0 || currentRow.getRowNum() == 1) {
                continue; // just skip the rows if row number is 0
            }
            UserTemp user = new UserTemp(currentRow.getRowNum() + 1);
            // parsing cells
            cellsLoop:
            for (Cell currentCell : currentRow) {
                String value = getCellValue(currentCell);
                switch (currentCell.getColumnIndex()) {
                    case 0:
                        user.setFirstName(value);
                        break;
                    case 1:
                        user.setLastName(value);
                        break;
                    case 2:
                        user.setEmail(value);
                        break;
                    case 3:
                        user.setTaccount(value);
                        break;
                    case 4:
                        user.setImageURL(value);
                    case 5:
                        user.setRegion(value);
                        break;
                    case 6:
                        user.setBusinessUnit(value);
                        break;
                    case 7:
                        user.setTags(value);
                        break;
                    case 8:
                        user.setRoles(value);
                        break;
                    default:
                        break cellsLoop;
                }

            }
            if (!isEmpty(user)) {
                parsedResults.add(user);
            } else {
                break rowsLoop;
            }
        }

        return parseSheet(parsedResults, roleDataSheet);
    }

    private UserDataSheet parseSheet(List<UserTemp> parsedResults, RoleDataSheet roleDataSheet) {
        Set<String> emailSet = new HashSet<>();
        Set<String> tAccountSet = new HashSet<>();
        List<String> errors = new ArrayList<>();
        UserDataSheet dataSheet = new UserDataSheet();
        // parsing rows
        for (UserTemp row : parsedResults) {
            User user = new User();

            parseFirstName(user, row.getFirstName(), errors, row.getRowNum());

            parseLastName(user, row.getLastName(), errors, row.getRowNum());

            parseEmail(user, row.getEmail(), errors, row.getRowNum(),emailSet);

            parseTaccount(user, row.getTaccount(), errors, row.getRowNum(),tAccountSet);

            parseRegion(user, row.getRegion(), errors, row.getRowNum());

            parseBusinessUnit(user, row.getBusinessUnit(), errors, row.getRowNum());

            parseTags(user, row.getTags(), errors, row.getRowNum());

            parseRoles(user, row.getRoles(), errors, row.getRowNum(), roleDataSheet.getRoles());

            dataSheet.getUsers().add(user);
            if (user.getTaccount() != null) {
                dataSheet.getUsersTaccounts().add(user.getTaccount().toUpperCase().trim());
            }

        }


        if (errors.size() > 0) {
            dataSheet.getErrors().addAll(errors);
            dataSheet.setValidSheet(Boolean.FALSE);
        }
        return dataSheet;
    }


    private boolean isEmpty(UserTemp user) {
        return StringUtils.isEmpty(user.getFirstName()) && StringUtils.isEmpty(user.getLastName()) && StringUtils.isEmpty(user.getEmail())
                && StringUtils.isEmpty(user.getImageURL()) && StringUtils.isEmpty(user.getTaccount()) && StringUtils.isEmpty(user.getBusinessUnit())
                && StringUtils.isEmpty(user.getRegion())
                && StringUtils.isEmpty(user.getTaccount()) && (user.getTags() == null || user.getTags().isEmpty());

    }

    private void parseFirstName(User user, String value, List<String> errors, int rowNumber) {
        user.setFirstName(formatString(value));
        validateField(FIRST_NAME_DEFINITION, value, errors, rowNumber);
    }

    private void parseLastName(User user, String value, List<String> errors, int rowNumber) {
        user.setLastName(formatString(value));
        validateField(LAST_NAME_DEFINITION, value, errors, rowNumber);
    }

    private void parseEmail(User user, String value, List<String> errors, int rowNumber,Set<String> emailSet) {

        user.setEmail(formatString(value));
        validateField(EMAIL_DEFINITION, value, errors, rowNumber);
        if (value != null && !value.isEmpty()) {
            if (emailSet.contains(value.toUpperCase().trim())) {
                errors.add(String.format("Row %s, email %s already exist", rowNumber, value));
            } else {
                emailSet.add(value.toUpperCase().trim());
            }
        }
    }

    private void parseTaccount(User user, String value, List<String> errors, int rowNumber,Set<String> tAccountSet) {

        user.setTaccount(formatString(value));
        validateField(TACCOUNT_DEFINITION, value, errors, rowNumber);
        if (value != null && !value.isEmpty()) {
            if (tAccountSet.contains(value.toUpperCase().trim())) {
                errors.add(String.format("Row %s, taccount %s already exist", rowNumber, value));
            } else {
                tAccountSet.add(value.toUpperCase().trim());
            }
        }
    }

    private void parseRegion(User user, String value, List<String> errors, int rowNumber) {
        user.setRegion(formatString(value));
        validateField(REGION_DEFINITION, value, errors, rowNumber);
    }

    private void parseBusinessUnit(User user, String value, List<String> errors, int rowNumber) {
        user.setBusinessUnit(formatString(value));
        validateField(BUSSINESSuNIT_DEFINITION, value, errors, rowNumber);
    }


    private void parseRoles(User user, String value, List<String> errors, int rowNumber, List<Role> roles) {
        validateField(ROLESDEFINITION, value, errors, rowNumber);
        List<String> rolesStr = new ArrayList<>();
        if (value != null && !value.trim().isEmpty()) {
            //split by comma
            List<String> rolesSTR = Stream.of(value.split(","))
                    .map(elem -> new String(formatString(elem)))
                    .collect(Collectors.toList());
            rolesSTR.stream().forEach(rr -> {
                if (!roles.stream().filter(r -> r.getName().trim().toUpperCase().equals(rr.trim().toUpperCase()))
                        .findFirst().isPresent()) {
                    errors.add(String.format("Row %s, Role %s does not exist in list of roles", rowNumber, rr));
                } else {
                    rolesStr.add(rr.trim());
                }
            });


        }
        user.setRoles(rolesStr);
    }

    private void parseTags(User user, String value, List<String> errors, int rowNumber) {
        ParseResults<String> parseResults = super.parseTags(value, rowNumber);
        errors.addAll(parseResults.getErrors());
        user.setTags(parseResults.getParsedData());
    }
}
