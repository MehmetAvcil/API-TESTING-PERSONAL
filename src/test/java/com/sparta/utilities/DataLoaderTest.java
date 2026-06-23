package com.sparta.utilities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataLoaderTest {

    @Mock
    private ObjectMapper mockMapper;

    @Mock
    private JsonNode mockRootNode;

    @Mock
    private JsonNode mockKeyNode;

    @BeforeEach
    void setUp() {
        DataLoader.setMapper(mockMapper);
    }

    @AfterEach
    void tearDown() {
        DataLoader.setMapper(new ObjectMapper());
    }

    @Test
    void getTestData_validKeyReturnsExpectedMap() throws Exception {
        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("id", 1);
        expectedMap.put("expectedStatusCode", 200);

        when(mockMapper.readTree(any(File.class))).thenReturn(mockRootNode);
        when(mockRootNode.get("valid_id_1")).thenReturn(mockKeyNode);
        when(mockMapper.convertValue(mockKeyNode, Map.class)).thenReturn(expectedMap);

        Map<String, Object> actualData = DataLoader.getTestData("valid_id_1", "courses_data.json");

        MatcherAssert.assertThat(actualData, Matchers.notNullValue());
        MatcherAssert.assertThat(actualData.get("id"), Matchers.is(1));
        MatcherAssert.assertThat(actualData.get("expectedStatusCode"), Matchers.is(200));
        verify(mockMapper).readTree(any(File.class));
        verify(mockRootNode).get("valid_id_1");
    }

    @Test
    void getTestData_invalidKeyReturnsNull() throws Exception {
        when(mockMapper.readTree(any(File.class))).thenReturn(mockRootNode);
        when(mockRootNode.get("invalid_key")).thenReturn(null);
        when(mockMapper.convertValue(null, Map.class)).thenReturn(null);

        Map<String, Object> actualData = DataLoader.getTestData("invalid_key", "courses_data.json");

        MatcherAssert.assertThat(actualData, Matchers.nullValue());
    }

    @Test
    void getTestData_fileReadFailureThrowsRuntimeException() throws Exception {
        when(mockMapper.readTree(any(File.class))).thenThrow(new IOException("File not found"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                DataLoader.getTestData("missing_file.json", "any_key")
        );

        MatcherAssert.assertThat(exception.getMessage(), Matchers.containsString("Failed to load test data"));
    }
}