package com.zcw.notesmanager.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Note {
    private String id;
    private String title;
    private LocalDateTime created;
    private LocalDateTime modified;
    private String content;
    private List<String> tags;
    private String author;
    private String status;
    private Integer priority;

    public Note(String id, String title, LocalDateTime created, 
                LocalDateTime modified, String content) {
        this.id = id;
        this.title = title;
        this.created = created;
        this.modified = modified;
        this.content = content;
        this.tags = new ArrayList<>();
    }

  
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDateTime getCreated() { return created; }
    public void setCreated(LocalDateTime created) { this.created = created; }

    public LocalDateTime getModified() { return modified; }
    public void setModified(LocalDateTime modified) { this.modified = modified; }

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