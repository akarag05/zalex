package com.zalex.employee_certification.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class DatabaseTestController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/db")
    public ResponseEntity<Map<String, Object>> testDatabaseConnection() {
        Map<String, Object> response = new HashMap<>();
        
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            
            response.put("status", "SUCCESS");
            response.put("message", "Database connection successful!");
            response.put("databaseProductName", metaData.getDatabaseProductName());
            response.put("databaseProductVersion", metaData.getDatabaseProductVersion());
            response.put("driverName", metaData.getDriverName());
            response.put("driverVersion", metaData.getDriverVersion());
            response.put("url", metaData.getURL());
            response.put("username", metaData.getUserName());
            response.put("catalogName", connection.getCatalog());
            
            return ResponseEntity.ok(response);
        } catch (SQLException e) {
            response.put("status", "FAILED");
            response.put("message", "Database connection failed!");
            response.put("error", e.getMessage());
            response.put("errorCode", e.getSQLState());
            
            return ResponseEntity.status(500).body(response);
        }
    }
}


