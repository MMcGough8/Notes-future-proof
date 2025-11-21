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
            throw new IOException("File does not exist: " + filePath.toAbsolutePath());
        }
        return Files.readString(filePath);
    }

    public String[] separateYamlAndContent(String fileContent) {
        if (fileContent == null || fileContent.trim().isEmpty()) {
            throw new IllegalArgumentException("File content cannot be null or empty");
        }

        String[] parts = fileContent.split("---", 3);

        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid note format: expected at least two '---' delimiters");
        }

        String yamlHeader = parts[1].trim();
        String content = parts[2].stripLeading();

        return new String[] { yamlHeader, content };
    }

    public void writeNote(Path filePath, String yamlHeader, String content) throws IOException {
        if (filePath == null) throw new IllegalArgumentException("File path cannot be null");
        if (yamlHeader == null) throw new IllegalArgumentException("YAML header cannot be null");

        String noteText = """
                ---
                %s
                ---
                
                %s""".formatted(yamlHeader.strip(), content != null ? content : "");

        Path parent = filePath.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }

        Files.writeString(filePath, noteText);
    }

    public void updateNote(Path filePath, String yamlHeader, String content) throws IOException {
        if (filePath == null) throw new IllegalArgumentException("File path cannot be null");
        if (!Files.exists(filePath)) {
            throw new IOException("Cannot update non-existent note: " + filePath);
        }
        writeNote(filePath, yamlHeader, content);
    }

    public void deleteNote(Path filePath) throws IOException {
        if (filePath == null) throw new IllegalArgumentException("File path cannot be null");
        if (!Files.exists(filePath)) {
            throw new IOException("Cannot delete: file does not exist: " + filePath);
        }
        Files.delete(filePath);
    }
}