package com.syngenta.portal.data.service;

import com.syngenta.portal.data.model.FileParseResults;
import com.syngenta.portal.data.model.SeedsGatewayDataLoadSheets;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.SQLException;


@Service
public class DataLoadingService {

//    @Value("classpath:/Seeds_Gateway_Template_V3.2.1.xlsx")
//    Resource excelFile;
    @Autowired
    private ExcelReader excelReader;
    @Autowired
    private ScriptGenerator scriptGenerator;
    @Autowired
    private FlatObjectGenerator flatObjectGenerator;
    @Autowired
    private ExcelParser excelParser;

    private Logger logger = LoggerFactory.getLogger(DataLoadingService.class);


    public FileParseResults run(byte[] file) {
        FileParseResults results = new FileParseResults();
        Resource excelFile = new ByteArrayResource(file);
        logger.info("Running: ");
        Workbook workbook = excelReader.readExcelWorkBook(excelFile);
        SeedsGatewayDataLoadSheets sheets = excelParser.parseExcelWorkBook(workbook);

        if (sheets.isValid()) {
            results.setParsingSucceed(true);
            results.setScript(generateScript(sheets));
//            try {
//                ScriptUtils.executeSqlScript(dataSource.getConnection(),
//                        dropScript);
//                ScriptUtils.executeSqlScript(dataSource.getConnection(),
//                        createScript);
//                ScriptUtils.executeSqlScript(dataSource.getConnection(),
//                        new EncodedResource(new InputStreamResource(new FileInputStream(results.getScript()))));
//
//            } catch (IOException | SQLException e) {
//                e.printStackTrace();
//            }
            results.setFlatObjectScript(generateFlatObject(sheets));
        } else {
            sheets.printErrors();
            results.setErrorFile(generateErrors(sheets));
        }
        return results;
    }

    private File generateFlatObject(SeedsGatewayDataLoadSheets sheets) {
        BufferedWriter bw = null;
        File temp = null;
        try {
            temp = File.createTempFile("temp-flat-file-name", ".sql");
            bw = new BufferedWriter(new FileWriter(temp));
            for (String statement : flatObjectGenerator.generate(sheets)) {
                bw.write(statement + "\n");
            }
            System.out.println("Done");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(bw);
        }
        return temp;
    }

    private File generateScript(SeedsGatewayDataLoadSheets sheets) {
        BufferedWriter bw = null;
        File temp = null;
        try {
            temp = File.createTempFile("temp-script-file-name", ".sql");
            bw = new BufferedWriter(new FileWriter(temp));
            for (String statement : scriptGenerator.generate(sheets)) {
                bw.write(statement + "\n");
            }

            System.out.println("Done");

        } catch (IOException e) {

            e.printStackTrace();

        } finally {
            IOUtils.closeQuietly(bw);
        }
        return temp;
    }

    private File generateErrors(SeedsGatewayDataLoadSheets sheets) {
        BufferedWriter bw = null;
        File temp = null;
        try {
            temp = File.createTempFile("temp-errors", ".txt");
            bw = new BufferedWriter(new FileWriter(temp));
            for (String error : sheets.getErrors()) {
                bw.write(error + "\n");
            }

            System.out.println("Done");

        } catch (IOException e) {

            e.printStackTrace();

        } finally {
            IOUtils.closeQuietly(bw);
        }
        return temp;
    }
}
