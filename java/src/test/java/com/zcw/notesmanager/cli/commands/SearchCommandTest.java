package com.zcw.notesmanager.cli.commands;

import com.zcw.notesmanager.model.Note;
import com.zcw.notesmanager.service.NoteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SearchCommandTest {

    @Test
    void testSearchByContent(@TempDir Path tempDir) throws IOException {
        NoteService noteService = new NoteService(tempDir);
            noteService.createNote("Note 1", "This contains Java content");
            noteService.createNote("Note 2", "This contains Python content");

        List<Note> results = noteService.searchByContent("Java");
            assertEquals(1, results.size());
            assertEquals("Note 1", results.get(0).getTitle());
    }
    
    @Test
    void testSearchByTitle(@TempDir Path tempDir) throws IOException {
        NoteService noteService = new NoteService(tempDir);
            noteService.createNote("Java Tutorial", "Content about programming");
            noteService.createNote("Python Guide", "Content about Python");

        List<Note> results = noteService.searchByTitle("Java");
            assertEquals(1, results.size());
            assertEquals("Java Tutorial", results.get(0).getTitle());
    }
    
    @Test
    void testSearchAll(@TempDir Path tempDir) throws IOException {
        NoteService noteService = new NoteService(tempDir);
            noteService.createNote("Java Tutorial", "Learning Java");
            noteService.createNote("Python Guide", "Mentions Java briefly");
            noteService.createNote("Shopping", "Buy coffee");
 
        List<Note> results = noteService.searchAll("Java");

        assertEquals(2, results.size());
    }
    
    @Test
    void testSearchWithTag(@TempDir Path tempDir) throws IOException {
        NoteService noteService = new NoteService(tempDir);
        
        Note note1 = noteService.createNote("Java Note", "Learning Java");
            note1.setTags(Arrays.asList("programming"));
            noteService.updateNote(note1.getId(), note1.getTitle(), note1.getContent(), note1.getTags());
        
        Note note2 = noteService.createNote("Java Shopping", "Buy Java coffee");
            note2.setTags(Arrays.asList("personal"));
            noteService.updateNote(note2.getId(), note2.getTitle(), note2.getContent(), note2.getTags());

        List<Note> results = noteService.searchWithTag("Java", "programming");
            assertEquals(1, results.size());
            assertEquals("Java Note", results.get(0).getTitle());
    }
    
    @Test
    void testSearchCaseInsensitive(@TempDir Path tempDir) throws IOException {
        NoteService noteService = new NoteService(tempDir);
        noteService.createNote("Test", "Learning JAVA");

        List<Note> results = noteService.searchByContent("java");
            assertEquals(1, results.size());
    }
}