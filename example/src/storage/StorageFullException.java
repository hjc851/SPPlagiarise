package storage;

// Author: Hayden Cheers
// Student No: 3163842
// Course: SENG2200

// Class Name: StorageFullException
// Date: 31/5/14
// Description: Exception subclass intended to be used with Storage in the event the list is full upon insertion

public class StorageFullException extends Exception {
    public StorageFullException() {
        super();
    }
    
    public StorageFullException(String message) {
        super(message);
    }
}