package com.zcw.notesmanager.cli.commands;

import com.zcw.notesmanager.service.NoteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class DeleteCommandTest {

    @Test
    void testDeleteWithConfirmation(@TempDir Path tempDir) throws IOException {
        NoteService noteService = new NoteService(tempDir);
        noteService.createNote("Test Note", "Content");
        
        String input = "yes\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        DeleteCommand deleteCommand = new DeleteCommand(noteService, "test", scanner);
        deleteCommand.execute();
        
        assertTrue(noteService.listAllNotes().isEmpty());
    }
    
    @Test
    void testDeleteWithCancellation(@TempDir Path tempDir) throws IOException {

        NoteService noteService = new NoteService(tempDir);
        noteService.createNote("Test Note", "Content");
        
        String input = "no\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        DeleteCommand deleteCommand = new DeleteCommand(noteService, "test", scanner);
        deleteCommand.execute();

        assertEquals(1, noteService.listAllNotes().size());
    }
}