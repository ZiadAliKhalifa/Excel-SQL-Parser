package com.syngenta.portal.data.model.category;

import com.syngenta.portal.data.model.DataSheet;
import com.syngenta.portal.data.model.Link;
import com.syngenta.portal.data.model.SheetType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CategoryLinksDataSheet extends DataSheet {

    private Map<String, List<Link>> categoryLinksMap = new HashMap<>();

    public Map<String, List<Link>> getCategoryLinksMap() {
        return categoryLinksMap;
    }

    @Override
    public String getSheetName() {
        return SheetType.CATEGORY_LINKS.getSheetName();
    }

}
