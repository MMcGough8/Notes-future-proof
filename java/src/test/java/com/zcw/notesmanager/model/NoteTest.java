package com.zcw.notesmanager.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
    
class NoteTest {
    

    @Test
    void testNoteCreationWithRequiredFields() {

        String id = "test-note-1";
        String title = "My First Note";
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime modified = LocalDateTime.now();
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
        Note note = new Note("test-2", "Test Note", LocalDateTime.now(), LocalDateTime.now(), "Content");
        
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

        Note note = new Note("test-3", "Test", LocalDateTime.now(), LocalDateTime.now(), "Content");


        assertNotNull(note.getTags());
        assertTrue(note.getTags().isEmpty());
    }
}


