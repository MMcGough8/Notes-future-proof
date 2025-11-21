package com.zcw.notesmanager.util;

import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;

class IdGeneratorTest {

    @Test
    void testGenerateTimestampBasedId() {
        IdGenerator idGenerator = new IdGenerator();
        
        String id1 = idGenerator.generateId();
        String id2 = idGenerator.generateId();
            assertNotNull(id1);
            assertNotNull(id2);
            assertNotEquals(id1, id2);
    
        assertTrue(id1.matches("\\d{8}-\\d{6}-.*"));
    }
    
    @Test
    void testGenerateIdWithTitle() {

        IdGenerator idGenerator = new IdGenerator();
        String title = "My Test Note";
        

        String id = idGenerator.generateId(title);
            assertNotNull(id);
            assertTrue(id.contains("my-test-note"));
            assertTrue(id.matches("\\d{8}-\\d{6}-my-test-note"));
    }
    
    @Test
    void testSanitizeTitle() {
        IdGenerator idGenerator = new IdGenerator();
            assertEquals("hello-world", idGenerator.sanitizeTitle("Hello World"));
            assertEquals("hello-world", idGenerator.sanitizeTitle("Hello  World!!!"));
            assertEquals("test-123", idGenerator.sanitizeTitle("Test@#$123"));
            assertEquals("note", idGenerator.sanitizeTitle("!@#$%Note"));
    }
    
    @Test
    void testGenerateIdWithLongTitle() {

        IdGenerator idGenerator = new IdGenerator();
        String longTitle = "This is a very long title that should be truncated to a reasonable length";
        
        String id = idGenerator.generateId(longTitle);
        
        assertTrue(id.length() < 100);
    }
}