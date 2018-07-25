/**
 * 
 */
package csc316.project4;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    
    private static int dictionaryWords;
    
    /**Found by multiplying number of dictionary characters by 0.2, adding that to number of dictionary characters and then finding the 
     * the closest prime number. 
     */
    private static final int sizeCapM = 30181; 
    
    
    /**
     * Method that is used to make the program run. 
     * 
     * @param args command line arguments
     */
    public static void main (String[] args) {
        Scanner console = new Scanner(System.in);

        System.out.println("What is the name of the file to spellcheck?");
        filename = console.nextLine();
        filename = filename.trim();
        System.out.println("What is the name of the file containing the dictionary?");
        dictionaryFile = console.nextLine();
        dictionaryFile = dictionaryFile.trim();
//        System.out.println("What is the name of the file to print to?");
//        outfile = console.nextLine();
//        outfile = outfile.trim();
        
        buildHashTable();
//        System.out.println("HashCode Check: " + useHashFunction("ABC"));
        
        
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
            
            int totalCharsLength = 0;
            while(dictionaryScan.hasNext()) {
                String currentWord = dictionaryScan.next();
                int hash = useHashFunction(currentWord);
                totalCharsLength += currentWord.length();
                
                //Use get to retrieve the linkedList at a position and store to temp
                //Append new word to temp list and then use set to reinsert the temp list to the spot
                dictionaryWords++;
            }
            System.out.println("Number of Words in Dictionary: " + dictionaryWords);
        } catch (FileNotFoundException e) {
            System.out.println("The dictionary file was not found.");
        }
        
    }
    
    
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
        return (wordVal % sizeCapM);
    }
    
    
}



