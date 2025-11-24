package com.zcw.notesmanager.cli.commands;

import com.zcw.notesmanager.model.Note;
import com.zcw.notesmanager.service.NoteService;

import java.io.IOException;
import java.util.List;

public class SearchCommand {
    
    private final NoteService noteService;
    private final String query;
    private final String searchType;
    private final String filterTag;
    
    public SearchCommand(NoteService noteService, String query) {
        this(noteService, query, "all", null);
    }
    
    public SearchCommand(NoteService noteService, String query, String searchType) {
        this(noteService, query, searchType, null);
    }
    
    public SearchCommand(NoteService noteService, String query, String searchType, String filterTag) {
        this.noteService = noteService;
        this.query = query;
        this.searchType = searchType;
        this.filterTag = filterTag;
    }
    
    public void execute() {
        if (query == null || query.trim().isEmpty()) {
            System.err.println("Error: Search query is required");
            System.out.println("Usage: search <query> [--title | --content] [--tag <tagname>]");
            return;
        }
        
        try {
            List<Note> results;
            
            if (filterTag != null && !filterTag.isEmpty()) {
                results = noteService.searchWithTag(query, filterTag);
                System.out.println("\nSearch results for \"" + query + "\" with tag '" + filterTag + "':");
            } else if ("title".equals(searchType)) {
                results = noteService.searchByTitle(query);
                System.out.println("\nSearch results in titles for \"" + query + "\":");
            } else if ("content".equals(searchType)) {
                results = noteService.searchByContent(query);
                System.out.println("\nSearch results in content for \"" + query + "\":");
            } else {
                results = noteService.searchAll(query);
                System.out.println("\nSearch results for \"" + query + "\":");
            }
            
            if (results.isEmpty()) {
                System.out.println("No notes found matching your search.\n");
                return;
            }
            
            System.out.println("═══════════════════════════════════════════════════════");
            
            for (int i = 0; i < results.size(); i++) {
                Note note = results.get(i);
                System.out.printf("%d. %s - %s", 
                    i + 1, 
                    note.getId(), 
                    note.getTitle());
                
                if (note.getTags() != null && !note.getTags().isEmpty()) {
                    System.out.printf(" [%s]", String.join(", ", note.getTags()));
                }
                System.out.println();
                
                String snippet = getSnippet(note, query, searchType);
                if (snippet != null) {
                    System.out.println("   " + snippet);
                }
            }
            
            System.out.println("═══════════════════════════════════════════════════════");
            System.out.printf("Found: %d note%s%n%n", results.size(), results.size() == 1 ? "" : "s");
            
        } catch (IOException e) {
            System.err.println("Error searching notes: " + e.getMessage());
        }
    }
    
    private String getSnippet(Note note, String query, String searchType) {
        if ("title".equals(searchType)) {
            return null; 
        }
        
        String content = note.getContent();
        if (content == null || content.isEmpty()) {
            return null;
        }
        
        String lowerContent = content.toLowerCase();
        String lowerQuery = query.toLowerCase();
        
        int index = lowerContent.indexOf(lowerQuery);
        if (index == -1) {
            return null;
        }
        
        int start = Math.max(0, index - 50);
        int end = Math.min(content.length(), index + query.length() + 50);
        
        String snippet = content.substring(start, end);
        
        if (start > 0) {
            snippet = "..." + snippet;
        }
        if (end < content.length()) {
            snippet = snippet + "...";
        }

        return snippet.trim();
    }
}