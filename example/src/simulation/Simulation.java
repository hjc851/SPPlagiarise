package simulation;

// Author: Hayden Cheers
// Student No: 3163842
// Course: SENG2200

// Class Name: Simulation
// Date: 11/6/14
// Description: Class that runs the simulation and contains the main event loop for SENG2200 Assignment 3

import chrono.TimeKeeper;
import productionObjects.ProducibleObject;
import producer.*;
import storage.*;
import record.*;

public class Simulation {
    
    // Reference to the currently executing simulation
    private static Simulation currentSimulation;
    
    // Returns the currently executing simulation
    public static Simulation currentSimulation() {
        return currentSimulation;
    }
    
    private double timeLimit;       // Timelimit of the simulation
    private double standardMean;    // Standard mean of producer production times
    private double standardRange;   // Standard range of producer production times
    
    private Producer producer[];  // Array of Producer derived objects
    private Storage storage[];      // Array of Storage objects
    private EventQueue eventQueue;  // Event queue of the list
    
    public Simulation(double timeLimit, double standardMean, double standardRange) {

        this.timeLimit = timeLimit;
        this.standardMean = standardMean;
        this.standardRange = standardRange;
        
        this.eventQueue = new EventQueue();
        
        this.producer = new Producer[8];
        this.storage = new Storage[5];
        
        // Create the Storage objects. Each index corresponds to a Storage node in the assignment spec
        storage[0] = new Storage(); //Q01
        storage[1] = new Storage(); //Q12
        storage[2] = new Storage(); //Q23
        storage[3] = new Storage(); //Q34
        storage[4] = new Storage(); //Q45
        
        // Create the Producer objects. Each index corresponds to an Producer in the assignment spec
        producer[0] = new ProducerStart(this.standardMean, this.standardRange, storage[0]);
        producer[1] = new ProducerStation(this.standardMean, this.standardRange, storage[1], storage[0]);
        producer[2] = new ProducerStation(this.standardMean * 2.0, this.standardRange * 2.0, storage[2], storage[1]);
        producer[3] = new ProducerStation(this.standardMean * 2.0, this.standardRange * 2.0, storage[2], storage[1]);
        producer[4] = new ProducerStation(this.standardMean, this.standardRange, storage[3], storage[2]);
        producer[5] = new ProducerStation(this.standardMean * 2.0, this.standardRange * 2.0, storage[4], storage[3]);
        producer[6] = new ProducerStation(this.standardMean * 2.0, this.standardRange * 2.0, storage[4], storage[3]);
        producer[7] = new ProducerFinish(this.standardMean, this.standardRange, storage[4]);
        
        // Set the nextStorage and previousStorage references for each storage node
        storage[0].setNext(producer[1]);
        storage[0].setPrevious(producer[0]);
        storage[1].setNext(producer[2], producer[3]);
        storage[1].setPrevious(producer[1]);
        storage[2].setNext(producer[4]);
        storage[2].setPrevious(producer[2], producer[3]);
        storage[3].setNext(producer[5], producer[6]);
        storage[3].setPrevious(producer[4]);
        storage[4].setNext(producer[7]);
        storage[4].setPrevious(producer[5], producer[6]);
        
        // Insert the starter event
        this.eventQueue.insertEvent(new ProducerEvent(TimeKeeper.currentTime(), ProducerEvent.CAN_START, producer[0]));
    }
    
    public double timeLimit() {
        return this.timeLimit;
    }
    
    // Preconditions: None
    // Postcondition: Main event loop has finished, and the statistics of each Producer and Storage object has been printed
    public void start() {
        
        Simulation.currentSimulation = this;
                
        while (TimeKeeper.currentTime() < this.timeLimit && this.eventQueue.count() > 0) {
            this.eventQueue.nextEvent().processEvent();
        }
        
        this.printStatistics();
    }
    
    // Preconditions: Is only called within start(), or start has been previously called (otherwise calling will be pointless
    // Postcondition: The statistics of each Producer and Storage object will have been printed
    private void printStatistics() {
        System.out.println(String.format("Time Limit: %.2f Last Producer Finish Time: %.2f\nStandard Mean: %.2f Standard Range: %.2f", this.timeLimit, TimeKeeper.currentTime(), this.standardMean, this.standardRange));
        System.out.println(String.format("Storage Capacity: %d", Storage.storageLimit()));
        System.out.println("ProducibleObject count: " + ProducibleObject.currentCount());
        System.out.println();
        System.out.println("Statistics\n");
        
        System.out.println("Assemblers");
        System.out.println(" ----------------------------------------------------- ");
        System.out.println(String.format("| %-14s | %-12s | %-8s | %-8s |", "Producer ID", "Production", "Starving", "Blocked"));
        System.out.println("|-----------------------------------------------------|");
        
        for (Producer a : producer) {
            System.out.println(a.statistics());
        }
        
        System.out.println(" ----------------------------------------------------- ");
        System.out.println();
        
        System.out.println("Storage");
        System.out.println(" ----------------------------------------------- ");
        System.out.println(String.format("| %-14s | %-12s | %-12s |", "Storage ID", "Average Time", "Average Count"));
        System.out.println(" ----------------------------------------------- ");
        
        for (Storage s : storage) {
            System.out.println(s.statistics());
        }
        
        System.out.println(" ----------------------------------------------- ");
        
        
    }
}