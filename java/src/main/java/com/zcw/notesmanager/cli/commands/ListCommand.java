package com.zcw.notesmanager.cli.commands;

import com.zcw.notesmanager.model.Note;
import com.zcw.notesmanager.service.NoteService;

import java.io.IOException;
import java.util.List;

public class ListCommand {
    
    private final NoteService noteService;
    
    public ListCommand(NoteService noteService) {
        this.noteService = noteService;
    }
    
    public void execute() {
        try {
            List<Note> notes = noteService.listAllNotes();
            
            if (notes.isEmpty()) {
                System.out.println("No notes found. Create one with 'create' command!");
                return;
            }
            
            System.out.println("\nYour Notes:");
            System.out.println("═══════════════════════════════════════════════════════");
            
            for (int i = 0; i < notes.size(); i++) {
                Note note = notes.get(i);
                System.out.printf("%d. %s - %s%n", 
                    i + 1, 
                    note.getId(), 
                    note.getTitle());
            }
            
            System.out.println("═══════════════════════════════════════════════════════");
            System.out.printf("Total: %d note%s%n%n", notes.size(), notes.size() == 1 ? "" : "s");
            
        } catch (IOException e) {
            System.err.println("Error listing notes: " + e.getMessage());
        }
    }
}