package com.sparta.utilities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class DataLoader {
    public static ObjectMapper mapper = new ObjectMapper();

    public static void setMapper(ObjectMapper customMapper) {
        mapper = customMapper;
    }

    public static Map<String, Object> getTestData(String key, String fileName) {
        try {
            File file = new File("src/test/resources/testdata/" + fileName);
            JsonNode rootNode = mapper.readTree(file);
            JsonNode keyNode = rootNode.get(key);
            return mapper.convertValue(keyNode, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load test data: " + e.getMessage());
        }
    }
    }