package com.zcw.notesmanager.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.IOException;
import java.nio.file.Files; 
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileServiceTest {
    @Test
    void testSeparateYamlFromContent(@TempDir Path tempDir) throws IOException {
        Path noteFile = tempDir.resolve("test-note.note");
        String noteFileContent = """
            ---
            title: Test Note
            created: 2025-11-18T10:30:00Z
            modified: 2025-11-18T10:45:00Z
            ---
            
            This is the note content.
            It has multiple lines.
            """;
    Files.writeString(noteFile, noteFileContent);
    
    FileService fileService = new FileService();
    
    String fullContent = fileService.readFile(noteFile);
    String[] parts = fileService.separateYamlAndContent(fullContent);
    
    assertNotNull(parts);
    assertEquals(2, parts.length);
    
    String yamlHeader = parts[0];
    String content = parts[1];
    
    assertTrue(yamlHeader.contains("title: Test Note"));
    assertTrue(yamlHeader.contains("created: 2025-11-18T10:30:00Z"));
    
    assertTrue(content.contains("This is the note content"));
    assertTrue(content.contains("It has multiple lines"));
    }

    @Test
    void testSeparateYamlWithNoContent() {
    String noteWithNoContent = """
            ---
            title: Empty Note
            created: 2025-11-18T10:30:00Z
            ---
            """;
    
    FileService fileService = new FileService();
    
    String[] parts = fileService.separateYamlAndContent(noteWithNoContent);
        assertEquals(2, parts.length);
        assertTrue(parts[0].contains("title: Empty Note"));
        assertEquals("", parts[1]); // Empty content
}

    @Test
    void testSeparateYamlInvalidFormat() {
    String invalidNote = "This is just text with no YAML header";
    FileService fileService = new FileService();

    assertThrows(IllegalArgumentException.class, () -> {
        fileService.separateYamlAndContent(invalidNote);
    });
    }
}
