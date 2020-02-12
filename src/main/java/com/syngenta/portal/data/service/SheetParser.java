package com.syngenta.portal.data.service;

import com.syngenta.portal.data.model.DataSheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * @author Riham Fayez
 */
public interface SheetParser {

    DataSheet parseDetailsSheet(Workbook workbook, DataSheet... referenceSheets);

}
