package com.zcw.notesmanager.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileService {
    
    public String readFile(Path filePath) throws IOException {
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }
        
        if (!Files.exists(filePath)) {
            throw new IOException("File does not exist: " + filePath);
        }
        
        return Files.readString(filePath);
    }
    
    public String[] separateYamlAndContent(String fileContent) {
        if (fileContent == null || fileContent.trim().isEmpty()) {
            throw new IllegalArgumentException("File content cannot be null or empty");
        }
        
        String[] parts = fileContent.split("---", 3);
        
        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid note format: missing YAML delimiters");
        }
        
        String yamlHeader = parts[1].trim();
        String content = parts[2].trim();
        
        return new String[] { yamlHeader, content };
    }
}