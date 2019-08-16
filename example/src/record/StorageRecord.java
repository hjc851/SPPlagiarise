package record;

// Author: Hayden Cheers
// Student No: 3163842
// Course: SENG2200

// Class Name: StorageRecord
// Date: 31/5/14
// Description: A record of an action made by a Storage node

import productionObjects.ProducibleObject;

public class StorageRecord extends EventRecord {
    
    // Event Identifiers
    public static final String DID_REMOVE = "DID_REMOVE";
    public static final String DID_ADD = "DID_ADD";
    
    // Capacity of the Storage object at the chrono of record creation
    private int capacity;
    // Object that was stored at the record creation chrono
    private ProducibleObject subject;
    
    public StorageRecord(double time, String info, int capacity, ProducibleObject subject) {
        this.time = time;
        this.info = info;
        this.capacity = capacity;
        this.subject = subject;
    }
    
    // Preconditions: None
    // Postcondition: The capacity of the Storage node at record chrono is returned
    public int capacity() {
        return this.capacity;
    }
    
    // Preconditions: None
    // Postcondition: A reference to the targeted object is returned
    public ProducibleObject target() {
        return this.subject;
    }
}
