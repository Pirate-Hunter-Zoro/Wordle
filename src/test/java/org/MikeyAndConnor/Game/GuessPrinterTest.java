package org.MikeyAndConnor.Game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GuessPrinterTest {

    /** We need a GuessPrinter to test */
    private static GuessPrinter printer;

    /**
     * Setup of each GuessPrinter object
     */
    @BeforeEach
    void setUp() {
        printer = new GuessPrinter("HELLO");
    }

    /**
     * A simple test to ensure that when handed a guess, the GuessPrinter's guessResult() method behaves as expected
     */
    @Test
    void getResult() {
        // to take on different values
        String guess;

        // make sure correct character guesses and placements are shown correctly
        guess = "JELLO";
        assertEquals("_****", printer.getResult(guess));

        // make sure non-in-place but still correct characters are represented correctly
        guess = "LOYAL";
        assertEquals("--__-", printer.getResult(guess));

        // make sure characters are not counted too many times
        guess = "LELLO";
        assertEquals("_****", printer.getResult(guess));

        // make sure a correct guess results in all asterisks
        guess = "HELLO";
        assertEquals("*****", printer.getResult(guess));

    }
}