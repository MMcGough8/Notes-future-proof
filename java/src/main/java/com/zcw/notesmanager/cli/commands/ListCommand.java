package com.zcw.notesmanager.cli.commands;

import com.zcw.notesmanager.model.Note;
import com.zcw.notesmanager.service.NoteService;

import java.io.IOException;
import java.util.List;

public class ListCommand {
    
    private final NoteService noteService;
    private final String filterTag;
    
    public ListCommand(NoteService noteService) {
        this(noteService, null);
    }
    
    public ListCommand(NoteService noteService, String filterTag) {
        this.noteService = noteService;
        this.filterTag = filterTag;
    }
    
    public void execute() {
        try {
            List<Note> notes;
            
            if (filterTag != null && !filterTag.isEmpty()) {
                notes = noteService.listNotesByTag(filterTag);
                
                if (notes.isEmpty()) {
                    System.out.println("\nNo notes found with tag: " + filterTag);
                    System.out.println("Use 'tags' command to see all available tags.\n");
                    return;
                }
                
                System.out.println("\nNotes tagged with '" + filterTag + "':");
            } else {
                notes = noteService.listAllNotes();
                
                if (notes.isEmpty()) {
                    System.out.println("No notes found. Create one with 'create' command!");
                    return;
                }
                
                System.out.println("\nYour Notes:");
            }
            
            System.out.println("═══════════════════════════════════════════════════════");
            
            for (int i = 0; i < notes.size(); i++) {
                Note note = notes.get(i);
                System.out.printf("%d. %s - %s", 
                    i + 1, 
                    note.getId(), 
                    note.getTitle());
                
                // Show tags if present
                if (note.getTags() != null && !note.getTags().isEmpty()) {
                    System.out.printf(" [%s]", String.join(", ", note.getTags()));
                }
                System.out.println();
            }
            
            System.out.println("═══════════════════════════════════════════════════════");
            System.out.printf("Total: %d note%s%n%n", notes.size(), notes.size() == 1 ? "" : "s");
            
        } catch (IOException e) {
            System.err.println("Error listing notes: " + e.getMessage());
        }
    }
}