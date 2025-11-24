package com.zcw.notesmanager.cli.commands;

import com.zcw.notesmanager.model.Note;
import com.zcw.notesmanager.service.NoteService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EditCommand {
    
    private final NoteService noteService;
    private final String noteId;
    private final Scanner scanner;
    
    public EditCommand(NoteService noteService, String noteId, Scanner scanner) {
        this.noteService = noteService;
        this.noteId = noteId;
        this.scanner = scanner;
    }
    
    public void execute() {
        if (noteId == null || noteId.trim().isEmpty()) {
            System.err.println("Error: Note ID is required");
            System.out.println("Usage: edit <note-id>");
            return;
        }
        
        try {
            Note note = noteService.readNote(noteId);
            
            System.out.println("\n=== Editing Note ===");
            System.out.println("ID: " + note.getId());
            System.out.println();
            
            System.out.println("Current title: " + note.getTitle());
            System.out.print("New title (press Enter to keep current): ");
            String newTitle = scanner.nextLine().trim();
            if (newTitle.isEmpty()) {
                newTitle = note.getTitle();
            }
            System.out.println("\nCurrent content:");
            System.out.println("───────────────────────────────────────");
            System.out.println(note.getContent());
            System.out.println("───────────────────────────────────────");
            System.out.println("\nDo you want to:");
            System.out.println("  1. Replace content");
            System.out.println("  2. Append to content");
            System.out.println("  3. Keep current content");
            System.out.print("Choice (1/2/3): ");
            
            String choice = scanner.nextLine().trim();
            String newContent = note.getContent();
            
            if (choice.equals("1")) {
                System.out.println("\nEnter new content (type 'END' on a new line when done):");
                newContent = readContent();
            } else if (choice.equals("2")) {
                System.out.println("\nEnter content to append (type 'END' on a new line when done):");
                String additionalContent = readContent();
                if (!additionalContent.isEmpty()) {
                    newContent = note.getContent() + "\n\n" + additionalContent;
                }
            } else if (choice.equals("3")) {
                System.out.println("Keeping current content.");
            } else {
                System.out.println("Invalid choice. Keeping current content.");
            }
            
            System.out.print("\nCurrent tags: ");
            if (note.getTags() != null && !note.getTags().isEmpty()) {
                System.out.println(String.join(", ", note.getTags()));
            } else {
                System.out.println("(none)");
            }
            System.out.print("New tags (comma-separated, or press Enter to keep current): ");
            String tagsInput = scanner.nextLine().trim();
            
            List<String> newTags = note.getTags();
            if (!tagsInput.isEmpty()) {
                newTags = new ArrayList<>();
                String[] tagArray = tagsInput.split(",");
                for (String tag : tagArray) {
                    String cleanTag = tag.trim().toLowerCase();
                    if (!cleanTag.isEmpty()) {
                        newTags.add(cleanTag);
                    }
                }
            }
            
            System.out.print("\nCurrent priority: ");
            if (note.getPriority() != null) {
                System.out.println(note.getPriority());
            } else {
                System.out.println("(none)");
            }
            System.out.print("New priority (1-5, or press Enter to keep current): ");
            String priorityInput = scanner.nextLine().trim();
            
            Integer newPriority = note.getPriority();
            if (!priorityInput.isEmpty()) {
                try {
                    int p = Integer.parseInt(priorityInput);
                    if (p >= 1 && p <= 5) {
                        newPriority = p;
                    } else {
                        System.out.println("Warning: Priority must be 1-5. Keeping current.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Warning: Invalid priority. Keeping current.");
                }
            }
            
            noteService.updateNote(noteId, newTitle, newContent, newTags, newPriority);
            
            System.out.println("\n✓ Note updated successfully!");
            System.out.println("Title: " + newTitle);
            if (newTags != null && !newTags.isEmpty()) {
                System.out.println("Tags: " + String.join(", ", newTags));
            }
            if (newPriority != null) {
                System.out.println("Priority: " + newPriority);
            }
            System.out.println();
            
        } catch (IOException e) {
            System.err.println("Error editing note: " + e.getMessage());
        }
    }
    
    private String readContent() {
        StringBuilder contentBuilder = new StringBuilder();
        boolean firstLine = true;
        
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.trim().equals("END")) {
                break;
            }
            if (!firstLine) {
                contentBuilder.append("\n");
            }
            contentBuilder.append(line);
            firstLine = false;
        }
        
        return contentBuilder.toString().trim();
    }
}