/* *****************************************
 * CSCI205 -Software Engineering and Design
 * Fall2022
 * Instructor: Prof. Brian King
 *
 * Name: Mikey Ferguson and Connor Coles
 * Section: 02
 * Date: 10/6/22
 * Time: 12:23PM
 *
 * Project: csci205_hw
 * Package: org.MikeyAndConnor.Game
 * Class: Game
 *
 * Description: Implementation of the class which is responsible for taking in user Guesses and printing them out
 *
 * *****************************************/

package org.MikeyAndConnor.Game;

import org.MikeyAndConnor.WordBank.WordBank;

import java.util.Scanner;
import java.util.TreeSet;

public class Game {
    /** The minimum length for a word*/
    public static final int MIN_LENGTH = 3;
    /** The maximum length for a word */
    public static final int MAX_LENGTH = 7;
    /** The amount of guesses that a user had taken */

    /** Private attribute which will be useful when prompting the user for a guess */
    private int wordLength;
    /** Guesses entered by the user thus far */
    private int guesses = 0;
    /** Length of word answer plus 1 */
    private int guessLimit;
    /** A TreeSet of words found in our dictionary*/
    private TreeSet<String> words;
    /** A word that is randomly selected from our dictionary of words */
    private String answer;
    /** A GuessPrinter object */
    private GuessPrinter printer;

    /**
     * Set all the members of our Game instance
     * @param wordLength {@link int}
     * @throws IllegalArgumentException
     */
    public Game(int wordLength) throws IllegalArgumentException{
        // throw an Exception if we need to
        if (wordLength < MIN_LENGTH || wordLength > MAX_LENGTH){
            throw new IllegalArgumentException("Word length must be between "+ MIN_LENGTH +" - "+ MAX_LENGTH);
        }

        // set the words member
        this.words = WordBank.collectWords(wordLength);

        // store the word length
        this.wordLength = wordLength;

        // select a random answer
        this.answer = selectRandomWord(this.words);

        // set the printer attribute
        this.printer = new GuessPrinter(this.answer);

        // our rule for the number of guesses a user is allowed
        this.guessLimit = wordLength + 1;
    }

    /**
     * Helper method to select a random element from a TreeSet
     * @param words {@link TreeSet<String> words}
     * @return {@link String}
     */
    private static String selectRandomWord(TreeSet<String> words){
        int magicIndex = (int)(words.size()*Math.random());
        int currentIndex = -1;
        for (String word : words){
            currentIndex++;
            if (currentIndex == magicIndex)
                return word;
        }
        return "This will never be returned if I did the math correctly";
    }

    /**
     * Allows the user to keep guessing as long as they are within the guess limit.
     * If they guess the word the program ends, if they reach the guessLimit, the program
     * ends.
     */
    public void playGame(){
        // the user guesses until they win or run out of guesses (whichever comes first)
        while(this.guesses < this.guessLimit){
            // get a guess and increment
            String guess = getGuess();
            reportGuess(guess.toUpperCase());
            // if the answer equals the guess
            if(this.answer.equalsIgnoreCase(guess)){
            // if the answer equals the guess
                System.out.println("You guesses the word lol: " + this.answer);
                return;
            }

        }
        System.out.println("YOU SUCK. JK. The word was: " + this.answer);



    }

    /**
     * getGuess keeps prompting the user for a guess until they provide
     * a word that is in our provided dictionary
     * @return guess - A possible {@link String} that the user enters
     *                 has to be contained in our TreeMap of words
     *
     */
    private String getGuess(){
        boolean done = false;
        Scanner scnr = new Scanner(System.in);


        while(!done) {
            //going through our list of valid words
            System.out.print("Enter a guess for a word of length " + this.wordLength + ": ");

            String userGuess = scnr.next();
            if (this.words.contains(userGuess.toUpperCase())){
                this.guesses++;
                return userGuess;
            } else {
                System.out.println("Not a valid guess try again");
                scnr.nextLine();
            }

        }
        return "Should never get here";

    }

    /**
     * Calls getResult from the GuessPrinter class, showing the user
     * how close their guess was to the answer.
     * @param guess- guess provided by user from getGuess
     */
    private void reportGuess(String guess){
        //prints the results of getResult with a given guess
        System.out.println(this.printer.getResult(guess));
    }

}
