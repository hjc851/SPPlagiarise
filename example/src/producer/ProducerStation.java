 package producer;

// Author: Hayden Cheers
// Student No: 3163842
// Course: SENG2200

// Class Name: Producer Station
// Date: 10/6/14
// Description: Producer subclass intended to an Producer station in a series between an ProducerStart and ProducerFinish

import storage.*;

public class ProducerStation extends Producer {
    
    public ProducerStation(double mean, double range, Storage next, Storage previous) {
        initialise(mean, range, next, previous);
        
        this.state = ProducerState.starving;
    }
    
    // Preconditions: Should be called at the start of processNextObject()
    // Postcondition: Retreives the nextStorage item from the previousStorage Storage node and returns it, otherwise throws an
    //                exception and places the Producer in a starving state
    protected void receiveNextObject() throws StorageEmptyException {
        try {
            this.currentObject = this.previousStorage.nextItem();
            
        } catch (StorageEmptyException e) {
            throw e;
        }
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