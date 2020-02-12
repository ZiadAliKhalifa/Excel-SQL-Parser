package com.syngenta.portal.data.service;


import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author Riham Fayez
 */
@Service
public class ExcelReader {


    public Workbook readExcelWorkBook(org.springframework.core.io.Resource resource) {
        Workbook workbook = null;
        try {
            workbook = new XSSFWorkbook(resource.getInputStream());

        } catch (IOException e) {
            throw new RuntimeException("Error occurred while reading the Excel File.");
        }
        if (workbook == null) {
            throw new RuntimeException("no workbook is found.");
        }

        return workbook;

    }

}
