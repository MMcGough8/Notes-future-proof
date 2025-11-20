package com.zcw.notesmanager.service;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

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

    @Test
    void testParseYamlWithEmptyString() {
  
        YamlService yamlService = new YamlService();
    
   
        Map<String, Object> result = yamlService.parseYaml("");
    
    
        assertNull(result);
    }

    @Test
    void testParseYamlWithInvalidSyntax() {
    
        String invalidYaml = "title: Broken\n  this is: not valid YAML: at all";
        YamlService yamlService = new YamlService();
    
        assertThrows(RuntimeException.class, () -> {
            yamlService.parseYaml(invalidYaml);
        });
    }

    @Test
    void testParseYamlWithNullInput() {
        YamlService yamlService = new YamlService();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> yamlService.parseYaml(null));

        assertEquals("YAML content cannot be null", exception.getMessage());
    }

    @Test
    void testWriteSimpleYamlHeader() {
        YamlService yamlService = new YamlService();
    
        Map<String, Object> noteData = new HashMap<>();
        noteData.put("title", "My Test Note");
        noteData.put("created", "2025-11-18T10:30:00Z");
        noteData.put("modified", "2025-11-18T10:45:00Z");
    
    
        String yaml = yamlService.writeYaml(noteData);
        assertNotNull(yaml);
        assertTrue(yaml.contains("title: My Test Note"));
        assertTrue(yaml.contains("created: 2025-11-18T10:30:00Z"));
        assertTrue(yaml.contains("modified: 2025-11-18T10:45:00Z"));
    }
}
