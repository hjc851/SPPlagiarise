package storage;

// Author: Hayden Cheers
// Student No: 3163842
// Course: SENG2200

// Class Name: Node<T>
// Date: 2/4/14
// Description: A productionObjects object designed to hold a reference to an object in a linked list. Modified to be in a package

public class Node<T> {
    private T data;             // Object to store
    private Node<T> next;       // Reference to nextStorage Node in list
    private Node<T> previous;   // Reference to previousStorage Node in list
    
    public Node(T data, Node<T> next, Node<T> previous) {
        this.data = data;
        this.next = next;
        this.previous = previous;
    }
    
    // Preconditions: None
    // Postcondition: The productionObjects reference is set to the parameter
    public void setData(T data) {
        this.data = data;
    }
    
    // Preconditions: None
    // Postcondition: The nextStorage reference is set to the parameter
    public void setNext(Node<T> next) {
        this.next = next;
    }
    
    // Preconditions: None
    // Postcondition: The previousStorage reference is set to the parameter
    public void setPrevious(Node<T> previous) {
        this.previous = previous;
    }
    
    // Preconditions: None
    // Postcondition: A reference to the productionObjects object is returned
    public T getData() {
        return this.data;
    }
    
    // Preconditions: None
    // Postcondition: A reference to the nextStorage Node is returned
    public Node<T> getNext() {
        return this.next;
    }
    
    // Preconditions: None
    // Postcondition: A reference to the previousStorage Node is returned
    public Node<T> getPrevious() {
        return this.previous;
    }
}