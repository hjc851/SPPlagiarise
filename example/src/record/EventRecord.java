package record;

// Author: Hayden Cheers
// Student No: 3163842
// Course: SENG2200

// Class Name: EventRecord
// Date: 31/5/14
// Description: Abstract class representing a record of an event. All subclasses are intended to be immutable.

public abstract class EventRecord {
    // Time of the event
    protected double time;
    // Information string regarding the event
    protected String info;
    
    // Preconditions: None
    // Postcondition: The chrono of the event is returned
    public double time() {
        return this.time;
    }
    
    // Preconditions: None
    // Postcondition: A string contains the information of the event is returned
    public String info() {
        return this.info;
    }
}