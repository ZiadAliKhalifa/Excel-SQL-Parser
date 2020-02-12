package com.syngenta.portal.data.model;

/**
 * @author Riham Fayez
 */
public class FieldDefinition {

    private String title;
    private boolean required;
    private Integer maxLength;
    private FieldType fieldType;
    private Integer maxValue;
    private Integer minValue;


    public FieldDefinition(String title, boolean required, FieldType fieldType) {
        this.title = title;
        this.required = required;
        this.fieldType = fieldType;
    }

    public FieldDefinition(String title, boolean required, Integer maxLength, FieldType fieldType) {
        this(title, required, fieldType);
        this.maxLength = maxLength;
    }


    public FieldDefinition(String title, boolean required, FieldType fieldType, Integer maxValue, Integer
            minValue) {
        this(title, required, fieldType);
        this.maxValue = maxValue;
        this.minValue = minValue;
    }

    public boolean isRequired() {
        return required;
    }

    public Integer getMaxLength() {
        return maxLength;
    }


    public FieldType getFieldType() {
        return fieldType;
    }

    public String getTitle() {
        return title;
    }

    public Integer getMaxValue() {
        return maxValue;
    }

    public Integer getMinValue() {
        return minValue;
    }
}
