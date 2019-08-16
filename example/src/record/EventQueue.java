package record;

// Author: Hayden Cheers
// Student No: 3163842
// Course: SENG2200

// Class Name: EventQueue
// Date: 31/5/14
// Description: A Queue of recoreded AssemblerEvents. Allows addition, retreival and peeking.
//              This class should never contain more events than the number of Producer items

import storage.SortedList;

public class EventQueue {
    // A reference to the last created EventQueue object
    private static EventQueue currentQueue;
    
    // Preconditions: None (null is returned if no objects have been created
    // Postcondition: A reference to the last created EventQueue is created
    public static EventQueue currentQueue() {
        return currentQueue;
    }
    
    // SortedList of the recorded events
    private SortedList<ProducerEvent> eventList;
    
    public EventQueue() {
        this.eventList = new SortedList<ProducerEvent>();
        currentQueue = this;
    }
    
    // Preconditions: None
    // Postcondition: The newEvent parameter is added to the event list in sorted order
    public void insertEvent(ProducerEvent newEvent) {
        this.eventList.insert(newEvent);
    }
    
    // Preconditions: None
    // Postcondition: The nextStorage event in the list is removed and returned
    public ProducerEvent nextEvent() {
        return this.eventList.removeFirst();
    }
    
    // Preconditions: None
    // Postcondition: A reference to the nextStorage event in the list is returned
    public ProducerEvent peekNext() {
        return this.eventList.firstObject();
    }
    
    // Preconditions: None
    // Postcondition: The count of objects in the queue is returned
    public int count() {
        return this.eventList.count();
    }
    
    // Preconditions: None
    // Postcondition: A String containing the String representation of the internal list is returned
    public String toString() {
        return this.eventList.toString();
    }
}
