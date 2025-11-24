package com.zcw.notesmanager.cli.commands;

import com.zcw.notesmanager.service.NoteService;
import java.io.IOException;
import java.util.List;

public class TagsCommand {
    
    private final NoteService noteService;
    
    public TagsCommand(NoteService noteService) {
        this.noteService = noteService;
    }
    
    public void execute() {
        try {
            List<String> tags = noteService.getAllTags();
            
            if (tags.isEmpty()) {
                System.out.println("\nNo tags found. Add tags when creating or editing notes.\n");
                return;
            }
            
            System.out.println("\nAll Tags:");
            System.out.println("═══════════════════════════════════════════════════════");
            
            for (String tag : tags) {
                System.out.println("  • " + tag);
            }
            
            System.out.println("═══════════════════════════════════════════════════════");
            System.out.printf("Total: %d tag%s%n", tags.size(), tags.size() == 1 ? "" : "s");
            System.out.println("\nUse 'list --tag <tagname>' to filter notes by tag.\n");
            
        } catch (IOException e) {
            System.err.println("Error listing tags: " + e.getMessage());
        }
    }
}