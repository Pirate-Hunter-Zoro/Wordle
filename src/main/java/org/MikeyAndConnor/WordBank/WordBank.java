/* *****************************************
 * CSCI205 -Software Engineering and Design
 * Fall2022
 * Instructor: Prof. Brian King
 *
 * Name: Mikey Ferguson and Connor Coles
 * Section: 02
 * Date: 10/5/22
 * Time: 5:21PM
 *
 * Project: csci205_hw
 * Package: org.MikeyAndConnor.WordBank
 * Class: WordBank
 *
 * Description: A class responsible for reading in words from a giant dictionary, and filtering them according to only the ones common enough to appear in a certain collection of novels
 *
 * *****************************************/

package org.MikeyAndConnor.WordBank;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class WordBank {

    /** An array of {@link String}s to store the URLs of the novels we will use to filter for common words */
    public static final String[] BOOK_LINKS = {"https://www.gutenberg.org/files/69096/69096-h/69096-h.htm", "https://www.gutenberg.org/files/69088/69088-h/69088-h.htm", "https://www.gutenberg.org/files/69090/69090-h/69090-h.htm", "https://www.gutenberg.org/files/69086/69086-h/69086-h.htm", "https://www.gutenberg.org/files/69087/69087-h/69087-h.htm"};

    /** A {@link String} which is the link to the source dictionary */
    public static final String DICTIONARY_LINK = "https://www.gutenberg.org/cache/epub/29765/pg29765.txt";

    /** Regular expression to search for words of the necessary length in the source dictionary */
    private static String dictMatcher;

    /** Regular expression to search for words of the necessary length in a given book */
    private static String bookMatcher;

    /** The name of the file which our filtered collection of words will be written to */
    public static final String FILE_NAME = "words.txt";

    /** Map to all the word lengths and their corresponding word banks*/
    private static HashMap<Integer, TreeSet<String>> wordLengthMap = new HashMap<>();

    // NOTE - the default constructor for WordBank() will suffice - we're never even going to need to call it

    /**
     * Returns a filtered collection of words read from a some assortment of books, only keeping the words present in the dictionary
     * @return collection {@link TreeSet<String>}
     */
    public static TreeSet<String> collectWords(int length) {

        /** Has this length already been asked for? */
        if (wordLengthMap.containsKey(length))
            return wordLengthMap.get(length);

        /** Set our regular expression attributes according to desired word length */
        dictMatcher = "((?<=\\s)[A-Z]{" + length + "}(?=\\s))";
        bookMatcher = "((?<=\\s)[a-z]{" + length + "}(?=[\\s.;!?]))";

        // give the user moral support
        System.out.println("Expect the scanning of the books and dictionary to take around three minutes...");

        // obtain all words of correct length
        System.out.println("Scanning dictionary... be patient! You will only have to wait for this once for the word length you entered.");
        TreeSet<String> collection = gatherFromDict();
        System.out.println("Done scanning dictionary.");

        // obtain all 5-letter strings in books, and remove the ones that are not present in the dictionary
        TreeSet<String> newWordBank = filterBookWords(collection);

        // write every word in our wordBank to this class's output file
        writeToFile(newWordBank);

        // record this newWordBank
        wordLengthMap.put(length, newWordBank);

        // return our filtered collection
        return newWordBank;
    }

    /**
     * Private method to collect all words of a given length from a dictionary - there should be no need to call this method outside this class
     * No need for calls to this method outside this class
     * @return collection {@link TreeSet<String>}
     */
    private static TreeSet<String> gatherFromDict() {

        // otherwise
        TreeSet<String> collection = new TreeSet<>();

        // we need an input stream to read from our dictionary
        try (BufferedInputStream dictReader = new BufferedInputStream((new URL(DICTIONARY_LINK)).openStream())){
            scanAndAdd(collection, dictReader, dictMatcher);
        } catch (IOException e){
            e.printStackTrace();
        }

        return collection;
    }

    /**
     * Collects all words of correct length that are present in the inputted dictionary of words
     * No need for calls to this method outside this class
     * @param dictionary {@link TreeSet<String>}
     * @return words {@link TreeSet<String>}
     */
    private static TreeSet<String> filterBookWords(TreeSet<String> dictionary) {
        // for each word, check if it's in any of the books - if not remove it
        TreeSet<String> words = findBookWords();
        // to do this we will use an Iterator
        // source: https://www.baeldung.com/java-concurrentmodificationexception
        for (Iterator<String> iterator = words.iterator(); iterator.hasNext();){
            if (!dictionary.contains(iterator.next())) // string of correct length is not a real word
                iterator.remove();
        }
        return words;
    }

    /**
     * Returns a set of all the unique words of the correct length from all the books
     * No need for calls to this method outside this class
     * @return words {@link TreeSet<String>}
     */
    private static TreeSet<String> findBookWords() {

        // if not then search
        TreeSet<String> words = new TreeSet<>();
        for (String book : BOOK_LINKS) {
            System.out.println("Scanning Book: " + book);
            addWords(words, book);
            System.out.println("Done Scanning Book: " + book);
        }

        // return
        return words;
    }

    /**
     * Given a link to a book, add all words within this book of the correct length which are not already in the 'words' TreeSet
     * No need for calls to this method outside this class
     * @param words {@link TreeSet<String>}
     * @param book {@link String}
     */
    private static void addWords(TreeSet<String> words, String book) {
        // create an input stream, and use said stream to add the words in the book to the TreeSet
        try (BufferedInputStream bookReader = new BufferedInputStream((new URL(book)).openStream())){
            scanAndAdd(words, bookReader, bookMatcher);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Helper method to add Strings to a set given a BufferedInputStream to read from given a Regex filter
     * No need for calls to this method outside this class
     * @param set {@link TreeSet<String>}
     * @param reader {@link BufferedInputStream}
     * @param filter {@link String}
     */
    private static void scanAndAdd(TreeSet<String> set, BufferedInputStream reader, String filter){
        // create a Scanner to scan the BufferedInputStream and add words that match the filter to the set
        Scanner scanner = new Scanner(reader);

        // create a Pattern matcher to scan for Strings that match the input regular expression
        Pattern filterMatcher = Pattern.compile(filter);

        // now keep matching until we can't anymore
        while (scanner.findWithinHorizon(filterMatcher, 0) != null){
            MatchResult match = scanner.match(); // find the match
            String word = match.group(1); // get a hold of the word
            set.add(word.toUpperCase()); // add the word to the set
        }

    }

    /**
     * Helper method to write all the words within a given set to this class's text file
     * No need for calls to this method outside this class
     * @param wordBank {@link TreeSet<String>}
     */
    private static void writeToFile(TreeSet<String> wordBank) {
        File file = new File(FILE_NAME);
        // create a PrintStream to this file

        try (PrintStream fileWriter = new PrintStream(file)){
            // for every word in our wordBank, print it to the file
            for (String word : wordBank)
                fileWriter.println(word);
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }


    }

}