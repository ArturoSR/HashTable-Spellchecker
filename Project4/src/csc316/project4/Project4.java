/**
 * 
 */
package csc316.project4;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * This program uses a hashtable to implement a spell-checker. 
 * 
 * @author Arturo Salinas
 */
public class Project4 {

    /** Store the filename of the dictionary to use*/
    private static String dictionaryFile = "";
    /** Name of the file to read from*/
    private static String filename = "";
    /** Name of the file to write to*/
    private static String outfile = "";
    /**Hash table that stores words of dictionary*/
    private static ArrayList<LinkedList<String>> hashTable;
    /**Keeps count of the number of words in the dictionary*/
    private static int dictionaryWords;
    /** Found by multiplying number of dictionary characters by 0.2, adding that to number of dictionary characters and then finding the 
     * the closest prime number. 
     */
    private static final int sizeCapM = 30181; 
    
    private static int wordsInFile;
    
    private static int numLookUps;
    
    
    /**
     * Method that is used to make the program run. 
     * 
     * @param args command line arguments
     */
    public static void main (String[] args) {
        Scanner console = new Scanner(System.in);

        System.out.println("What is the name of the file containing the dictionary?");
        dictionaryFile = console.nextLine();
        dictionaryFile = dictionaryFile.trim();
        System.out.println("What is the name of the file to spellcheck?");
        filename = console.nextLine();
        filename = filename.trim();
//        System.out.println("What is the name of the file to print to?");
//        outfile = console.nextLine();
//        outfile = outfile.trim();
        
        buildHashTable();
        System.out.println("Yuki is " + hashTable.get(useHashFunction("Yuki")).toString());
        System.out.println("yuh is " + hashTable.get(useHashFunction("yuh")).toString());
        System.out.println("Index of Braggart " + hashTable.get(useHashFunction("braggart")).indexOf("braggart"));
        System.out.println("roadblock is " + hashTable.get(useHashFunction("roadblock")).toString());
        System.out.println("don't is " + hashTable.get(useHashFunction("don't")).toString());
        System.out.println("Contents of space 25 is" + hashTable.get(25).toString());
        
        spellChecker();
        
        console.close();
    }
    
    /**
     * Method that builds the hashTable for the dictionary. 
     */
    public static void buildHashTable() {
        try {
            Scanner dictionaryScan = new Scanner(new FileInputStream(dictionaryFile));
            dictionaryWords = 0;
            //Initializing the hashTable
            hashTable = new ArrayList<LinkedList<String>>(sizeCapM);
            for (int i = 0; i < sizeCapM; i++){
                LinkedList<String> listInit = new LinkedList<String>();
                hashTable.add(listInit);
            }
            

            while(dictionaryScan.hasNext()) {
                String currentWord = dictionaryScan.next();
                int hash = useHashFunction(currentWord);
                hashTable.get(hash).add(currentWord);
                dictionaryWords++;
            }
            System.out.println("Number of Words in Dictionary: " + dictionaryWords);
        } catch (FileNotFoundException e) {
            System.out.println("The dictionary file was not found.");
        }
        
    }
    
    /**
     * Method that creates a compressed hash-code to use in deciding where to store a word.
     * Applies the Polynomial Hash Code discussed in class and the divide to compress method. 
     * 
     * @param word is the word to make the hashCode of
     * @return the integer hash code of the word passed in. 
     */
    public static int useHashFunction(String word) {
        //Since each ascii character is typically 7 bits long
        int radix = 128;
        int wordVal = 0;
        for(int i = 0, j = (word.length() - 1); i < word.length(); i++, j--) {
            int charVal = (int) word.charAt(i);
//            System.out.println("current value of character is " + charVal);
//            System.out.println("current value of exponent is " + j);
            wordVal += (int) (charVal * Math.pow(radix, j));
        }
        
//        System.out.println("Uncompressed word Value: " + wordVal);
//        System.out.println("Expected: " + (1073475 % 30181));
        
        //Absolute value to avoid negative compressed hash values
        return Math.abs((wordVal % sizeCapM));
    }
    
    /**
     * Method that checks to see if words are spelled correctly. 
     */
    public static void spellChecker() {
        try {
            Scanner checkFile = new Scanner(new FileInputStream(filename));
            //PrintStream output = new PrintStream(new File(outfile));
            wordsInFile = 0;
            numLookUps = 0;
            int numProbes = 0;
            while(checkFile.hasNextLine()) {
                String checkLine = checkFile.nextLine();
                checkLine = removePunctuation(checkLine);
                Scanner lineScanner = new Scanner(checkLine);
                while(lineScanner.hasNext()) {
                    String wordStore = lineScanner.next();
                    String word = wordStore;
                    int currentProbes = lookUpWord(word);
                    
                    
                    String wordCopy = word;
                    //First Rule
                    if(currentProbes <= 0 && Character.isUpperCase(word.charAt(0))) {
                        word = Character.toLowerCase(word.charAt(0)) + word.substring(1);
                        currentProbes = lookUpWord(word);
                    }
                    
                    //Second Rule
                    if(currentProbes <= 0 && word.length() > 2 && word.substring(word.length() - 2).equals("'s")) {
                        word = word.substring(0, word.length() - 2);
                        currentProbes = lookUpWord(word);
                    }
                    
                    //Third Rule 
                    wordCopy = word;
                    if(currentProbes <= 0 && word.length() > 1 && word.substring(word.length() - 1).equals("s")) {
                        word = word.substring(0, (word.length() - 1));
                        currentProbes = lookUpWord(word);
                        if(currentProbes <= 0 && wordCopy.substring(wordCopy.length() - 2).equals("es")) {
                            word = wordCopy.substring(0, wordCopy.length() - 2);
                            currentProbes = lookUpWord(word);
                        }
                    }
                    
                    //Fourth Rule
                    wordCopy = word;
                    if(currentProbes <= 0 && word.length() > 2 && word.substring(word.length() - 2).equals("ed")) {
                        word = word.substring(0, (word.length() - 2));
                        currentProbes = lookUpWord(word);
                        if(currentProbes <= 0 && wordCopy.substring(wordCopy.length() - 1).equals("d")) {
                            word = wordCopy.substring(0, wordCopy.length() - 1);
                            currentProbes = lookUpWord(word);
                        }
                    }
                    
                    //Fifth Rule
                    wordCopy = word;
                    if(currentProbes <= 0 && word.length() > 2 && word.substring(word.length() - 2).equals("er")) {
                        word = word.substring(0, (word.length() - 2));
                        currentProbes = lookUpWord(word);
                        if(currentProbes <= 0 && wordCopy.substring(wordCopy.length() - 1).equals("r")) {
                            word = wordCopy.substring(0, wordCopy.length() - 1);
                            currentProbes = lookUpWord(word);
                        }
                    }
                    
                    //Sixth Rule
                    wordCopy = word;
                    if(currentProbes <= 0 && word.length() > 3 && word.substring(word.length() - 3).equals("ing")) {
                        word = word.substring(0, (word.length() - 3));
                        currentProbes = lookUpWord(word);
                        if(currentProbes <= 0 && wordCopy.substring(wordCopy.length() - 3).equals("ing")) {
                            word = wordCopy.substring(0, wordCopy.length() - 3) + "e";
                            currentProbes = lookUpWord(word);
                        }
                    }
                    
                    //Seventh Rule
                    if(currentProbes <= 0 && word.length() > 2 && word.substring(word.length() - 2).equals("ly")) {
                        word = word.substring(0, (word.length() - 2));
                        currentProbes = lookUpWord(word);
                    }
                    
                    if(currentProbes <= 0 ) {
                        System.out.println("Misspelled Word: " + wordStore);
                    }
                }
                //it should count the number of probes in the hash
                //table. For the purposes of counting, a probe occurs whenever a text word is compared to a word
                //in the table. A probe does not occur when a text word is compared to an empty table entry.
                
                //This should hold the conditions of rechecking as well
                //lookups++ in every if else of rechecking word
                
                
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error reading the input file to spellcheck.");
        }
        
    }
    
    /**
     * Method that looks in the hashTable to see if the word is present.
     * 
     * @param word the word to lookup
     * @return the number of probes that it took to look up a word. 0 if the word was compared to an empty row.
     */
    public static int lookUpWord(String word) {
        //If word is not in dictionary return 0
        LinkedList<String> listToProbe = hashTable.get(useHashFunction(word));
        int probes = 0;
        if(listToProbe.isEmpty()) {
            return 0;
        } else if(listToProbe.indexOf(word) == -1){
                numLookUps++;
                probes = (listToProbe.size() + 1);
                //Looked through entire list and compared against all elements but didn't find
                return (probes * -1);
            
        } else {
            //Since at least one comparison has to be made.
            numLookUps++;
            probes = (listToProbe.indexOf(word) + 1);
            return probes;
        
        }
        
    }
    
    /**
     * Simple method that removes all punctuation from a string line.
     * @param line the string to remove the punctuation from.
     * @return the initial string without any punctuation except "'"
     */
    public static String removePunctuation(String line) {
        line = line.replaceAll("[\\p{Punct}&&[^']]+", " ");
        return line;
    }
    
    
}



