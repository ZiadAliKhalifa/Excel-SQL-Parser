package com.syngenta.portal.data.model.shortcut;

import com.syngenta.portal.data.model.DataSheet;
import com.syngenta.portal.data.model.Link;
import com.syngenta.portal.data.model.SheetType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShortcutLinksDataSheet extends DataSheet {

    private Map<String, List<Link>> shortcutLinksMap = new HashMap<>();

    public Map<String, List<Link>> getShortcutLinksMap() {
        return shortcutLinksMap;
    }

    @Override
    public String getSheetName() {
        return SheetType.SHORTCUTS_LINKS.getSheetName();
    }

}
