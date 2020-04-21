package com.syngenta.portal.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

@Service
public class ScriptExecutor {
    @Value("classpath:/drop.sql")
    Resource dropScript;

    @Value("classpath:/createTables.sql")
    Resource createScript;

    @Autowired
    private DataSource dataSource;

    public boolean execute(File script) {
        dropTables();
        createTables();
        return executeScript(script);
    }

    public boolean dropTables() {
        try {
            ScriptUtils.executeSqlScript(dataSource.getConnection(),
                    dropScript);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean createTables() {
        try {
            ScriptUtils.executeSqlScript(dataSource.getConnection(),
                    createScript);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean executeScript(File script) {
        try {
            ScriptUtils.executeSqlScript(dataSource.getConnection(),
                    new EncodedResource(new InputStreamResource(new FileInputStream(script))));

        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
