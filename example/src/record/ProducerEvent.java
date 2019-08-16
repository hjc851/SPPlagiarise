package record;

// Author: Hayden Cheers
// Student No: 3163842
// Course: SENG2200

// Class Name: ProducerEvent
// Date: 31/5/14
// Description: A record of an event made by an Producer

import chrono.TimeKeeper;
import producer.Producer;

public class ProducerEvent extends EventRecord implements Comparable<ProducerEvent> {
    
    // Event Identifier
    public static final String WILL_FINISH_OBJECT = "WILL_FINISH_OBJECT";
    public static final String CAN_START = "CAN_START";
    
    // Producer object that made this record
    private Producer owner;
    
    public ProducerEvent(double time, String info, Producer owner) {
        this.time = time;
        this.info = info;
        this.owner = owner;
    }
    
    // Preconditions: None
    // Postcondition: Returns an int indicating the equality of the two objects
    public int compareTo(ProducerEvent that) {
        if (this.time < that.time) 
            return 1;
        else if (this.time == that.time)
            return 0;
        else
            return -1;
    }
    
    // Preconditions: None
    // Postcondition: The owner has processed its nextStorage item
    public void processEvent() {
        TimeKeeper.setTime(this.time);   // Set the getSharedTimeKeeper to the event chrono
        
        this.owner.processNextObject();
    }
    
    public String toString() {
        return "owner: " + owner + " info: " + info + " chrono: " + time;
    }
}
