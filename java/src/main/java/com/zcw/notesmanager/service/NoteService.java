package com.zcw.notesmanager.service;

import com.zcw.notesmanager.model.Note;
import com.zcw.notesmanager.util.IdGenerator;

import java.io.IOException;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.nio.file.*;
import java.util.*;

public class NoteService {

    private final FileService fileService = new FileService();
    private final YamlService yamlService = new YamlService();
    private final IdGenerator idGenerator = new IdGenerator();
    private final Path notesDirectory;

    public NoteService(Path notesDirectory) {
        this.notesDirectory = notesDirectory;
    }

    public Note createNote(String title, String content) throws IOException {
        String noteId = idGenerator.generateId(title);
        Note note = new Note(noteId, title, content);
        saveNote(note);
        return note;
    }

    private void saveNote(Note note) throws IOException {
        Map<String, Object> noteData = new HashMap<>();
        noteData.put("title", note.getTitle());
        noteData.put("created", note.getCreated().toString());
        noteData.put("modified", note.getModified().toString());

        if (note.getTags() != null && !note.getTags().isEmpty()) {
            noteData.put("tags", note.getTags());
        }
        if (note.getAuthor() != null) noteData.put("author", note.getAuthor());
        if (note.getStatus() != null) noteData.put("status", note.getStatus());
        if (note.getPriority() != null) noteData.put("priority", note.getPriority());

        String yamlFrontMatter = yamlService.writeYaml(noteData);
        
        String sanitizedTitle = sanitizeTitle(note.getTitle());
        String filename = sanitizedTitle + "-" + note.getId() + ".note";
        Path noteFile = notesDirectory.resolve(filename);

        fileService.writeNote(noteFile, yamlFrontMatter, note.getContent());
    }

    private String sanitizeTitle(String title) {
        if (title == null || title.isEmpty()) {
            return "untitled";
        }
        
        String sanitized = title.toLowerCase()
            .replaceAll("[^a-z0-9]+", "-")
            .replaceAll("^-+|-+$", "");
        
        if (sanitized.length() > 30) {
            sanitized = sanitized.substring(0, 30).replaceAll("-+$", "");
        }
        
        return sanitized.isEmpty() ? "untitled" : sanitized;
    }
    public Note readNote(String noteId) throws IOException {
    // Find the note file that contains this ID
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(notesDirectory, "*.note")) {
        for (Path noteFile : stream) {
            String filename = noteFile.getFileName().toString();
            // Check if filename contains the note ID
            if (filename.contains(noteId)) {
                return loadNoteFromFile(noteFile);
            }
        }
    }
    
    throw new IOException("Note not found with ID: " + noteId);
}

public List<Note> listAllNotes() throws IOException {
    List<Note> notes = new ArrayList<>();
    
    if (!Files.exists(notesDirectory)) {
        return notes;
    }
    
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(notesDirectory, "*.note")) {
        for (Path noteFile : stream) {
            try {
                Note note = loadNoteFromFile(noteFile);
                notes.add(note);
            } catch (Exception e) {
                System.err.println("Warning: Could not load " + noteFile.getFileName() + ": " + e.getMessage());
            }
        }
    }
    
    return notes;
}

private Note loadNoteFromFile(Path noteFile) throws IOException {
    String fileContent = fileService.readFile(noteFile);
    String[] parts = fileService.separateYamlAndContent(fileContent);
    
    String yamlHeader = parts[0];
    String content = parts[1];
    
    Map<String, Object> metadata = yamlService.parseYaml(yamlHeader);
    
    String filename = noteFile.getFileName().toString();
    String noteId = filename.substring(filename.lastIndexOf('-') + 1, filename.lastIndexOf('.'));
    
    String title = (String) metadata.get("title");
    java.time.Instant created = java.time.Instant.parse((String) metadata.get("created"));
    java.time.Instant modified = java.time.Instant.parse((String) metadata.get("modified"));
    
    Note note = new Note(noteId, title, created, modified, content);
    
    if (metadata.containsKey("tags")) {
        note.setTags((List<String>) metadata.get("tags"));
    }
    if (metadata.containsKey("author")) {
        note.setAuthor((String) metadata.get("author"));
    }
    if (metadata.containsKey("status")) {
        note.setStatus((String) metadata.get("status"));
    }
    if (metadata.containsKey("priority")) {
        note.setPriority((Integer) metadata.get("priority"));
    }
    
    return note;
    }

    public void deleteNote(String noteId) throws IOException {
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(notesDirectory, "*.note")) {
        for (Path noteFile : stream) {
            String filename = noteFile.getFileName().toString();
            if (filename.contains(noteId)) {
                fileService.deleteNote(noteFile);
                return;
            }
        }
    }
    
    throw new IOException("Note not found with ID: " + noteId);
}

public void updateNote(String noteId, String newTitle, String newContent) throws IOException {
    Path noteFile = null;
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(notesDirectory, "*.note")) {
        for (Path file : stream) {
            String filename = file.getFileName().toString();
            if (filename.contains(noteId)) {
                noteFile = file;
                break;
            }
        }
    }
    
    if (noteFile == null) {
        throw new IOException("Note not found with ID: " + noteId);
    }
    
    Note existingNote = loadNoteFromFile(noteFile);
    
    existingNote.setTitle(newTitle);
    existingNote.setContent(newContent);
    existingNote.setModified(java.time.Instant.now());
    
    fileService.deleteNote(noteFile);

    saveNote(existingNote);
}

public void updateNote(String noteId, String newTitle, String newContent, List<String> tags) throws IOException {
    Path noteFile = null;
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(notesDirectory, "*.note")) {
        for (Path file : stream) {
            String filename = file.getFileName().toString();
            if (filename.contains(noteId)) {
                noteFile = file;
                break;
            }
        }
    }
    
    if (noteFile == null) {
        throw new IOException("Note not found with ID: " + noteId);
    }

    Note existingNote = loadNoteFromFile(noteFile);

    existingNote.setTitle(newTitle);
    existingNote.setContent(newContent);
    existingNote.setModified(java.time.Instant.now());
    
    if (tags != null) {
        existingNote.setTags(tags);
    }

    fileService.deleteNote(noteFile);

    saveNote(existingNote);
}

public List<Note> listNotesByTag(String tag) throws IOException {
    List<Note> allNotes = listAllNotes();
    List<Note> filteredNotes = new ArrayList<>();
    
    String searchTag = tag.toLowerCase().trim();
    
    for (Note note : allNotes) {
        if (note.getTags() != null) {
            for (String noteTag : note.getTags()) {
                if (noteTag.equalsIgnoreCase(searchTag)) {
                    filteredNotes.add(note);
                    break;
                }
            }
        }
    }
    
    return filteredNotes;
}

public List<String> getAllTags() throws IOException {
    List<Note> allNotes = listAllNotes();
    Set<String> uniqueTags = new HashSet<>();
    
    for (Note note : allNotes) {
        if (note.getTags() != null) {
            uniqueTags.addAll(note.getTags());
        }
    }
    
    List<String> sortedTags = new ArrayList<>(uniqueTags);
    Collections.sort(sortedTags);
    
    return sortedTags;
}
}