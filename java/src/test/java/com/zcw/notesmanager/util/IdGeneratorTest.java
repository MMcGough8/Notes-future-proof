package com.zcw.notesmanager.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class IdGeneratorTest {

    private IdGenerator idGenerator;

    @BeforeEach
    void setUp() {
        idGenerator = new IdGenerator();
    }

    @Test
    void testGenerateIdReturnsCorrectLength() {
        String id = idGenerator.generateId();
 
        assertNotNull(id);
        assertEquals(8, id.length(), "ID should be exactly 8 characters");
    }
    
    @Test
    void testGenerateIdContainsOnlyValidCharacters() {
        String id = idGenerator.generateId();

        assertTrue(id.matches("[a-z0-9]{8}"), "ID should only contain lowercase letters and numbers");
    }
    
    @Test
    void testGenerateIdIsUnique() {
        String id1 = idGenerator.generateId();
        String id2 = idGenerator.generateId();
        String id3 = idGenerator.generateId();

        assertNotEquals(id1, id2, "IDs should be unique");
        assertNotEquals(id2, id3, "IDs should be unique");
        assertNotEquals(id1, id3, "IDs should be unique");
    }
    
    @Test
    void testGenerateIdWithTitleHint() {
        String title = "My Test Note";

        String id = idGenerator.generateId(title);

        assertNotNull(id);
        assertTrue(id.startsWith("myte"), "ID should start with first 4 chars of cleaned title");
        assertEquals(8, id.length(), "ID should be 8 characters total (4 title + 4 random)");
        assertTrue(id.matches("[a-z0-9]{8}"), "ID should only contain lowercase letters and numbers");
    }
    
    @Test
    void testGenerateIdWithShortTitle() {
        String title = "Hi";
        
        String id = idGenerator.generateId(title);
        
        assertNotNull(id);
        assertTrue(id.startsWith("hi"), "ID should start with the short title");
        assertEquals(6, id.length(), "ID should be 2 (title) + 4 (random) = 6 characters");
    }
    
    @Test
    void testGenerateIdWithLongTitle() {
        String title = "This is a very long title that should be truncated";

        String id = idGenerator.generateId(title);
        
        assertNotNull(id);
        assertTrue(id.startsWith("this"), "ID should only use first 4 chars");
        assertEquals(8, id.length(), "ID should be 4 (title) + 4 (random) = 8 characters");
    }
    
    @Test
    void testGenerateIdWithSpecialCharacters() {
        String title = "My@#$Test!!!";
        
        String id = idGenerator.generateId(title);
        
        assertNotNull(id);
        assertTrue(id.startsWith("myte"), "Special characters should be removed");
        assertEquals(8, id.length(), "ID should be 8 characters");
        assertFalse(id.contains("@"), "Should not contain special characters");
        assertFalse(id.contains("#"), "Should not contain special characters");
        assertFalse(id.contains("!"), "Should not contain special characters");
    }
    
    @Test
    void testGenerateIdWithSpaces() {
        String title = "Test Note";
        
        String id = idGenerator.generateId(title);
        
        assertNotNull(id);
        assertTrue(id.startsWith("test"), "Spaces should be removed");
        assertEquals(8, id.length(), "ID should be 8 characters");
        assertFalse(id.contains(" "), "Should not contain spaces");
    }
    
    @Test
    void testGenerateIdWithNullTitle() {
        String id = idGenerator.generateId(null);
        
        assertNotNull(id);
        assertEquals(8, id.length(), "Should fall back to 8-char random ID");
        assertTrue(id.matches("[a-z0-9]{8}"), "Should be valid random ID");
    }
    
    @Test
    void testGenerateIdWithEmptyTitle() {
        String id = idGenerator.generateId("");
        
        assertNotNull(id);
        assertEquals(8, id.length(), "Should fall back to 8-char random ID");
        assertTrue(id.matches("[a-z0-9]{8}"), "Should be valid random ID");
    }
    
    @Test
    void testGenerateIdWithBlankTitle() {
        String id = idGenerator.generateId("   ");
        
        assertNotNull(id);
        assertEquals(8, id.length(), "Should fall back to 8-char random ID");
        assertTrue(id.matches("[a-z0-9]{8}"), "Should be valid random ID");
    }
    
    @Test
    void testGenerateIdWithOnlySpecialCharacters() {
        String title = "@#$%^&*()";
        
        String id = idGenerator.generateId(title);
        
        assertNotNull(id);
        assertEquals(8, id.length(), "Should fall back to 8-char random ID when no valid chars");
        assertTrue(id.matches("[a-z0-9]{8}"), "Should be valid random ID");
    }
    
    @Test
    void testGenerateIdWithNumbers() {
        String title = "Test123";
        
        String id = idGenerator.generateId(title);
        
        assertNotNull(id);
        assertTrue(id.startsWith("test"), "Should include numbers from title");
        assertEquals(8, id.length(), "ID should be 8 characters");
    }
    
    @Test
    void testGenerateIdWithMixedCase() {
        String title = "TeSt";
        
        String id = idGenerator.generateId(title);
        
        assertNotNull(id);
        assertTrue(id.startsWith("test"), "Should be lowercase");
        assertEquals(8, id.length(), "ID should be 8 characters");
        assertTrue(id.equals(id.toLowerCase()), "ID should be all lowercase");
    }
    
    @Test
    void testMultipleIdsWithSameTitleAreDifferent() {

        String title = "Test Note";

        String id1 = idGenerator.generateId(title);
        String id2 = idGenerator.generateId(title);
        String id3 = idGenerator.generateId(title);
        

        assertNotEquals(id1, id2, "IDs with same title should have different random suffixes");
        assertNotEquals(id2, id3, "IDs with same title should have different random suffixes");
        assertNotEquals(id1, id3, "IDs with same title should have different random suffixes");
    }
    
    @Test
    void testGenerateIdConsistentFormat() {
        for (int i = 0; i < 100; i++) {
            String id = idGenerator.generateId();
            
            assertEquals(8, id.length(), "Every ID should be 8 characters");
            assertTrue(id.matches("[a-z0-9]{8}"), "Every ID should match format");
        }
    }
    
    @Test
    void testGenerateIdWithTitleConsistentFormat() {
        for (int i = 0; i < 100; i++) {
            String id = idGenerator.generateId("Test");

            assertTrue(id.startsWith("test"), "Should always start with 'test'");
            assertEquals(8, id.length(), "Should always be 8 characters");
            assertTrue(id.matches("[a-z0-9]{8}"), "Should always match format");
        }
    }
}