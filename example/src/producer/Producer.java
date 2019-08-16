 package producer;

// Author: Hayden Cheers
// Student No: 3163842
// Course: SENG2200

// Class Name: Producer
// Date: 10/6/14
// Description: Abstract class representing an Producer on a production line. Requires getNext() and passCurrentToStorage() to be overwritten.

import java.util.Random;

import chrono.TimeKeeper;
import productionObjects.ProducibleObject;
import storage.*;
import record.*;

public abstract class Producer {
    
    // Random number randomProducer
    protected static final Random randomProducer = new Random();
    // Object creation producerCounter
    private static int producerCounter = 0;
    
    protected ProducerState state;  // Current state of the Producer object
    
    private double productionMean;    // Average chrono to process an object
    private double productionRange;   // Range limit of item processing
    
    // Time taken to produce an item
    protected double actualProductionTime;    // Time the Producer has spent producing items
    protected double actualBlockedTime;       // Time the Producer has spent blocked
    protected double actualStarvedTime;       // Time the Producer has spent starving
    
    protected ProducibleObject currentObject;  // Current object being processed
    protected Storage nextStorage, previousStorage;           // Next and Previous Storage objects
    
    private int stationId = producerCounter++; // unique stationId of the Producer
    
    // Preconditions: Method should only be called once
    // Postcondition: The Producer is initialised with the default values, as well as the passed parameters
    protected void initialise(double mean, double range, Storage next, Storage previous) {
        this.productionMean = mean;
        this.productionRange = range;
        this.nextStorage = next;
        this.previousStorage = previous;

        this.actualProductionTime = 0;
        this.actualStarvedTime = 0;
        this.actualBlockedTime = 0;
    }
    
    // Preconditions: None
    // Postcondition: Upon success an item is retreived from the previousStorage storage node, processed and passed to the nextStorage storage node.
    //                If there was no items in the previousStorage storage node, the Producer enters a starving state
    //                If there was no more capacity in the nextStorage storage node, the Producer enters a blocking state
    public void processNextObject() {
        // pass to storage
        if (this.currentObject != null) {
            try {
                this.moveCurrentObjectToStorage();
            } catch (StorageFullException e) {
                // Enters blocked state
                this.state = ProducerState.blocking;
                this.actualBlockedTime -= TimeKeeper.currentTime();
                return;
            }
        }
        
        
        // get nextStorage item
        try {
            this.receiveNextObject();
        } catch (StorageEmptyException e) {
            // Enters starved state
            this.state = ProducerState.starving;
            this.actualStarvedTime -= TimeKeeper.currentTime();
            return;
        }
        
        // calculate how long it will take
        double p = productionMean + productionRange * (randomProducer.nextDouble() - 0.5);
        
        this.actualProductionTime += p;
        
        EventQueue.currentQueue().insertEvent(new ProducerEvent(TimeKeeper.currentTime() + p, ProducerEvent.WILL_FINISH_OBJECT, this));
    }
    
    // Preconditions: Method should only be callen from processNextObject
    // Postcondition: The nextStorage item from the previousStorage Storage object is retrieved, otherwise an exception is thrown
    // Notes:         This method needs to handle what happend to an Producer if there are no more items in the previousStorage Storage
    abstract protected void receiveNextObject() throws StorageEmptyException;
    
    // Preconditions: Method should be called from within processNextObject if the Producer is working, or unblock() if it is blocking
    // Postcondition: The current item processed by the Producer is passed to the nextStorage Storage node, otherwise the Producer enters a blocking state
    // Notes:         This method needs to handle what happend to an Producer if there is no more capacity in the nextStorage Storage
    abstract protected void moveCurrentObjectToStorage() throws StorageFullException;
    
    // Preconditions: The Producer is in a blocking state
    // Postcondition: The assembler's state is restored to blocking, the blocked chrono is updated and the Assemblers current item is passed to storage
    public void unblock() {
        try {
            this.moveCurrentObjectToStorage();
            this.actualBlockedTime += TimeKeeper.currentTime();
            this.state = ProducerState.working;
            EventQueue.currentQueue().insertEvent(new ProducerEvent(TimeKeeper.currentTime(), ProducerEvent.CAN_START, this));
                
        } catch (StorageFullException e) {
            // Enters blocked state. This should never occur, unless the Storage object unblocks incorrectly
            this.state = ProducerState.blocking;
            return;
        }
    }
    
    // Preconditions: The Producer is in a starving state
    // Postcondition: The assembeler's state is retored to working, the starve chrono is updated and a new event is added to the event queue to allow it to resume processing
    public void unstarve() {
        this.state = ProducerState.working;
        this.actualStarvedTime += TimeKeeper.currentTime();
        
        EventQueue.currentQueue().insertEvent(new ProducerEvent(TimeKeeper.currentTime(), ProducerEvent.CAN_START, this));
    }
    
    // Preconditions: None
    // Postcondition: Returns the current state of the Producer
    public ProducerState currentState() {
        return this.state;
    }
    
    // Preconditions: None
    // Postcondition: Returns the assemble identifier in the form of a string
    public String toString() {
        return "Producer" + stationId;
    }
    
    // Preconditions: The Producer should have ran atleast once (otherwise will be pointless)
    // Postcondition: The statistics of the Producer are returned in a formatted String
    public String statistics() {
        if (state == ProducerState.starving) {      // If an Producer has stopped in either a Blocking or Starving state
            this.actualStarvedTime += TimeKeeper.currentTime();    // the chrono will be negative. To fix this increase the recorded chrono by the clocks current chrono
            this.state = ProducerState.sleeping;
        } else if (this.state == ProducerState.blocking) {
            this.actualBlockedTime += TimeKeeper.currentTime();
            this.state = ProducerState.sleeping;
        } else {
            this.state = ProducerState.sleeping;
        }
        
        return String.format("| %-14s | %-12.10s | %-8.8s | %-8.8s |", this,  this.actualProductionTime /TimeKeeper.currentTime() * 100.0, this.actualStarvedTime /TimeKeeper.currentTime() * 100.0, this.actualBlockedTime /TimeKeeper.currentTime() * 100.0);
    }
}