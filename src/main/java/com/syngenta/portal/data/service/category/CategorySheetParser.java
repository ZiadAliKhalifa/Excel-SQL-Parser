package com.syngenta.portal.data.service.category;

import com.syngenta.portal.data.model.*;
import com.syngenta.portal.data.model.category.Category;
import com.syngenta.portal.data.model.category.CategoryDataSheet;
import com.syngenta.portal.data.model.category.CategoryTemp;
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

@Service
public class CategorySheetParser extends AbstractSheetParser {
    private static FieldDefinition CATEGORY_NAME_DEFINITION =
            new FieldDefinition("Category Name", Boolean.TRUE, 35, FieldType.TEXT);
    private static FieldDefinition CATEGORY_DESCR_DEFINITION =
            new FieldDefinition("Category Description", Boolean.FALSE, 500, FieldType.TEXT);
    private static FieldDefinition COLOR_DEFINITION =
            new FieldDefinition("Color", Boolean.FALSE, 20, FieldType.TEXT);
    private static FieldDefinition IMAGE_URL_DEFINITION =
            new FieldDefinition("Image url", Boolean.FALSE, 2000, FieldType.TEXT);

    public CategoryDataSheet parseDetailsSheet(Workbook workbook, DataSheet... referenceSheets) {
        Sheet categoryDataSheet = readSheet(workbook, SheetType.CATEGORY);

        return parseSheet(categoryDataSheet);
    }

    private CategoryDataSheet parseSheet(Sheet sheet) {

        List<CategoryTemp> tempParsedList = new ArrayList<>();

        // parsing rows
        rowsLoop:
        for (Row currentRow : sheet) {
            if (currentRow.getRowNum() == 0 || currentRow.getRowNum() == 1) {
                continue; // just skip the rows if row number is 0
            }
            CategoryTemp temp = new CategoryTemp(currentRow.getRowNum() + 1);

            // parsing cells
            cellsLoop:
            for (Cell currentCell : currentRow) {
                String value = getCellValue(currentCell);

                switch (currentCell.getColumnIndex()) {
                    case 0:
                        temp.setName(value);
                        break;
                    case 1:
                        temp.setDescription(value);
                        break;
                    case 2:
                        temp.setTags(value);

                        break;
                    case 3:
                        temp.setImageURL(value);
                        break;
                    case 4:
                        temp.setColor(value);
                        break;
                    default:
                        break cellsLoop;
                }

            }

            if (!isEmptyCategory(temp)) {
                tempParsedList.add(temp);


            } else {
                break rowsLoop;
            }
        }


        return parse(tempParsedList);
    }

    private CategoryDataSheet parse(List<CategoryTemp> tempParsedList) {
        Set<String> categorySet = new HashSet<>();
        List<String> errors = new ArrayList<>();

        CategoryDataSheet categoryDataSheet = new CategoryDataSheet();
        for (CategoryTemp row : tempParsedList) {
            Category category = new Category();
            parseCategoryName(category, row.getName(), errors, row.getRowNum(), categorySet);
            parseDescription(category, row.getDescription(), errors, row.getRowNum());
            parseTags(category, row.getTags(), errors, row.getRowNum());
            parseImageUrl(category, row.getImageURL(), errors, row.getRowNum());
            parseColor(category, row.getColor(), errors, row.getRowNum());
            categoryDataSheet.getCategories().add(category);
        }


        if (errors.size() > 0) {
            categoryDataSheet.getErrors().addAll(errors);
            categoryDataSheet.setValidSheet(Boolean.FALSE);
        }
        categoryDataSheet.setCategoriesNameSet(categorySet);
        return categoryDataSheet;
    }


    private boolean isEmptyCategory(CategoryTemp category) {
        return StringUtils.isEmpty(category.getDescription()) && StringUtils.isEmpty(category.getName())
                && StringUtils.isEmpty(category.getColor())
                && StringUtils.isEmpty(category.getImageURL())
                && StringUtils.isEmpty(category.getTags());

    }

    private void parseCategoryName(Category category, String value, List<String> errors, int rowNumber, Set<String> categorySet) {
        category.setName(formatString(value));
        validateField(CATEGORY_NAME_DEFINITION, value, errors, rowNumber);
        if (value != null && !value.isEmpty() && !categorySet.add(value.trim().toUpperCase())) {
            errors.add(String.format("Row %s, Category %s already exist", rowNumber, value));
        }
    }

    private void parseColor(Category category, String value, List<String> errors, int rowNumber) {
        category.setColor(formatString(value));
        validateField(COLOR_DEFINITION, value, errors, rowNumber);
    }

    private void parseDescription(Category category, String value, List<String> errors, int rowNumber) {
        category.setDescription(formatString(value));
        validateField(CATEGORY_DESCR_DEFINITION, value, errors, rowNumber);
    }

    private void parseImageUrl(Category category, String value, List<String> errors, int rowNumber) {
        category.setImageURL(formatString(value));
        validateField(IMAGE_URL_DEFINITION, value, errors, rowNumber);
    }


    private void parseTags(Category category, String value, List<String> errors, int rowNumber) {
        ParseResults<String> parseResults = super.parseTags(value, rowNumber);
        errors.addAll(parseResults.getErrors());
        category.setTags(parseResults.getParsedData());
    }

}
