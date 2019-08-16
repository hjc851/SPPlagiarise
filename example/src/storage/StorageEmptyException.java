package storage;

// Author: Hayden Cheers
// Student No: 3163842
// Course: SENG2200

// Class Name: StorageEmptyException
// Date: 31/5/14
// Description: Exception subclass to be used in the Storage class in the event that there are no objects in the list

public class StorageEmptyException extends Exception {
    public StorageEmptyException() {
        super();
    }
    
    public StorageEmptyException(String message) {
        super(message);
    }
}