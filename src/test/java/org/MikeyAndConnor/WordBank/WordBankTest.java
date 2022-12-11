package org.MikeyAndConnor.WordBank;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class WordBankTest {

    /** A simple test to ensure that the collectWords method collects words of the correct length */
    @Test
    void collectWords() {
        TreeSet<String> words = WordBank.collectWords(5);
        for (String word : words){
            assertEquals(5, word.length());
        }

        words = WordBank.collectWords(4);
        for (String word : words){
            assertEquals(4, word.length());
        }

    }
}