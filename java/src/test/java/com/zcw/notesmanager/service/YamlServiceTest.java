package com.zcw.notesmanager.service;

import org.junit.jupiter.api.Test;
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
}
