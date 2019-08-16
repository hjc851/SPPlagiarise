 

// Author: Hayden Cheers
// Student No: 3163842
// Course: SENG2200

package storage;

// Class Name: Storage
// Date: 2/6/14
// Description: Storage node that has a global storage capacity that is initially set to 1, and cannot be 0. Stores AssemblableObjects

import java.util.HashMap;

import chrono.TimeKeeper;
import productionObjects.ProducibleObject;
import producer.*;
import simulation.Simulation;

public class Storage {
    
    private static int storageLimit = 1;    // Global storage limit
    private static int counter = 0;         // Internal creation counter
    
    // Preconditions: This should not be changed in the middle of a simulation
    // Postcondition: The gloabl storage limit is set to the parameter value (if larger than 0), or remains what is was if zero or less.
    public static void setStorageLimit(int storageLimit) {
        if (storageLimit > 0)
            Storage.storageLimit = storageLimit;
        else
            System.out.println("ERROR: The StorageLimit of all Storage objects must be larger than 0");
    }
    
    // Preconditions: None
    // Postcondition: Returns the global storage limit
    public static int storageLimit() {
        return Storage.storageLimit;
    }
    
    private CircularLinkedList<ProducibleObject> storageList;  // List of objects stored
    private HashMap<ProducibleObject, Double> dictionary;      // Dictionary of insertion times
    private Producer next[];                                   // References to the nextStorage Producer object(s)
    private Producer previous[];                               // References to the previousStorage Producer object(s)
    private int id;                                             // Unique identifier of this Producer object
    
    private double averageTime;     // Average chrono of objects in this list
    private double countedObjects;  // Number of objects that have passed through this list
    private double averageCount;
    private double lastEventTime;
    
    public Storage() {
        this.storageList = new CircularLinkedList<ProducibleObject>();
        this.dictionary = new HashMap<ProducibleObject, Double>();
        this.id = counter++;
        
        this.averageTime = 0;
        this.countedObjects = 0;
        this.averageCount = 0;
        this.lastEventTime = 0;
    }
    
    // Preconditions: The parameter(s) should not be null
    // Postcondition: The nextStorage reference(s) will be set to the parameters
    public void setNext(Producer... next) {
        this.next = next;
    }
    
    // Preconditions: The parameter(s) should not be null
    // Postcondition: The previousStorage reference(s) will be set to the parameters
    public void setPrevious(Producer... previous) {
        this.previous = previous;
    }
    
    // Preconditions: None (An exception is thrown if the list has reached capacity
    // Postcondition: 
    public void addItem(ProducibleObject object) throws StorageFullException {
        if (this.storageList.count() < Storage.storageLimit) {
            
            this.storageList.insertLast(object);
            
            // Update the average count
            this.averageCount += (this.count()-1)*(TimeKeeper.currentTime() - this.lastEventTime) / Simulation.currentSimulation().timeLimit();
            
            // Insert the relevent records
            this.dictionary.put(object, TimeKeeper.currentTime());
            
            // Update the last event chrono
            this.lastEventTime = TimeKeeper.currentTime();
            
            // Unstarve the first starving Producer (if there is one)
            for (Producer a : next) {
                if (a.currentState() == ProducerState.starving) {
                    a.unstarve();
                    return;
                }
            }
            
        } else {
            throw new StorageFullException();
            
        }
    }
    
    // Preconditions: None (an exception is thrown if the list is empty)
    // Postcondition: The nextStorage item in the list is reutrned
    public ProducibleObject nextItem() throws StorageEmptyException {
        if (this.count() > 0) {

            ProducibleObject o = this.storageList.removeFirst(); // The object to be returned
            
            // Update the average count
            this.averageCount += (this.count() + 1)*(TimeKeeper.currentTime() - this.lastEventTime) / Simulation.currentSimulation().timeLimit();
          
            // Update the average chrono each object spends in the storage list
            double insertTime = this.dictionary.remove(o);
            double removeTime = TimeKeeper.currentTime();
            this.averageTime = (averageTime * countedObjects + (removeTime - insertTime)) / ++countedObjects;
            
            // Unblock the first found Producer before this Storage node
            for (Producer a : previous) {
                if (a.currentState() == ProducerState.blocking) {
                    a.unblock();
                    break;
                }
            }
            
            // Update the last event chrono
            this.lastEventTime = TimeKeeper.currentTime();
            return o;
            
        } else {
            throw new StorageEmptyException();
        }
    }
    
    // Preconditions: None
    // Postcondition: The count of items in this list is returned
    public int count() {
        return this.storageList.count();
    }
    
    // Preconditions: None
    // Postcondition: A String containing this objects identifier is returned
    public String toString() {
        return "Storage" + id;
    }
    
    // Preconditions: None
    // Postcondition: A formatted string with theis objects statistics is returned
    public String statistics() {
        return String.format("| %-14s | %-12.11s | %-12.11s  |", this, this.averageTime, this.averageCount);
    }

}