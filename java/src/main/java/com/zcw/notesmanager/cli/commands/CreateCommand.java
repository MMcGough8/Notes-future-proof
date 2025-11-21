package com.zcw.notesmanager.cli.commands;

import com.zcw.notesmanager.model.Note;
import com.zcw.notesmanager.service.NoteService;

import java.io.IOException;
import java.util.Scanner;

public class CreateCommand {
    
    private final NoteService noteService;
    private final Scanner scanner;
    
    public CreateCommand(NoteService noteService, Scanner scanner) {
        this.noteService = noteService;
        this.scanner = scanner;
    }
    
    public void execute() {
        try {
            System.out.print("Enter note title: ");
            String title = scanner.nextLine().trim();
            
            if (title.isEmpty()) {
                System.out.println("Error: Title cannot be empty");
                return;
            }
            
            System.out.println("Enter note content (press Ctrl+D or Ctrl+Z when done):");
            StringBuilder contentBuilder = new StringBuilder();
            
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                contentBuilder.append(line).append("\n");
            }
            
            String content = contentBuilder.toString().trim();
            
            Note note = noteService.createNote(title, content);
            
            System.out.println("\n✓ Note created successfully!");
            System.out.println("ID: " + note.getId());
            System.out.println("Title: " + note.getTitle());
            
        } catch (IOException e) {
            System.err.println("Error creating note: " + e.getMessage());
        }
    }
}