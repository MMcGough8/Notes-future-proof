package com.zcw.notesmanager.util;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class IdGenerator {
    
    private static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").withZone(ZoneOffset.UTC);
    
    private static final int MAX_TITLE_LENGTH = 50;
    
    public String generateId() {
        String timestamp = DATE_FORMATTER.format(Instant.now());
        String randomPart = UUID.randomUUID().toString().substring(0, 8);
        return timestamp + "-" + randomPart;
    }
    
    public String generateId(String title) {
        String timestamp = DATE_FORMATTER.format(Instant.now());
        String sanitized = sanitizeTitle(title);

        if (sanitized.length() > MAX_TITLE_LENGTH) {
            sanitized = sanitized.substring(0, MAX_TITLE_LENGTH);
        }
        
        return timestamp + "-" + sanitized;
    }
    
    public String sanitizeTitle(String title) {
    if (title == null || title.trim().isEmpty()) {
        return "note";
    }
    
    return title.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", " ")
                .trim()
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
    }
}
