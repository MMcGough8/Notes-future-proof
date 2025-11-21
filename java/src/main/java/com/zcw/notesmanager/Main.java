package com.zcw.notesmanager;

import com.zcw.notesmanager.cli.CommandParser;
import com.zcw.notesmanager.cli.commands.CreateCommand;
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
            System.out.println("Personal Notes Manager");
            System.out.println("Type 'help' for commands or 'exit' to quit\n");
            
            while (true) {
                System.out.print("> ");
                String input = scanner.nextLine().trim();
                
                if (input.isEmpty()) {
                    continue;
                }
                
                if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
                    System.out.println("Goodbye!");
                    break;
                }
                
                String[] commandArgs = input.split("\\s+");
                
                try {
                    executeCommand(commandArgs, parser, noteService, scanner);
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                    System.out.println("Type 'help' for usage information\n");
                }
            }
            
        } catch (Exception e) {
            System.err.println("Fatal error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void executeCommand(String[] args, CommandParser parser, NoteService noteService, Scanner scanner) {
        CommandParser.ParsedCommand parsed = parser.parse(args);
        
        switch (parsed.getCommand()) {
            case "create":
                CreateCommand createCommand = new CreateCommand(noteService, scanner);
                createCommand.execute();
                break;
                
            case "help":
                showHelp();
                break;
                
            default:
                System.out.println("Unknown command: " + parsed.getCommand());
                System.out.println("Type 'help' for available commands");
        }
    }
    
    private static void showHelp() {
        System.out.println("\nAvailable commands:");
        System.out.println("  create          - Create a new note");
        System.out.println("  help            - Show this help message");
        System.out.println("  exit            - Exit the program");
        System.out.println();
    }
}