package com.syngenta.portal.data.model;

import java.util.ArrayList;
import java.util.List;

public class ParseResults<T> {
    private T parsedData;
    private List<String> errors = new ArrayList<String>();

    public T getParsedData() {
        return parsedData;
    }

    public void setParsedData(T parsedData) {
        this.parsedData = parsedData;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }


}
