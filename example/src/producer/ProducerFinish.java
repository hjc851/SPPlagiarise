 package producer;

// Author: Hayden Cheers
// Student No: 3163842
// Course: SENG2200

// Class Name: ProducerFinish
// Date: 10/6/14
// Description: Producer subclass intended to be an Producer objects at the end of a series of other Producer objects

import productionObjects.ProducibleObject;
import storage.*;

public class ProducerFinish extends Producer {
    
    private CircularLinkedList<ProducibleObject> bucket;
    
    public ProducerFinish(double mean, double range, Storage previous) {
        initialise(mean, range, null, previous);
        
        this.state = ProducerState.starving;
        
        this.bucket = new CircularLinkedList<ProducibleObject>();
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
    
    // Preconditions: This method is to be called at the end of processNextObject()
    // Postcondition: The current item is passed to the AssemblerFinisher's internal bucket, so items are kept in the event that 
    //                their statistics are relevent
    protected void moveCurrentObjectToStorage() {
        this.bucket.insertLast(this.currentObject);
        
        this.currentObject = null;
    }
}