package com.zcw.notesmanager.util;

import java.util.Random;


public class IdGenerator {

    private static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
    private static final int ID_LENGTH = 8;
    private static final Random random = new Random();

    public String generateId() {
        char[] result = new char[ID_LENGTH];
        for (int i = 0; i < ID_LENGTH; i++) {
            result[i] = CHARS[random.nextInt(CHARS.length)];
        }
        return new String(result);
    }

    public String generateId(String titleHint) {
        if (titleHint != null && !titleHint.isBlank()) {
            String clean = titleHint.toLowerCase()
                    .replaceAll("[^a-z0-9]+", "")
                    .replaceAll("^(.{0,4}).*", "$1");

            if (!clean.isEmpty()) {
                return clean + generateRandomSuffix(4);
            }
        }
        return generateId();
    }

    private String generateRandomSuffix(int length) {
        char[] suffix = new char[length];
        for (int i = 0; i < length; i++) {
            suffix[i] = CHARS[random.nextInt(CHARS.length)];
        }
        return new String(suffix);
    }
}