package com.zcw.notesmanager.cli.commands;

import com.zcw.notesmanager.model.Note;
import com.zcw.notesmanager.service.NoteService;

import java.io.IOException;
import java.util.Scanner;

public class DeleteCommand {
    
    private final NoteService noteService;
    private final String noteId;
    private final Scanner scanner;
    
    public DeleteCommand(NoteService noteService, String noteId, Scanner scanner) {
        this.noteService = noteService;
        this.noteId = noteId;
        this.scanner = scanner;
    }
    
    public void execute() {
        if (noteId == null || noteId.trim().isEmpty()) {
            System.err.println("Error: Note ID is required");
            System.out.println("Usage: delete <note-id>");
            return;
        }
        
        try {

            Note note = noteService.readNote(noteId);
            
            System.out.println("\nNote to delete:");
            System.out.println("  ID: " + note.getId());
            System.out.println("  Title: " + note.getTitle());
            System.out.println();
            
            System.out.print("Are you sure you want to delete this note? (yes/no): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();
            
            if (confirmation.equals("yes") || confirmation.equals("y")) {
                noteService.deleteNote(noteId);
                System.out.println("\n✓ Note deleted successfully!\n");
            } else {
                System.out.println("\nDeletion cancelled.\n");
            }
            
        } catch (IOException e) {
            System.err.println("Error deleting note: " + e.getMessage());
        }
    }
}