/* *****************************************
 * CSCI205 -Software Engineering and Design
 * Fall2022
 * Instructor: Prof. Brian King
 *
 * Name: Mikey Ferguson and Connor Coles
 * Section: 02
 * Date: 10/6/22
 * Time: 12:57PM
 *
 * Project: csci205_hw
 * Package: org.MikeyAndConnor.Game
 * Class: GuessPrinter
 *
 * Description: Class responsible for printing out the match-reporting string given a user's guess
 *
 * *****************************************/

package org.MikeyAndConnor.Game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class GuessPrinter {

    public static Object getResult;
    /** The correct word the user should be trying to guess */
    private final String answer;

    /**
     * Initialization of the GuessPrinter class just means setting the answer member
     * This class need only every be initialized within the Game package
     * @param answer {@link String}
     */
    protected GuessPrinter(String answer){
        this.answer = answer;
    }

    /**
     * Method to print out the result of a guess compared to the actual word
     * Should give the user clues whether letters are present and/or in the right place
     * No need for this method to be called outside the Game package
     * @param guess {@link String}
     */
    protected String getResult(String guess) {
        // this method will never be called with a guess of different length than the actual answer

        // break up the answer and guess into arrays
        char[] answerArray = this.answer.toCharArray();
        char[] guessArray = guess.toCharArray();

        // we need to keep track of the number of times each character in answerArray can match
        HashMap<Character, Integer> maxMatchFrequencies = new HashMap<>();
        for (char c : answerArray){
            if (maxMatchFrequencies.containsKey(c))
                maxMatchFrequencies.put(c, maxMatchFrequencies.get(c)+1);
            else
                maxMatchFrequencies.put(c, 1);
        }

        // also keep track of how many matches have currently been found of each character in answerArray
        HashMap<Character, Integer> currentMatchFrequencies = new HashMap<>();
        for (char c : answerArray){
            currentMatchFrequencies.put(c, 0);
        }

        // matching is too complicated to shove in this method
        return matchAlgorithm(answerArray, guessArray, maxMatchFrequencies, currentMatchFrequencies);
    }


    /**
     * An algorithm to return the result clue to the user given their guess and the answer
     * @param answerArray {@link char[]}
     * @param guessArray {@link char[]}
     * @param maxMatchFrequencies {@link Map<Character,Integer>}
     * @param currentMatchFrequencies {@link Map<Character,Integer>}
     * @return {@link String}
     */
    private static String matchAlgorithm(char[] answerArray, char[] guessArray, Map<Character, Integer> maxMatchFrequencies, Map<Character, Integer> currentMatchFrequencies){
        // result initialization
        char[] result = new char[answerArray.length];

        HashSet<Integer> indecesLeftToExplore = new HashSet<>();

        // first we must prioritize showing the user if their guess had any correct letters in the correct places
        for (int i=0; i<answerArray.length; i++) {
            indecesLeftToExplore.add(i);
            // if the letter lines up correctly, then we for sure want an asterisk
            // also count the match
            if (answerArray[i] == guessArray[i]) {
                result[i] = '*';
                currentMatchFrequencies.put(answerArray[i], currentMatchFrequencies.get(answerArray[i]) + 1);
                indecesLeftToExplore.remove(i);
            }
        }

        // now, show if there are any correct letters in the incorrect places remaining
        for (int i : indecesLeftToExplore) {
            char guessChar = guessArray[i];
            // if the character is still present in the target word, and it has not been matched as many times as it appears in the target word
            if (maxMatchFrequencies.containsKey(guessChar)){
                int numMatchesSoFar = currentMatchFrequencies.get(guessChar);
                //  can we match this character anymore or is it just an extra?
                if (numMatchesSoFar < maxMatchFrequencies.get(guessChar)) {
                    result[i] = '-';
                    // also keep track of the matched character count of this character
                    currentMatchFrequencies.put(guessChar, numMatchesSoFar + 1);
                } else {
                    result[i] = '_';
                }
            } else{
                // it was just a plain shit guess
                result[i] = '_';
            }

        }

        // concatenate our array of characters into a string
        String matchReport = "";
        for (char c : result)
            matchReport += c;

        // source for converting char[] to String: https://www.geeksforgeeks.org/convert-character-array-to-string-in-java/
        return matchReport;
    }

}