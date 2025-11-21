package com.zcw.notesmanager.service;

import com.zcw.notesmanager.model.Note;
import com.zcw.notesmanager.util.IdGenerator;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteService {
    
    private final FileService fileService;
    private final YamlService yamlService;
    private final IdGenerator idGenerator;
    private final Path notesDirectory;
    
    public NoteService(Path notesDirectory) {
        this.fileService = new FileService();
        this.yamlService = new YamlService();
        this.idGenerator = new IdGenerator();
        this.notesDirectory = notesDirectory;
    }
    
    public Note createNote(String title, String content) throws IOException {
        String noteId = idGenerator.generateId(title);
        Note note = new Note(noteId, title, content);
        saveNote(note);
        return note;
    }
    
    public List<Note> listAllNotes() throws IOException {
        List<Note> notes = new ArrayList<>();
        
        if (!Files.exists(notesDirectory)) {
            return notes;
        }
        
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(notesDirectory, "*.note")) {
            for (Path noteFile : stream) {
                try {
                    Note note = loadNote(noteFile);
                    notes.add(note);
                } catch (Exception e) {
                    System.err.println("Warning: Could not load " + noteFile.getFileName() + ": " + e.getMessage());
                }
            }
        }

        notes.sort((n1, n2) -> n2.getCreated().compareTo(n1.getCreated()));
        
        return notes;
    }

    private Note loadNote(Path noteFile) throws IOException {

        String fileContent = fileService.readFile(noteFile);
        

        String[] parts = fileService.separateYamlAndContent(fileContent);
        String yamlHeader = parts[0];
        String content = parts[1];
        
        Map<String, Object> metadata = yamlService.parseYaml(yamlHeader);
        
        String filename = noteFile.getFileName().toString();
        String noteId = filename.substring(0, filename.lastIndexOf('.'));
        
        String title = (String) metadata.get("title");
        Instant created = Instant.parse((String) metadata.get("created"));
        Instant modified = Instant.parse((String) metadata.get("modified"));
        
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
    
    private void saveNote(Note note) throws IOException {
        Map<String, Object> noteData = new HashMap<>();
        noteData.put("title", note.getTitle());
        noteData.put("created", note.getCreated().toString());
        noteData.put("modified", note.getModified().toString());
        
        if (note.getTags() != null && !note.getTags().isEmpty()) {
            noteData.put("tags", note.getTags());
        }
        if (note.getAuthor() != null) {
            noteData.put("author", note.getAuthor());
        }
        if (note.getStatus() != null) {
            noteData.put("status", note.getStatus());
        }
        if (note.getPriority() != null) {
            noteData.put("priority", note.getPriority());
        }
        
        String yaml = yamlService.writeYaml(noteData);
        Path noteFile = notesDirectory.resolve(note.getId() + ".note");
        fileService.writeNote(noteFile, yaml, note.getContent());
    }
}