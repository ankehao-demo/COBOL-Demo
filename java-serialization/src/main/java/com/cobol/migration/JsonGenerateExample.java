package com.cobol.migration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class JsonGenerateExample {
    
    public static void main(String[] args) {
        try {
            String name = "Test Name";
            String value = "Test Value";
            String blank = " ";
            String enabled = "true";
            
            Map<String, Object> recordMap = new LinkedHashMap<>();
            recordMap.put("name", name);
            recordMap.put("value", value);
            recordMap.put("ws-record-blank", blank);
            recordMap.put("enabled", enabled);
            
            Map<String, Object> rootMap = new LinkedHashMap<>();
            rootMap.put("ws-record", recordMap);
            
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
            
            String jsonOutput = objectMapper.writeValueAsString(rootMap);
            
            int jsonCharCount = jsonOutput.length();
            
            System.out.println("JSON document successfully generated.");
            System.out.println("Generated JSON for record: " + 
                padRight(name, 10) + " " + 
                padRight(value, 10) + " " + 
                padRight(blank, 10) + " " + 
                enabled + " ");
            System.out.println("----------------------------");
            System.out.println(jsonOutput);
            System.out.println("----------------------------");
            System.out.println("JSON output character count: " + String.format("%04d", jsonCharCount));
            System.out.println("Done.");
            
        } catch (Exception e) {
            System.err.println("Error generating JSON error: " + e.getMessage());
            System.exit(1);
        }
    }
    
    private static String padRight(String s, int n) {
        if (s == null) {
            s = "";
        }
        if (s.length() >= n) {
            return s.substring(0, n);
        }
        return String.format("%-" + n + "s", s);
    }
}
