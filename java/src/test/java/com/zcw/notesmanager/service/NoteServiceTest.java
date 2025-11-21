package com.zcw.notesmanager.service;

import com.zcw.notesmanager.model.Note;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class NoteServiceTest {

    @Test
    void testCreateNote(@TempDir Path tempDir) throws IOException {

        NoteService noteService = new NoteService(tempDir);
        String title = "My Test Note";
        String content = "This is the content of my note.";

        Note note = noteService.createNote(title, content);
            assertNotNull(note);
            assertEquals(title, note.getTitle());
            assertEquals(content, note.getContent());
            assertNotNull(note.getId());
            assertNotNull(note.getCreated());
            assertNotNull(note.getModified());
        
        Path noteFile = tempDir.resolve(note.getId() + ".note");
            assertTrue(Files.exists(noteFile));

        String fileContent = Files.readString(noteFile);
            assertTrue(fileContent.contains("title: My Test Note"));
            assertTrue(fileContent.contains("This is the content of my note."));
    }
}