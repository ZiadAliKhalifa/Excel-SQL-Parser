package com.syngenta.portal.data.service.category;

import com.syngenta.portal.data.model.*;
import com.syngenta.portal.data.model.category.*;
import com.syngenta.portal.data.model.shortcut.ShortcutDataSheet;
import com.syngenta.portal.data.service.common.AbstractSheetParser;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CategoryLinksSheetParser extends AbstractSheetParser {
    private static FieldDefinition CATEGORY_NAME_DEFINITION =
            new FieldDefinition("Category Name", Boolean.TRUE, 50, FieldType.TEXT);
    private static FieldDefinition LINK_CATEGORY =
            new FieldDefinition("Parent Group", Boolean.FALSE, 50, FieldType.TEXT);
    private static FieldDefinition LINK_NAME =
            new FieldDefinition("Link Name", Boolean.TRUE, 50, FieldType.TEXT);
    private static FieldDefinition LINK_URL =
            new FieldDefinition("url", Boolean.TRUE, 2000, FieldType.TEXT);

    public CategoryLinksDataSheet parseDetailsSheet(Workbook workbook, DataSheet... referenceSheets) {
        Sheet categoryLinksDataSheet = readSheet(workbook, SheetType.CATEGORY_LINKS);
        CategoryDataSheet categoryDataSheet = (CategoryDataSheet) referenceSheets[0];
        return parseSheet(categoryLinksDataSheet, categoryDataSheet);
    }

    private CategoryLinksDataSheet parseSheet(Sheet sheet, CategoryDataSheet categoryDataSheet) {
        List<LinkTemp> tempParsedList = new ArrayList<>();
        // parsing rows
        rowsLoop:
        for (Row currentRow : sheet) {
            if (currentRow.getRowNum() == 0 || currentRow.getRowNum() == 1) {
                continue; // just skip the rows if row number is 0
            }
            LinkTemp temp = new LinkTemp(currentRow.getRowNum()+1);
            // parsing cells
            cellsLoop:
            for (Cell currentCell : currentRow) {
                String value = getCellValue(currentCell);
                switch (currentCell.getColumnIndex()) {
                    case 0:
                        temp.setUrlOwner(value);
                        break;
                    case 1:
                        temp.setCategory(value);
                        break;
                    case 2:
                        temp.setName(value);
                        break;
                    case 3:
                        temp.setUrl(value);
                        break;
                    default:
                        break cellsLoop;
                }
            }
            if (!isEmptyLink(temp)) {
                tempParsedList.add(temp);
            } else {
                break rowsLoop;
            }
        }

        return parse(tempParsedList, categoryDataSheet);
    }


    private CategoryLinksDataSheet parse(List<LinkTemp> tempParsedList, CategoryDataSheet categoryDataSheet) {
        List<String> errors = new ArrayList<>();
        String categoryName = "";
        CategoryLinksDataSheet categoryLinksDataSheet = new CategoryLinksDataSheet();
        for (LinkTemp row : tempParsedList) {
            Link link = new Link();
            categoryName = parseCategoryName(row.getUrlOwner(), errors, row.getRowNum(), categoryDataSheet.getCategoriesNameSet());
            parseLinkGroup(link, row.getCategory(), errors, row.getRowNum());
            parseLinkName(link, row.getName(), errors, row.getRowNum());
            parseUrl(link, row.getUrl(), errors, row.getRowNum());

            List<Link> links = categoryLinksDataSheet.getCategoryLinksMap().get(categoryName.toUpperCase().trim());
            if (links == null) {
                links = new ArrayList<>();
            }
            links.add(link);
            categoryLinksDataSheet.getCategoryLinksMap().put(categoryName.toUpperCase().trim(), links);
        }
        for (String key:categoryLinksDataSheet.getCategoryLinksMap().keySet()){
            Set<String> linkNames=new HashSet<String>();
            for(Link link:categoryLinksDataSheet.getCategoryLinksMap().get(key)){
                if((link.getName()!=null && !link.getName().isEmpty()) && linkNames.contains(link.getName().toUpperCase().trim())){
                    errors.add(String.format("Row %s, Link %s already exist for category %s", link.getName(), key));
                }else{
                    linkNames.add(link.getName().toUpperCase().trim());
                }
            }
        }


        if (errors.size() > 0) {
            categoryLinksDataSheet.getErrors().addAll(errors);
            categoryLinksDataSheet.setValidSheet(Boolean.FALSE);
        }
        return categoryLinksDataSheet;
    }


    private boolean isEmptyLink(LinkTemp link) {
        return StringUtils.isEmpty(link.getName()) && StringUtils.isEmpty(link.getUrl())
                && StringUtils.isEmpty(link.getCategory()) && StringUtils.isEmpty(link.getUrlOwner());

    }

    private String parseCategoryName(String value, List<String> errors, int rowNumber, Set<String> categoriesNames) {
        validateField(CATEGORY_NAME_DEFINITION, value, errors, rowNumber);
        if (value != null && !value.isEmpty() && !categoriesNames.isEmpty() && !categoriesNames.contains(value.trim().toUpperCase())) {
            errors.add(String.format("Row %s, Category name %s doest not exist", rowNumber, value));
        }

        return value != null ? value.trim() : "";
    }

    private void parseLinkGroup(Link link, String value, List<String> errors, int rowNumber) {
        link.setUrl(value!=null?value.trim():null);
        validateField(LINK_CATEGORY, value, errors, rowNumber);
    }

    private void parseLinkName(Link link, String value, List<String> errors, int rowNumber) {
        link.setName(value!=null?value.trim():null);
        validateField(LINK_NAME, value, errors, rowNumber);
    }


    private void parseUrl(Link link, String value, List<String> errors, int rowNumber) {
        link.setUrl(value);
        validateField(LINK_URL, value, errors, rowNumber);
    }


}
