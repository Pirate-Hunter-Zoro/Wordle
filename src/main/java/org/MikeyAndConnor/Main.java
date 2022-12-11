package org.MikeyAndConnor;

import org.MikeyAndConnor.Game.Game;

import java.util.Scanner;

public class Main {

    /** Main method */
    public static void main(String[] args) {
        // tell the user how to play
        displayRules();

        // ask the user for what word length they want, and play the game
        // let them keep playing until they do not want to anymore
        boolean quitting = false;
        do{
            Game game = getWordLengthAndCreateGame();
            System.out.println();
            game.playGame();
            quitting = !playAgain();
        } while (!quitting);
    }

    /**
     * Helper method to portray the rules of the game to the user
     */
    public static void displayRules(){
        System.out.println("After you select a word length, you'll get to play!");
        System.out.println("You have as many guesses as there are characters in the word PLUS 1");
        System.out.println("'*' means the character is in the word you are trying to guess and it is in the right place");
        System.out.println("'-' means the character is in the word you are trying to guess, but not in the correct place");
        System.out.println("'_' means the character is not in the word you are trying to guess");
        System.out.println();
    }

    /**
     * Helper method to prompt the user for a (valid) word length, and creates and returns a Game
     * @return {@link Game}
     */
    public static Game getWordLengthAndCreateGame(){
        // create a Scanner to prompt the user until they enter a valid length
        Scanner scan = new Scanner(System.in);
        boolean done = false;
        int wordLength = 0;

        // keep going until the user enters a valid integer
        while (!done){
            System.out.print("Enter a word length between " + Game.MIN_LENGTH + " and " + Game.MAX_LENGTH + ": ");

            // user must enter an integer
            if (scan.hasNextInt()){

                // if the Game creation raises an exception, the length was not valid
                wordLength = scan.nextInt();
                try{
                    Game game = new Game(wordLength);
                    return game;
                } catch (IllegalArgumentException e){
                    System.out.println("Illegal length of word specified. Try again.");
                    scan.nextLine();
                }
            }

            // the user did not even enter a valid integer
            else {
                System.out.println("Please enter an integer. Try again.");
                scan.nextLine();
            }

        }

        // this will never run
        return new Game(5);

    }

    /**
     * Method to find out whether the user wants to play again
     * @return {@link boolean}
     */
    public static boolean playAgain(){
        // ask the user if they want to play again
        Scanner scan = new Scanner(System.in);
        String response;

        // ask the user if they want to play again until they provide a valid response
        while (true){
            System.out.print("Play again ('yes' or 'no')? ");
            response = scan.next();
            if (response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("no"))
                return response.equalsIgnoreCase("yes");
            System.out.println("Enter a 'yes' or 'no'. We'll ask again.");
            scan.nextLine();
        }

    }

}