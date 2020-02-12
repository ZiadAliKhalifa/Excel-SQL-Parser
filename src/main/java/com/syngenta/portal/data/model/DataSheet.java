package com.syngenta.portal.data.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Riham Fayez
 */
public abstract class DataSheet {
    private List<String> errors = new ArrayList<>();

    public void setValidSheet(boolean validSheet) {
        this.validSheet = validSheet;
    }

    private boolean validSheet = Boolean.TRUE;


    public List<String> getErrors() {
        return errors;
    }

    public boolean isValidSheet() {
        return validSheet;
    }

    public abstract String getSheetName();

    public void printErrors() {
        System.out.println("----------------------------");
        System.out.println(this.getSheetName() + " Error List:");
        System.out.println("----------------------------");

        if (!isValidSheet()) {
            for (String error : getErrors()) {
                System.out.println(error);
            }
        } else {
            System.out.println("No Errors found");
        }
    }


}
