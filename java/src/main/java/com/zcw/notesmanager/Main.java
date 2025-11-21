package com.zcw.notesmanager;

import com.zcw.notesmanager.cli.CommandParser;
import com.zcw.notesmanager.cli.commands.CreateCommand;
import com.zcw.notesmanager.service.NoteService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: notes <command> [options]");
            System.out.println("Try 'notes --help' for more information.");
            return;
        }

        try {
            Path notesDir = Path.of("notes");
            if (!Files.exists(notesDir)) {
                Files.createDirectories(notesDir);
            }
            
            CommandParser parser = new CommandParser();
            CommandParser.ParsedCommand parsed = parser.parse(args);
            
            NoteService noteService = new NoteService(notesDir);
            Scanner scanner = new Scanner(System.in);
            
            if ("create".equals(parsed.getCommand())) {
                CreateCommand createCommand = new CreateCommand(noteService, scanner);
                createCommand.execute();
            } else {
                System.out.println("Unknown command: " + parsed.getCommand());
                System.out.println("Available commands: create");
            }
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}