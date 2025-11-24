package com.zcw.notesmanager;

import com.zcw.notesmanager.cli.CommandParser;
import com.zcw.notesmanager.cli.commands.CreateCommand;
import com.zcw.notesmanager.cli.commands.ListCommand;
import com.zcw.notesmanager.cli.commands.ReadCommand;
import com.zcw.notesmanager.cli.commands.DeleteCommand;
import com.zcw.notesmanager.cli.commands.EditCommand;
import com.zcw.notesmanager.service.NoteService;
import java.nio.file.Files;
import java.nio.file.Path;
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
                new ListCommand(noteService).execute();
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

    private static void showHelp() {
        System.out.println("\nAvailable commands:");
        System.out.println("  create            Create a new note");
        System.out.println("  list              List all notes");
        System.out.println("  read <id>         View a note (or 'view')");
        System.out.println("  edit <id>         Edit a note (or 'update')");
        System.out.println("  delete <id>       Delete a note (or 'remove')");
        System.out.println("  help              Show this help message");
        System.out.println("  exit              Quit the app\n");
    }
}