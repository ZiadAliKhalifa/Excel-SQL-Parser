package com.syngenta.portal.data.service;

import com.syngenta.portal.data.model.DataSheet;

import java.util.List;


public interface SheetScriptGenerator<T extends DataSheet> {

    List<String> generate(T dataSheet, DataSheet... referenceSheets);

    public static String formatForSql(String str){
        String formatted=null;
        if(str!=null){
            formatted=  str.trim().replace("'","''");
        }
        return formatted;
    }

    public static String trim(String str){
        return str!=null?str.trim():null;
    }
}
