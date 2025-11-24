package com.zcw.notesmanager.cli.commands;

import com.zcw.notesmanager.model.Note;
import com.zcw.notesmanager.service.NoteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class TagsCommandTest {

    @Test
    void testListNotesByTag(@TempDir Path tempDir) throws IOException {
        NoteService noteService = new NoteService(tempDir);
        
        Note note1 = noteService.createNote("Java Note", "Content 1");
        note1.setTags(Arrays.asList("java", "programming"));
        noteService.updateNote(note1.getId(), note1.getTitle(), note1.getContent(), note1.getTags());
        
        Note note2 = noteService.createNote("Python Note", "Content 2");
        note2.setTags(Arrays.asList("python", "programming"));
        noteService.updateNote(note2.getId(), note2.getTitle(), note2.getContent(), note2.getTags());

        var javaNotes = noteService.listNotesByTag("java");
        var programmingNotes = noteService.listNotesByTag("programming");

        assertEquals(1, javaNotes.size());
        assertEquals(2, programmingNotes.size());
        assertEquals("Java Note", javaNotes.get(0).getTitle());
    }
    
    @Test
    void testGetAllTags(@TempDir Path tempDir) throws IOException {
        NoteService noteService = new NoteService(tempDir);
        
        Note note1 = noteService.createNote("Note 1", "Content 1");
        note1.setTags(Arrays.asList("java", "programming"));
        noteService.updateNote(note1.getId(), note1.getTitle(), note1.getContent(), note1.getTags());
        
        Note note2 = noteService.createNote("Note 2", "Content 2");
        note2.setTags(Arrays.asList("python", "programming"));
        noteService.updateNote(note2.getId(), note2.getTitle(), note2.getContent(), note2.getTags());

        var allTags = noteService.getAllTags();
  
        assertEquals(3, allTags.size());
        assertTrue(allTags.contains("java"));
        assertTrue(allTags.contains("python"));
        assertTrue(allTags.contains("programming"));
    }
}