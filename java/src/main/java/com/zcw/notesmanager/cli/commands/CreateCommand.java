package com.zcw.notesmanager.cli.commands;

import com.zcw.notesmanager.model.Note;
import com.zcw.notesmanager.service.NoteService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
            
            System.out.println("Enter note content (type 'END' on a new line when done):");
            StringBuilder contentBuilder = new StringBuilder();
            
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().equals("END")) {
                    break;
                }
                if (contentBuilder.length() > 0) {
                    contentBuilder.append("\n");
                }
                contentBuilder.append(line);
            }
            
            String content = contentBuilder.toString().trim();
            
            if (content.isEmpty()) {
                System.out.println("Warning: Note created with empty content");
            }
            
            System.out.print("Add tags (comma-separated, or press Enter to skip): ");
            String tagsInput = scanner.nextLine().trim();
            List<String> tags = new ArrayList<>();
            
            if (!tagsInput.isEmpty()) {
                String[] tagArray = tagsInput.split(",");
                for (String tag : tagArray) {
                    String cleanTag = tag.trim().toLowerCase();
                    if (!cleanTag.isEmpty()) {
                        tags.add(cleanTag);
                    }
                }
            }
            
            Integer priority = null;
            System.out.print("Set priority (1-5, or press Enter to skip): ");
            String priorityInput = scanner.nextLine().trim();
            
            if (!priorityInput.isEmpty()) {
                try {
                    int p = Integer.parseInt(priorityInput);
                    if (p >= 1 && p <= 5) {
                        priority = p;
                    } else {
                        System.out.println("Warning: Priority must be 1-5. Skipping.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Warning: Invalid priority. Skipping.");
                }
            }
            
            Note note = noteService.createNote(title, content);

            if (!tags.isEmpty() || priority != null) {
                if (!tags.isEmpty()) {
                    note.setTags(tags);
                }
                if (priority != null) {
                    note.setPriority(priority);
                }
                noteService.updateNote(note.getId(), note.getTitle(), note.getContent(), note.getTags(), note.getPriority());
            }
            
            System.out.println("\n✓ Note created successfully!");
            System.out.println("ID: " + note.getId());
            System.out.println("Title: " + note.getTitle());
            if (!tags.isEmpty()) {
                System.out.println("Tags: " + String.join(", ", tags));
            }
            if (priority != null) {
                System.out.println("Priority: " + priority);
            }
            System.out.println();
            
        } catch (IOException e) {
            System.err.println("Error creating note: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}