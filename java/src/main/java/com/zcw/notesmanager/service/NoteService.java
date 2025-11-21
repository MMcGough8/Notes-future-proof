package com.zcw.notesmanager.service;

import com.zcw.notesmanager.model.Note;
import com.zcw.notesmanager.util.IdGenerator;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.HashMap;
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