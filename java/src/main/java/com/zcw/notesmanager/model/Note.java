package com.zcw.notesmanager.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Note {
    private String id;
    private String title;
    private Instant created;
    private Instant modified;
    private String content;
    private List<String> tags;
    private String author;
    private String status;
    private Integer priority;

    public Note(String id, String title, Instant created, 
                Instant modified, String content) {
        this.id = id;
        this.title = title;
        this.created = created;
        this.modified = modified;
        this.content = content;
        this.tags = new ArrayList<>();
    }

     public Note(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.created = Instant.now();
        this.modified = Instant.now();
        this.tags = new ArrayList<>();
    }

  
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Instant getCreated() { return created; }
    public void setCreated(Instant created) { this.created = created; }

    public Instant getModified() { return modified; }
    public void setModified(Instant modified) { this.modified = modified; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }

    @Override
    public String toString() {
        return "Note{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", created=" + created +
                ", modified=" + modified +
                ", tags=" + tags +
                '}';
    }
}