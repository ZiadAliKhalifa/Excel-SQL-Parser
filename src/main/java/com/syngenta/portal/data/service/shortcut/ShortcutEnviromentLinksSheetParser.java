package com.syngenta.portal.data.service.shortcut;

import com.syngenta.portal.data.model.*;
import com.syngenta.portal.data.model.category.CategoryDataSheet;
import com.syngenta.portal.data.model.category.CategoryLinksDataSheet;
import com.syngenta.portal.data.model.category.LinkTemp;
import com.syngenta.portal.data.model.shortcut.Shortcut;
import com.syngenta.portal.data.model.shortcut.ShortcutDataSheet;
import com.syngenta.portal.data.model.shortcut.ShortcutEnviromentLinksDataSheet;
import com.syngenta.portal.data.model.shortcut.ShortcutLinksDataSheet;
import com.syngenta.portal.data.service.common.AbstractSheetParser;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class ShortcutEnviromentLinksSheetParser extends AbstractSheetParser {
    private static FieldDefinition SHORTCUT_NAME_DEFINITION =
            new FieldDefinition("Shortcut Name", Boolean.TRUE, 50, FieldType.TEXT);
    private static FieldDefinition LINK_NAME =
            new FieldDefinition("Link Name", Boolean.TRUE, 50, FieldType.TEXT);
    private static FieldDefinition LINK_URL =
            new FieldDefinition("url", Boolean.TRUE, 2000, FieldType.TEXT);

    public ShortcutEnviromentLinksDataSheet parseDetailsSheet(Workbook workbook, DataSheet... referenceSheets) {
        Sheet ShortcutEnviromentLinksDataSheet = readSheet(workbook, SheetType.SHORTCUTS_ENVIRONMENT_LINKS);
        ShortcutDataSheet shortcutDataSheet = (ShortcutDataSheet) referenceSheets[0];
        return parseSheet(ShortcutEnviromentLinksDataSheet, shortcutDataSheet);
    }

    private ShortcutEnviromentLinksDataSheet parseSheet(Sheet sheet, ShortcutDataSheet shortcutDataSheet) {
        List<LinkTemp> tempParsedList = new ArrayList<>();
        // parsing rows
        rowsLoop:
        for (Row currentRow : sheet) {
            if (currentRow.getRowNum() == 0 || currentRow.getRowNum() == 1) {
                continue; // just skip the rows if row number is 0
            }
            LinkTemp temp = new LinkTemp(currentRow.getRowNum() + 1);
            // parsing cells
            cellsLoop:
            for (Cell currentCell : currentRow) {
                String value = getCellValue(currentCell);
                switch (currentCell.getColumnIndex()) {
                    case 0:
                        temp.setUrlOwner(value);
                        break;
                    case 1:
                        temp.setName(value);
                        break;
                    case 2:
                        temp.setUrl(value);
                        break;
                    case 3:
                        temp.setOpenInIframe(value);
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

        return parse(tempParsedList, shortcutDataSheet);
    }

    private ShortcutEnviromentLinksDataSheet parse(List<LinkTemp> tempParsedList, ShortcutDataSheet shortcutDataSheet) {
        List<String> errors = new ArrayList<>();
        ShortcutEnviromentLinksDataSheet shortcutEnvironmentDataSheet = new ShortcutEnviromentLinksDataSheet();
        String shortcutName = "";
        for (LinkTemp row : tempParsedList) {
            Link link = new Link();
            link.setCategory("Environments");
            shortcutName = parseModuleName(row.getUrlOwner(), errors, row.getRowNum(), shortcutDataSheet.getShortcutsMap());
            parseLinkName(link, row.getName(), errors, row.getRowNum());
            parseUrl(link, row.getUrl(), errors, row.getRowNum());
            parseIFrame(link, row.getOpenInIframe(), errors, row.getRowNum());

            String formattedShortcutName = shortcutName.toUpperCase().trim();
            List<Link> links = shortcutEnvironmentDataSheet.getShortcutLinksMap().get(shortcutName.toUpperCase().trim());
            if (links == null) {
                links = new ArrayList<>();
            }
            links.add(link);
            shortcutEnvironmentDataSheet.getShortcutLinksMap().put(formattedShortcutName, links);
        }

        for (String key : shortcutEnvironmentDataSheet.getShortcutLinksMap().keySet()) {
            Set<String> linkNames = new HashSet<String>();
            for (Link link : shortcutEnvironmentDataSheet.getShortcutLinksMap().get(key)) {
                if ((link.getName() != null && !link.getName().isEmpty()) && linkNames.contains(link.getName().toUpperCase().trim())) {
                    errors.add(String.format("Environment Link %s already exist for shortcut %s", link.getName(), key));
                } else {
                    linkNames.add(link.getName().toUpperCase().trim());
                }
            }
        }

        for (Shortcut shortcut : shortcutDataSheet.getShortcuts()) {
            String defaultEnv = shortcut.getDefaultEnviroment();
            if(defaultEnv!=null && !defaultEnv.isEmpty() && shortcut.getName()!=null && !shortcut.getName().isEmpty()) {
                boolean found = false;
                if(shortcutEnvironmentDataSheet.getShortcutLinksMap().get(shortcut.getName().toUpperCase().trim())!=null) {
                    for (Link link : shortcutEnvironmentDataSheet.getShortcutLinksMap().get(shortcut.getName().toUpperCase().trim())) {
                        if (link.getName().toUpperCase().trim().equals(defaultEnv.toUpperCase().trim())) {
                            found = true;
                            break;
                        }
                    }
                }
                if(!found){
                    errors.add(String.format("Default Environment for shortcut %s does not exist in list of environments", shortcut.getName()));
                }
            }
        }

        if (errors.size() > 0) {
            shortcutEnvironmentDataSheet.getErrors().addAll(errors);
            shortcutEnvironmentDataSheet.setValidSheet(Boolean.FALSE);
        }
        return shortcutEnvironmentDataSheet;
    }

    private void parseIFrame(Link link, String value, List<String> errors, int rowNumber) {
        boolean iframe = true;
        if (value != null && !value.isEmpty() && (value.toUpperCase().equals("FALSE") || value.toUpperCase().equals("NO"))) {
            iframe = false;
        }
        link.setOpenInIframe(iframe);
    }

    private boolean isEmptyLink(LinkTemp link) {
        return StringUtils.isEmpty(link.getName()) && StringUtils.isEmpty(link.getUrl())
                && StringUtils.isEmpty(link.getCategory()) && StringUtils.isEmpty(link.getUrlOwner());

    }

    private String parseModuleName(String value, List<String> errors, int rowNumber, Map<String, Shortcut> shortcuts) {
        validateField(SHORTCUT_NAME_DEFINITION, value, errors, rowNumber);
        if (value != null && !value.isEmpty() && !shortcuts.isEmpty() && shortcuts.get(value.trim().toUpperCase()) == null) {
            errors.add(String.format("Row %s, Shortcut name %s doest not exist", rowNumber, value));
        }

        return value != null ? value.trim() : "";
    }

    private void parseLinkName(Link link, String value, List<String> errors, int rowNumber) {
        link.setName(value!=null?value.trim():null);
        validateField(LINK_NAME, value, errors, rowNumber);
    }


    private void parseUrl(Link link, String value, List<String> errors, int rowNumber) {
        link.setUrl(value!=null?value.trim():null);
        validateField(LINK_URL, value, errors, rowNumber);
    }


}
