package com.syngenta.portal.data.model.category;

import com.syngenta.portal.data.model.DataSheet;
import com.syngenta.portal.data.model.SheetType;

import java.util.*;


public class CategoryDataSheet extends DataSheet {

    public List<Category> categories = new ArrayList<>();


    public Set<String> categoriesNameSet = new HashSet<>();

    private Map<String, String> categoryUUIDMap = new HashMap<>();

    public List<Category> getCategories() {
        return categories;
    }

    public Map<String, String> getCategoryUUIDMap() {
        return categoryUUIDMap;
    }

    public Set<String> getCategoriesNameSet() {
        return categoriesNameSet;
    }

    public void setCategoriesNameSet(Set<String> categoriesNameSet) {
        this.categoriesNameSet = categoriesNameSet;
    }

    @Override
    public String getSheetName() {
        return SheetType.CATEGORY.getSheetName();
    }

}
