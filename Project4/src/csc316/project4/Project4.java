/**
 * 
 */
package csc316.project4;

import java.util.Scanner;

/**
 * This program 
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
    
    
    /**
     * Method that is used to make the program run. 
     * 
     * @param args command line arguments
     */
    public static void main (String[] args) {
        Scanner console = new Scanner(System.in);

        System.out.println("What is the name of the file?");
        filename = console.nextLine();
        filename = filename.trim();
        System.out.println("What is the name of the file to print to?");
        outfile = console.nextLine();
        outfile = outfile.trim();
    }
    
    
    
}
