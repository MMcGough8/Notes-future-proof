package com.zcw.notesmanager.service;

import com.zcw.notesmanager.model.Note;
import com.zcw.notesmanager.util.IdGenerator;

import java.io.IOException;
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
}
