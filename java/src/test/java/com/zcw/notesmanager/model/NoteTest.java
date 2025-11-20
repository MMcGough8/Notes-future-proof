package com.zcw.notesmanager.model;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class NoteTest {

    @Test
    void testNoteCreationWithRequiredFields() {
        String id = "test-note-1";
        String title = "My First Note";
        Instant created = Instant.now();
        Instant modified = Instant.now();
        String content = "This is the content of my note.";

        Note note = new Note(id, title, created, modified, content);
            assertEquals(id, note.getId());
            assertEquals(title, note.getTitle());
            assertEquals(created, note.getCreated());
            assertEquals(modified, note.getModified());
            assertEquals(content, note.getContent());
            assertNotNull(note.getTags());
            assertTrue(note.getTags().isEmpty());
    }

    @Test
    void testNoteWithOptionalFields() {
        Note note = new Note("test-2", "Test Note", 
            Instant.now(), Instant.now(), "Content");
        
        note.setAuthor("Marc");
        note.setStatus("in-progress");
        note.setPriority(3);
        note.setTags(Arrays.asList("java", "testing"));

        assertEquals("Marc", note.getAuthor());
        assertEquals("in-progress", note.getStatus());
        assertEquals(3, note.getPriority());
        assertEquals(2, note.getTags().size());
        assertTrue(note.getTags().contains("java"));
    }

    @Test
    void testNoteTagsInitializedAsEmptyList() {
        Note note = new Note("test-3", "Test", Instant.now(), Instant.now(), "Content");
            assertNotNull(note.getTags());
            assertTrue(note.getTags().isEmpty());
    }
    
    @Test
    void testNoteCreationWithAutoTimestamps() {
        String id = "test-4";
        String title = "Auto Timestamp Note";
        String content = "This note gets timestamps automatically";
        
        Note note = new Note(id, title, content);
            assertEquals(id, note.getId());
            assertEquals(title, note.getTitle());
            assertEquals(content, note.getContent());
            assertNotNull(note.getCreated());
            assertNotNull(note.getModified());
            assertNotNull(note.getTags());
            assertTrue(note.getTags().isEmpty());
    
        Instant now = Instant.now();
            assertTrue(note.getCreated().isBefore(now.plusSeconds(1)));
            assertTrue(note.getModified().isBefore(now.plusSeconds(1)));
    }
}


