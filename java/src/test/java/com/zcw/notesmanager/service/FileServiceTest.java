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

    @Test
    void testWriteNoteToFile(@TempDir Path tempDir) throws IOException {

    Path noteFile = tempDir.resolve("new-note.note");
    String yamlHeader = "title: New Note\ncreated: 2025-11-20T10:30:00Z\nmodified: 2025-11-20T10:30:00Z";
    String content = "This is my note content.\nWith multiple lines.";
    
    FileService fileService = new FileService();
        fileService.writeNote(noteFile, yamlHeader, content);
            assertTrue(Files.exists(noteFile));
    
    String writtenContent = Files.readString(noteFile);
        assertTrue(writtenContent.startsWith("---"));
        assertTrue(writtenContent.contains("title: New Note"));
        assertTrue(writtenContent.contains("This is my note content"));
    }

@Test
void testUpdateExistingNote(@TempDir Path tempDir) throws IOException {
 
    Path noteFile = tempDir.resolve("existing-note.note");
    String originalYaml = "title: Original Title\ncreated: 2025-11-20T10:00:00Z\nmodified: 2025-11-20T10:00:00Z";
    String originalContent = "Original content";
    
    FileService fileService = new FileService();
        fileService.writeNote(noteFile, originalYaml, originalContent);
    
  
    String updatedYaml = "title: Updated Title\ncreated: 2025-11-20T10:00:00Z\nmodified: 2025-11-20T15:30:00Z";
    String updatedContent = "Updated content with new information";
        fileService.updateNote(noteFile, updatedYaml, updatedContent);
    
   
    String fullContent = fileService.readFile(noteFile);
    String[] parts = fileService.separateYamlAndContent(fullContent);
        assertTrue(parts[0].contains("Updated Title"));
        assertTrue(parts[0].contains("modified: 2025-11-20T15:30:00Z"));
        assertEquals(updatedContent, parts[1]);
    }

    @Test
void testDeleteNote(@TempDir Path tempDir) throws IOException {

    Path noteFile = tempDir.resolve("to-delete.note");
    String yaml = "title: Delete Me\ncreated: 2025-11-20T10:00:00Z";
    String content = "This will be deleted";
    
    FileService fileService = new FileService();
    fileService.writeNote(noteFile, yaml, content);
        assertTrue(Files.exists(noteFile));

    fileService.deleteNote(noteFile);
        assertFalse(Files.exists(noteFile));
}

@Test
void testFullNoteLifecycle(@TempDir Path tempDir) throws IOException {

    Path noteFile = tempDir.resolve("lifecycle.note");
    FileService fileService = new FileService();
    
    String yaml = "title: Lifecycle Test\ncreated: 2025-11-20T10:00:00Z\nmodified: 2025-11-20T10:00:00Z";
    String content = "Original content";
        fileService.writeNote(noteFile, yaml, content);
            assertTrue(Files.exists(noteFile));
    
    String readContent = fileService.readFile(noteFile);
        assertTrue(readContent.contains("Lifecycle Test"));
    
    String updatedYaml = "title: Lifecycle Test\ncreated: 2025-11-20T10:00:00Z\nmodified: 2025-11-20T15:00:00Z";
    String updatedContent = "Updated content";
    fileService.updateNote(noteFile, updatedYaml, updatedContent);
    
    String rereadContent = fileService.readFile(noteFile);
        assertTrue(rereadContent.contains("Updated content"));
    
    fileService.deleteNote(noteFile);
        assertFalse(Files.exists(noteFile));
}
}
