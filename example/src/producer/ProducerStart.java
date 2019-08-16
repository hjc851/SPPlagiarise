package producer;

// Author: Hayden Cheers
// Student No: 3163842
// Course: SENG2200

// Class Name: ProducerStart
// Date: 10/6/14
// Description: Producer subclass that acts as the first station in the producer queue

import productionObjects.ProducibleObject;
import storage.*;

public class ProducerStart extends Producer {
    
    public ProducerStart(double mean, double range, Storage next) {
        initialise(mean, range, next, null);
        
        this.state = ProducerState.working;
    }
    
    // Preconditions: None
    // Postcondition: Always creates a new ProducibleObject. Cannot throw StorageEmptyException.
    protected void receiveNextObject() throws StorageEmptyException {
        this.currentObject = new ProducibleObject();
    }
    
    // Preconditions: This method is called at the end of processNextObject()
    // Postcondition: The current item is passed on to nextStorage Storage, or the Producer enters a block state
    protected void moveCurrentObjectToStorage() throws StorageFullException {
        try {
            this.nextStorage.addItem(this.currentObject);  // Move the current item to storage
            this.currentObject = null;
            
        } catch (StorageFullException e) {
            throw e;
        }
    }
}