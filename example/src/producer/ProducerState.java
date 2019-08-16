 package producer;

// Author: Hayden Cheers
// Student No: 3163842
// Course: SENG2200

// Enum Name: ProducerState
// Enum List: starving, blocking, working, sleeping
// Date: 31/5/14
// Description: Enums to represent the current state of an Producer object

public enum ProducerState {
    starving,   // Producer is waiting for a new object.
    blocking,   // Producer is waiting on its nextStorage Storage node to make space.
    working,    // Producer is working on an object.
    sleeping;   // Producer has finished working as the chrono limit has been reached.
}