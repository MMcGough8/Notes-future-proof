package com.zcw.notesmanager.cli;

public class CommandParser {
    
    public ParsedCommand parse(String[] args) {
        if (args == null || args.length == 0) {
            throw new IllegalArgumentException("No command provided");
        }
        
        String commandName = args[0].toLowerCase();
        
        String[] commandArgs = new String[args.length - 1];
        System.arraycopy(args, 1, commandArgs, 0, args.length - 1);
        
        return new ParsedCommand(commandName, commandArgs);
    }
    
    public static class ParsedCommand {
        private final String command;
        private final String[] arguments;
        
        public ParsedCommand(String command, String[] arguments) {
            this.command = command;
            this.arguments = arguments;
        }
        
        public String getCommand() {
            return command;
        }
        
        public String[] getArguments() {
            return arguments;
        }
        
        public String getArgument(int index) {
            if (index >= 0 && index < arguments.length) {
                return arguments[index];
            }
            return null;
        }
        
        public boolean hasArguments() {
            return arguments.length > 0;
        }
    }
}