package com.adamegyed.hangman;

import java.util.Scanner;
import javax.swing.JOptionPane;

public class Main {

    public static void main(String[] args) {

        //----------------Variables----------------
        String wtg = ""; // Word to Guess
        String wDisp; // String to display

        String charCountStr; // Character counting string, holds one of each
        int lgth; //Length of wtg;
        int numOfUniqs; // Number of unique characters in the string
        String charAtPosition; // A string that really just holds a character, used while counting uniques

        Scanner userInput = new Scanner(System.in); // New instance of a scanner to take user input

        boolean hasValidWord; // Boolean to test if the user has entered a valid word or phrase. Used in the beginning to loop
        // when user inputs 'bad' phrases for the program.

        boolean wantCont = true; // Want to continue
        String contStr; // Stores what the user enters when prompted to continue


        boolean doneGuessing; //true when player 2 has finished guessing
        int guessesLeft; // number of guesses left
        int ugc; // Unique guess correct
        int ugi; // Unique guesses incorrect
        boolean won; // has the user won
        String guess; // User's guess
        String prevGuesses; // String that holds all single-character guesses, separated by commas
        char guessChar; // char conversion of guess, if applicable




        //----------------Start of Program----------------

        System.out.println("Welcome to Hangman!!");



        while(wantCont) { // While the user wants the program to continue
            // ----Beginning of main game loop----

            // Giving many variables blank "initialized" states. This needs to run whenever the game loop repeats to prevent problems
            wDisp = "";
            charCountStr = ""; // Character counting string;
            numOfUniqs = 0;
            doneGuessing = false;
            guessesLeft = 26;
            ugc = 0;
            ugi = 0;
            won = false;
            prevGuesses = "";

            // Repeat asking for a word until program has a valid word less than 25 characters
            for(hasValidWord = false;!hasValidWord;) {
                System.out.println("Player 1, please enter the phrase to guess");

                wtg = JOptionPane.showInputDialog(null,"Please enter the phrase to guess:", "Hangman", JOptionPane.INFORMATION_MESSAGE);

                if(wtg==null) {
                    JOptionPane.showMessageDialog(null,"Thank you for playing","Hangman", JOptionPane.PLAIN_MESSAGE);
                    userInput.close();
                    System.exit(0);
                }

                wtg = wtg.trim(); //Removes any whitespaces in beginning and end of string

                if (wtg.length()>35) { // Sets a maximum limit on the size of the string
                    System.out.println("Please make your phrase shorter than 35 characters.");
                }
                else if (wtg.length()<=0) {
                    // do nothing if the user only enters spaces or leaves it blank
                    System.out.println("Please actually enter something.");
                }
                else if (wtg.contains(",")) {
                    System.out.println("Phrase to guess contains invalid character");
                    // Again, fail if the word to guess contains a comma
                    // commas are used to keep track of the previous guesses and would interfere with .contains()
                }
                else hasValidWord = true;

            }
            // At this point, wtg has the entire word to guess and spaces in front and back trimmed



            // Count number of unique characters
            lgth = wtg.length();
            for (int i = 0;i < lgth;i++) { // Run the number of times as the length of the string

               charAtPosition = wtg.substring(i,i+1).toLowerCase(); // A string that is used as a char. it needs to be a string for .contains() to be able to use it
                //System.out.println(charAtPosition);  // Was used in debugging


                if (!charAtPosition.equals(" ") && !charCountStr.contains(charAtPosition)) { // If the counting string does not contain the character the loop is at
                    // and is not a space
                    numOfUniqs++; //A new character is found, increment the counter of unique characters
                    charCountStr = charCountStr.concat(charAtPosition); //add the char to the counting string.
                    // This could have been done much more efficiently with a hashtable, but I guess we're not allowed to use those...

                }
            }


            //System.out.println("NumOfUniques: "+numOfUniqs+"\ncharCountStr: "+charCountStr);
            // Was used in debugging

            //System.out.println(wDisp+"\nLength: "+lgth);
            // More debugging


            // Set up display string
            for (int i = 0;i < lgth;i++) {

                if (wtg.charAt(i)!=' ') { //
                    wDisp = wDisp.concat(" _");
                }
                else wDisp = wDisp.concat("  ");
            }


            guessesLeft = 10 + lgth / 4;

            System.out.println("Player 2, please start guessing!\n"+wDisp+"\nGuesses Left: "+guessesLeft+"\n");

           while(!doneGuessing) {

                //Block for guess-checking
                guess = userInput.nextLine();

                guess = guess.trim();
                guess = guess.toLowerCase();
               //Guess is 'normalized' - all checking is done without whitespaces and in lower case

                if (isValidPhrase(guess)) {
                    if (guess.length()==1) { // If the user only guesses 1 character

                        if (charCountStr.contains(guess) && !prevGuesses.contains(guess)) {
                            // if if the word to guess contains the guessed character and the list of previous guesses does not contain it

                            ugc++; // Unique guesses correct is incremented
                            guessChar = guess.charAt(0); // made so the charAt method is not repeated unnecessarily

                            for (int i = 0;i < lgth; i++) {
                                if (Character.toLowerCase(wtg.charAt(i))==guessChar) {
                                    wDisp = wDisp.substring(0, 2 * i + 1)+wtg.charAt(i)+wDisp.substring(2*i+2);
                                    // Redefine the display with substrings as everything up until the matching char,
                                    // the corresponding character from the match, and the rest of the display.
                                    // Do this for each character in the word to guess string
                                    // 2 * i + 1 is used for the irregular spacing of the display String
                                }
                            }
                        }
                        else if (prevGuesses.contains(guess)) {
                            System.out.println("You've already guessed that!");
                        }
                        else {
                        // this will run if the char is not in the word to guess
                            ugi++; // Increment the unique guesses incorrect
                            System.out.println("That guess was not correct.");
                            guessesLeft--;
                        }
                        // if previous guesses does not contain this guess, add it
                        if (!prevGuesses.contains(guess)) {
                            prevGuesses = prevGuesses.concat(guess.toLowerCase()+",");
                        }

                    }
                    else { // Guess is longer than 1 character or length of 0
                        if (guess.equals("")) {
                            System.out.println("Please actually guess!");

                        }
                        else if (guess.equalsIgnoreCase(wtg)) { // If the guess is the entirety of the word to guess
                            for (int i = 0;i < lgth; i++) {
                                wDisp = wDisp.substring(0, 2 * i + 1)+wtg.charAt(i)+wDisp.substring(2*i+2);
                                // Make the display actually show the word to guess
                            }
                            doneGuessing = true;
                            won = true;
                        }
                        else  {
                            // tried and failed to guess the whole phrase
                            // nothing is added to the list of guessed characters and the display
                            System.out.println("That guess was not correct.");
                            guessesLeft--;
                        }
                    }


                }
                else System.out.println("Please enter a valid guess:");
               // if the guess is not valid (in this case, containing a comma)

               // Block for checking if player 2 has won or not
               if (guessesLeft <= 0) {
                   doneGuessing = true;
                   won = false;
               }
               if (ugc == numOfUniqs) {
                   doneGuessing = true;
                   won = true;
               }

                // Block for the user's end of turn statement
               if (!doneGuessing) { // If is not done guessing
                   System.out.println(wDisp); // Print the display string
                   System.out.println("Guesses left: "+guessesLeft); // Let the user know how many guesses they have left
                   System.out.println("Previous Guesses: "+prevGuesses);
               }
               else if (!won) { // Did not win
                   System.out.println("You lost.");
               }
               else { // Won
                   System.out.println(wDisp);
                   System.out.println("You won!!");
               }

            }





            // prompting the user if they want to repeat or not
            System.out.println("Would you like to play again?");
            contStr = userInput.nextLine();
            if (contStr.equalsIgnoreCase("y") || contStr.equalsIgnoreCase("yes")) {
                wantCont = true;
            }
            else wantCont = false;


        } // ----End of main game loop----



        System.out.println("Program is exiting...");
        userInput.close(); // Close the scanner



    } // End of method main

    // Checks to make sure the string does not contain invalid characters. Originally intended with more restrictions
    private static boolean isValidPhrase(String phrase) {
        return !phrase.contains(",");
    }



} // End of Class Main
