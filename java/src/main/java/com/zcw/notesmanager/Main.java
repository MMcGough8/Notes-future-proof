package com.zcw.notesmanager;

import com.zcw.notesmanager.cli.CommandParser;
import com.zcw.notesmanager.cli.commands.*;
import com.zcw.notesmanager.model.Note;
import com.zcw.notesmanager.service.NoteService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try {
            Path notesDir = Path.of("notes");
            if (!Files.exists(notesDir)) {
                Files.createDirectories(notesDir);
            }

            NoteService noteService = new NoteService(notesDir);
            Scanner scanner = new Scanner(System.in);
            CommandParser parser = new CommandParser();

            if (args.length > 0) {
                executeCommand(args, parser, noteService, scanner);
                return;
            }

            System.out.println("=== Personal Notes Manager ===");
            System.out.println("Type 'help' for commands, 'exit' to quit\n");

            while (true) {
                System.out.print("> ");
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) continue;
                if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
                    System.out.println("Goodbye!");
                    break;
                }

                String[] commandArgs = input.split("\\s+", 2);
                String commandName = commandArgs[0].toLowerCase();

                if ("help".equals(commandName)) {
                    showHelp();
                    continue;
                }

                try {
                    executeCommand(commandArgs, parser, noteService, scanner);
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage() + "\n");
                }
            }

        } catch (Exception e) {
            System.err.println("Fatal error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void executeCommand(String[] args, CommandParser parser, NoteService noteService, Scanner scanner) {
        String commandName = args[0].toLowerCase();

        String[] remainingArgs = args.length > 1
                ? args[1].split("\\s+")
                : new String[0];

        switch (commandName) {
            case "create":
                new CreateCommand(noteService, scanner).execute();
                break;
                
            case "list":
    if (remainingArgs.length >= 1 && remainingArgs[0].equals("--priority")) {
        try {
            List<Note> notes = noteService.listNotesByPriority();
                System.out.println("\nNotes by Priority (Highest to Lowest):");
                System.out.println("═══════════════════════════════════════════════════════");
            
            for (int i = 0; i < notes.size(); i++) {
                Note note = notes.get(i);
                    String priorityIndicator = note.getPriority() != null ? " [P" + note.getPriority() + "]" : "";
                    System.out.printf("%d. %s - %s%s", i + 1, note.getId(), note.getTitle(), priorityIndicator);
                if (note.getTags() != null && !note.getTags().isEmpty()) {
                    System.out.printf(" [%s]", String.join(", ", note.getTags()));
                }
                System.out.println();
            }
            System.out.println("═══════════════════════════════════════════════════════");
        } catch (IOException e) {
            System.err.println("Error listing notes: " + e.getMessage());
        }
    }
        else if (remainingArgs.length >= 2 && remainingArgs[0].equals("--tag")) {
            String tag = remainingArgs[1];
        new ListCommand(noteService, tag).execute();

    } else if (remainingArgs.length > 0 && remainingArgs[0].startsWith("--")) {
        System.err.println("Unknown option: " + remainingArgs[0]);
        System.out.println("Usage: list [--tag <tagname>] [--priority]\n");
    } else {
        new ListCommand(noteService).execute();
    }
    break;
            case "tags":
                new TagsCommand(noteService).execute();
                break;
                
            case "search":
            case "find":
                handleSearchCommand(remainingArgs, noteService);
                break;
                
            case "read":
            case "view":
                if (remainingArgs.length == 0) {
                    System.err.println("Error: Note ID is required");
                    System.out.println("Usage: read <note-id>\n");
                } else {
                    new ReadCommand(noteService, remainingArgs[0]).execute();
                }
                break;
                
            case "edit":
            case "update":
                if (remainingArgs.length == 0) {
                    System.err.println("Error: Note ID is required");
                    System.out.println("Usage: edit <note-id>\n");
                } else {
                    new EditCommand(noteService, remainingArgs[0], scanner).execute();
                }
                break;
                
            case "delete":
            case "remove":
                if (remainingArgs.length == 0) {
                    System.err.println("Error: Note ID is required");
                    System.out.println("Usage: delete <note-id>\n");
                } else {
                    new DeleteCommand(noteService, remainingArgs[0], scanner).execute();
                }
                break;

            case "help":
                showHelp();
                break;

            default:
                System.out.println("Unknown command: " + commandName);
                System.out.println("Type 'help' for available commands\n");
        }
    }
    
    private static void handleSearchCommand(String[] remainingArgs, NoteService noteService) {
        if (remainingArgs.length == 0) {
            System.err.println("Error: Search query is required");
            System.out.println("Usage: search <query> [--title | --content] [--tag <tagname>]\n");
            return;
        }
        
        String query = null;
        String searchType = "all";
        String filterTag = null;
        
        int i = 0;
        while (i < remainingArgs.length) {
            String arg = remainingArgs[i];
            
            if (arg.equals("--title")) {
                searchType = "title";
                i++;
            } else if (arg.equals("--content")) {
                searchType = "content";
                i++;
            } else if (arg.equals("--tag") && i + 1 < remainingArgs.length) {
                filterTag = remainingArgs[i + 1];
                i += 2;
            } else {
                query = arg;
                i++;
            }
        }
        
        if (query == null) {
            System.err.println("Error: Search query is required");
            System.out.println("Usage: search <query> [--title | --content] [--tag <tagname>]\n");
            return;
        }
        
        new SearchCommand(noteService, query, searchType, filterTag).execute();
    }

    private static void showHelp() {
        System.out.println("\nAvailable commands:");
        System.out.println("  create                     Create a new note");
        System.out.println("  list                       List all notes");
        System.out.println("  list --tag <name>          List notes with specific tag");
        System.out.println("  list --priority            List notes by priority");
        System.out.println("  tags                       Show all tags");
        System.out.println("  search <query>             Search notes (or 'find')");
        System.out.println("  search <query> --title     Search in titles only");
        System.out.println("  search <query> --content   Search in content only");
        System.out.println("  search <query> --tag <tag> Search within tagged notes");
        System.out.println("  read <id>                  View a note (or 'view')");
        System.out.println("  edit <id>                  Edit a note (or 'update')");
        System.out.println("  delete <id>                Delete a note (or 'remove')");
        System.out.println("  help                       Show this help message");
        System.out.println("  exit                       Quit the app\n");
    }
}