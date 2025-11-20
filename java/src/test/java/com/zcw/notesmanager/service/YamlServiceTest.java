package com.zcw.notesmanager.service;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class YamlServiceTest {

    @Test
    void testParseSimpleYamlHeader() {
        String yamlHeader = """
                title: My Test Note
                created: 2025-11-18T10:30:00Z
                modified: 2025-11-18T10:45:00Z
                """;
        
        YamlService yamlService = new YamlService();
        
        Map<String, Object> result = yamlService.parseYaml(yamlHeader);
        
        
        assertEquals("My Test Note", result.get("title"));
        assertNotNull(result.get("created"));
        assertNotNull(result.get("modified"));
    }

    @Test
    void testParseYamlWithTags() {
        String yamlHeader = """
            title: Test Note
            created: 2025-11-18T10:30:00Z
            modified: 2025-11-18T10:45:00Z
            tags: [java, testing, tdd]
            """;
    
        YamlService yamlService = new YamlService();
    
        Map<String, Object> result = yamlService.parseYaml(yamlHeader);
    
        assertEquals("Test Note", result.get("title"));
        assertNotNull(result.get("tags"));
        assertTrue(result.get("tags") instanceof List);
    }

    @Test
    void testParseYamlWithAllFields() {
    // Given
        String yamlHeader = """
            title: Complete Note
            created: 2025-11-18T10:30:00Z
            modified: 2025-11-18T10:45:00Z
            tags: [java, project]
            author: Marc
            status: in-progress
            priority: 5
            """;
    
        YamlService yamlService = new YamlService();
    
        Map<String, Object> result = yamlService.parseYaml(yamlHeader);
    
        assertEquals("Complete Note", result.get("title"));
        assertEquals("Marc", result.get("author"));
        assertEquals("in-progress", result.get("status"));
        assertEquals(5, result.get("priority"));
    }
}
