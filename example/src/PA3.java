// Author: Hayden Cheers
// Student No: 3163842
// Course: SENG2200

// Class Name: PA3
// Date: 11/6/14
// Description: Main driver class for SENG2200 Assignment 3

import java.util.Scanner;
import storage.Storage;
import simulation.Simulation;

public class PA3 {

    java.lang.String q = "";
    producer.Producer r = null;
    producer.Producer s = null;
    producer.Producer t = null;

    public static void main(String[] args) {

java.lang.String x = "";
java.lang.String y = "";
java.lang.String z = "";

producer.Producer a = null;
producer.Producer b = null;
producer.Producer c = null;
producer.Producer d = null;

        System.out.print("\f"); // Clear the terminal (in BlueJ)
        
        int avai = 0;
        
        try {
            avai = System.in.available();
        } catch (Exception e) {
            
        }

        if (avai <= 0) {
            System.out.println("ERROR: System.in is empty, no file was passed or it is empty");
        } else {
            double theRange = 0, theMean = 0;
            int theStorageLimit = 0;
            
            try {
                Scanner keyboard = new Scanner(System.in);
                
                theMean = keyboard.nextDouble();
                theRange = keyboard.nextDouble();
                theStorageLimit = keyboard.nextInt();

            } catch (Exception ex) {
                System.out.println("ERROR: There are not enough values or the values passed are in the incorrect format");
                System.out.println("       Values should be in the form mean (double) rng (double) storageLimit (int)");
                
                return;
            }
            
            Simulation sim = new Simulation(10000000, theMean, theRange);
            Storage.setStorageLimit(theStorageLimit);
            sim.start();
        }
        
        
    }
}