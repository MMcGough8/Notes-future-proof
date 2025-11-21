package com.zcw.notesmanager.cli.commands;

import com.zcw.notesmanager.model.Note;
import com.zcw.notesmanager.service.NoteService;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class ReadCommand {
    
    private final NoteService noteService;
    private final String noteId;
    
    private static final DateTimeFormatter FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public ReadCommand(NoteService noteService, String noteId) {
        this.noteService = noteService;
        this.noteId = noteId;
    }
    
    public void execute() {
        if (noteId == null || noteId.trim().isEmpty()) {
            System.err.println("Error: Note ID is required");
            System.out.println("Usage: read <note-id>");
            return;
        }
        
        try {
            Note note = noteService.readNote(noteId);
            displayNote(note);
        } catch (IOException e) {
            System.err.println("Error reading note: " + e.getMessage());
        }
    }
    
    private void displayNote(Note note) {
        System.out.println();
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("Title: " + note.getTitle());
        System.out.println("ID: " + note.getId());
        System.out.println("Created: " + FORMATTER.format(note.getCreated().atZone(java.time.ZoneId.systemDefault())));
        System.out.println("Modified: " + FORMATTER.format(note.getModified().atZone(java.time.ZoneId.systemDefault())));
        
        if (note.getAuthor() != null) {
            System.out.println("Author: " + note.getAuthor());
        }
        if (note.getStatus() != null) {
            System.out.println("Status: " + note.getStatus());
        }
        if (note.getPriority() != null) {
            System.out.println("Priority: " + note.getPriority());
        }
        if (note.getTags() != null && !note.getTags().isEmpty()) {
            System.out.println("Tags: " + String.join(", ", note.getTags()));
        }
        
        System.out.println("───────────────────────────────────────────────────────");
        System.out.println(note.getContent());
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println();
    }
}