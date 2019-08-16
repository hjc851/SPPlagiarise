package productionObjects;

// Author: Hayden Cheers
// Student No: 3163842
// Course: SENG2200

// Class Name: ProducibleObject
// Date: 31/5/14
// Description: An Object representing an item to be assembled on a production line. Capable of storing
//              records of events that the object takes part in.

public class ProducibleObject {
    
    // Counter for objects created
    private static int createdCount;
    
    // Preconditions: None
    // Postcondition: The number of AssemblableObjects created is returned
    public static int currentCount() {
        return createdCount;
    }
    
    // Unique identifier of this object
    private int id;
    
    public ProducibleObject() {
        this.id = createdCount++;
    }
    
    // Preconditions: None
    // Postcondition: A string returning this objects identifier is returned
    public String toString() {
        return "ProducibleObject:"+this.id;
    }
}