package com.zcw.notesmanager.service;
import com.zcw.notesmanager.model.Note;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
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
            assertThrows(RuntimeException.class, () -> { yamlService.parseYaml(invalidYaml);
        });
    }

    @Test
    void testParseYamlWithNullInput() {
        YamlService yamlService = new YamlService();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> yamlService.parseYaml(null));
            assertEquals("YAML content cannot be null", exception.getMessage());
    }

@Test
void testWriteYamlWithTags() {
    
    YamlService yamlService = new YamlService();
    
    Map<String, Object> noteData = new HashMap<>();
    noteData.put("title", "Test Note");
    noteData.put("tags", Arrays.asList("java", "testing", "tdd"));
    
    
    String yaml = yamlService.writeYaml(noteData);
 
    Map<String, Object> parsed = yamlService.parseYaml(yaml);
    
    assertEquals("Test Note", parsed.get("title"));
    assertNotNull(parsed.get("tags"));
    assertTrue(parsed.get("tags") instanceof List);
    
    @SuppressWarnings("unchecked")
    List<String> tags = (List<String>) parsed.get("tags");
    assertEquals(3, tags.size());
    assertTrue(tags.contains("java"));
    assertTrue(tags.contains("testing"));
    assertTrue(tags.contains("tdd"));
}

    @Test
    void testInstantToStringFormat() {
        Note note = new Note("test-format", "Format Test", "Content");

        String createdString = note.getCreated().toString();
        String modifiedString = note.getModified().toString();
            assertTrue(createdString.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.*Z"));
            assertTrue(modifiedString.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.*Z"));
            assertTrue(createdString.endsWith("Z"));
            assertTrue(modifiedString.endsWith("Z"));
}
}
