package com.syngenta.portal.data.service.common;

import com.syngenta.portal.data.model.FieldDefinition;
import com.syngenta.portal.data.model.FieldType;
import com.syngenta.portal.data.model.ParseResults;
import com.syngenta.portal.data.model.SheetType;
import com.syngenta.portal.data.model.category.Category;
import com.syngenta.portal.data.service.SheetParser;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Riham Fayez
 */
public abstract class AbstractSheetParser implements SheetParser{
    private static FieldDefinition TAGS_DEFINITION =
            new FieldDefinition("Tags", Boolean.FALSE, FieldType.TEXT);
    private static int TAGS_COUNT = 10;
    private static int TAG_LENGTH = 35;

    public static boolean isNumber(String string) {
        try {
            Long.parseLong(string);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public Sheet readSheet(Workbook workbook, SheetType sheetType) {

        Sheet sheet = workbook.getSheet(sheetType.getSheetName());
        if (sheet == null) {
            throw new RuntimeException("no sheet called " + sheetType.getSheetName() + " is found.");
        }
        return sheet;
    }

    public String getCellValue(Cell currentCell) {
        String cellValue = null;

        if (currentCell == null) return "";

        currentCell.setCellType(CellType.STRING);
        cellValue = currentCell.getStringCellValue();

        if (cellValue != null) {
            cellValue = cellValue.trim();
//            if (cellValue.equals("-")) {
//                cellValue = cellValue.replace("-", "");
//            }
        }
        return cellValue;
    }

    public void validateField(FieldDefinition fieldDefinition, String value, int rowNum) {
        if (validateRequiredField(fieldDefinition, value)) {
            throw new ParserBusinessException("Row " + rowNum + ", " + fieldDefinition.getTitle() + " is empty.");
        } else if (validateMaximumLength(fieldDefinition, value)) {
            throw new ParserBusinessException("Row " + rowNum + ", " + fieldDefinition.getTitle() + " length is greater" +
                    " than expected " + fieldDefinition.getMaxLength());
        } else if (validateFieldType(fieldDefinition, value)) {
            throw new ParserBusinessException("Row " + rowNum + ", " + fieldDefinition.getTitle() + " invalid field " +
                    "type");
        } else if (validateMinimumValue(fieldDefinition, value)) {
            throw new ParserBusinessException("Row " + rowNum + ", " + fieldDefinition.getTitle() + " value less " +
                    "than applicable Minimum value " + fieldDefinition.getMinValue());
        } else if (validateMaximumValue(fieldDefinition, value)) {
            throw new ParserBusinessException("Row " + rowNum + ", " + fieldDefinition.getTitle() + " value less " +
                    "than applicable Maximum value " + fieldDefinition.getMaxValue());
        }
    }

    private boolean validateRequiredField(FieldDefinition fieldDefinition, String value) {
        return fieldDefinition.isRequired() && isEmpty(value);
    }

    private boolean validateMaximumLength(FieldDefinition fieldDefinition, String value) {
        return fieldDefinition.getMaxLength() != null && value != null && value.length() > fieldDefinition.getMaxLength();
    }

    private boolean validateFieldType(FieldDefinition fieldDefinition, String value) {
        if (FieldType.NUMERIC.equals(fieldDefinition.getFieldType())) {
            return !isNumber(value);
        }
        return false;
    }

    private boolean validateMinimumValue(FieldDefinition fieldDefinition, String value) {

        return fieldDefinition.getMinValue() != null && Integer.valueOf(value).compareTo(fieldDefinition.getMinValue()) < 0;
    }

    private boolean validateMaximumValue(FieldDefinition fieldDefinition, String value) {
        return fieldDefinition.getMaxValue() != null && Integer.valueOf(value).compareTo(fieldDefinition.getMaxValue()) > 0;

    }

    private boolean isEmpty(String value) {
        return value == null || value.length() == 0;

    }

    protected void validateField(FieldDefinition fieldDefinition, String value, List<String> errors, int rowNumber) {
        try {
            validateField(fieldDefinition, value, rowNumber);
        } catch (ParserBusinessException ex) {
            errors.add(ex.getMessage());
        }
    }

    protected String formatString(String value){
        return value!=null?value.trim():null;
    }

    protected ParseResults<String> parseTags(String value, int rowNumber) {
        ParseResults<String> parseResults = new ParseResults<>();
        validateField(TAGS_DEFINITION, value, parseResults.getErrors(), rowNumber);

        Set<String> tagSet=new HashSet<>();

        List<String> tagsString = new ArrayList<>();
        if (value != null && !value.trim().isEmpty()) {
            //split by comma
            List<String> tags = Stream.of(value.split(","))
                    .map(elem -> new String(formatString(elem)))
                    .collect(Collectors.toList());
            if (tags.size() > TAGS_COUNT) {
                parseResults.getErrors().add(String.format("Row %s, Tags count is greater than %s", rowNumber,TAGS_COUNT));
            } else {
                tags.stream().forEach(tag -> {
                    if ((tag.length() > TAG_LENGTH)) {
                        parseResults.getErrors().add(String.format("Row %s, Tag %s length is greater than %s", rowNumber, tag,TAG_LENGTH));
                    } else if(tagSet.contains(tag.trim().toUpperCase())){
                        parseResults.getErrors().add(String.format("Row %s, Tag %s already exist", rowNumber, tag));
                    }else {
                        tagsString.add(tag.trim());
                        tagSet.add(tag.trim().toUpperCase());
                    }
                });

            }
        }
        parseResults.setParsedData(String.join(",", tagsString));
        return parseResults;
    }

}
