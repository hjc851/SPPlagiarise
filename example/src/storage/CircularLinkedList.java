package storage;

// Author: Hayden Cheers
// Student No: 3163842
// Course: SENG2200

// Class Name: CicularLinkedList (with ListIterator)
// Date: 4/4/14
// Description: Linked List that uses java generics. Modified to be in a pakage.

import java.util.Iterator;
import java.lang.Iterable;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

public class CircularLinkedList<T> implements Iterable<T> {
    private final Node<T> sentinel; // Sentinel Node
    private int count;              // Count of objects in the list
    private int modNumber;          // Modification number
    
    public CircularLinkedList() {
        this.sentinel = new Node<T>(null, null, null);
        this.sentinel.setNext(this.sentinel);
        this.sentinel.setPrevious(this.sentinel);
        
        this.count = 0;
        this.modNumber = 0;
    }
    
    // **   ==  ==  ==  ==  ==  ==
    //      Insetion    ==  ==  ==
    // **   ==  ==  ==  ==  ==  ==
    
    // Preconditions: None
    // Postcondition: The productionObjects object is inserted first in the list
    public void insertFirst(T data) {
        this.insertAfterNode(data, this.sentinel);
    }
    
    // Preconditions: None
    // Postcondition: The productionObjects object is inserted last in the list
    public void insertLast(T data) {
        this.insertBeforeNode(data, this.sentinel);
    }
    
    // Preconditions: None (if target is null or is not in the list, an exception will be thrown)
    // Postcondition: The productionObjects object is inserted in the list after the target object
    public void insertAfterObject(T data, T target) throws ArrayStoreException {
       ListIterator it = new ListIterator();
        
       while (it.hasNext()) {
           if (it.next() == target) {
               this.insertAfterNode(data, it.current);
               
               return;
           }
       }
       
       throw new ArrayStoreException("Target " + target + " is not in the list");
    }
    
    // Preconditions: None (if target is null or is not in the list, an exception will be thrown)
    // Postcondition: The productionObjects object is inserted in the list before the target object
    public void insertBeforeObject(T data, T target) throws ArrayStoreException {
        ListIterator it = new ListIterator();
        
       while (it.hasNext()) {
           if (it.next() == target) {
               this.insertBeforeNode(data, it.current);
               
               return;
           }
       }
       
       throw new ArrayStoreException("Target " + target + " is not in the list");
    }
    
    // Preconditions: target is not null
    // Postcondition: A new Node is inserted after the target Node
    private void insertAfterNode(T data, Node<T> target) {
            
        Node<T> newNode = new Node<T>(data, target.getNext(), target);
        
        target.getNext().setPrevious(newNode);
        target.setNext(newNode);
        
        this.count++;
        this.modNumber++;
    }
    
    // Preconditions: target is not null
    // Postcondition: A new Node is inserted before the target Node
    private void insertBeforeNode(T data, Node<T> target) {
            
        Node<T> newNode = new Node<T>(data, target, target.getPrevious());
        
        target.getPrevious().setNext(newNode);
        target.setPrevious(newNode);
        
        this.count++;
        this.modNumber++;
    }
    
    // **   ==  ==  ==  ==  ==  ==
    //      Removal
    // **   ==  ==  ==  ==  ==  ==
    
    // Preconditions: None (null is returned if the list is empty)
    // Postcondition: The first object in the list is removed and is returned
    public T removeFirst() {
        Node<T> target = this.sentinel.getNext();
        
        this.sentinel.setNext(target.getNext());
        target.getNext().setPrevious(this.sentinel);
        
        if (this.count > 0)
            this.count--;
        this.modNumber++;
        
        return target.getData();
        
    }
    
    // Preconditions: None (null is returned if the list is empty)
    // Postcondition: The first object in the list is removed and returned
    public T removeLast() {
        Node<T> target = this.sentinel.getPrevious();
        
        this.sentinel.setPrevious(target.getPrevious());
        target.getPrevious().setNext(this.sentinel);
        
        if (this.count > 0)
            this.count--;
        this.modNumber++;
        
        return target.getData();
    }
    
    // Preconditions: None (an exception is thrown if the productionObjects object is null or is not in the list)
    // Postcondition: The specified productionObjects object in removed from the list
    public void removeObject(T data) {
        ListIterator iterator = new ListIterator();
        
        while (iterator.hasNext()) {
            if (iterator.next() == data) {
                iterator.remove();
                
                return;
            }
        }
        
        throw new ArrayStoreException("Object " + data + " was not found");
    }
    
    // **   ==  ==  ==  ==  ==  ==
    //      Query   ==  ==  ==  ==
    // **   ==  ==  ==  ==  ==  ==
    
    // Preconditions: None
    // Postcondition: Returns a reference to the first object in the list
    public T firstObject() {
        return this.sentinel.getNext().getData();
    }
    
    // Preconditions: None
    // Postcondition: Returns a reference to the last object in the list
    public T lastObject() {
        return this.sentinel.getPrevious().getData();
    }
    
    // Preconditions: None
    // Postcondition: Returns a boolean showing if the list is empty
    public boolean isEmpty() {
        return (this.sentinel.getNext() == this.sentinel);
    }
    
    // Preconditions: None
    // Postcondition: Returns the count of the list
    public int count() {
        return this.count;
    }
    
    // **   ==  ==  ==  ==  ==  ==
    //      Output  ==  ==  ==  ==
    // **   ==  ==  ==  ==  ==  ==
    
    // Preconditions: None
    // Postcondition: Returns a string containg the contents of the list
    public String toString() {
        StringBuffer buffer = new StringBuffer(this.hashCode() + " {\n");   // Create a StringBuffer (in lieu of a mutable string)
        
        ListIterator iterator = new ListIterator();
        int i = 0;
        
        while (iterator.hasNext()) {    // Go through the list, appending the current item and its index to the buffer
            buffer.append("[" + i + "]\t" + iterator.next() + "\n");
            i++;
        }
            
        buffer.append("}\n");
            
        return buffer.toString();
    }
    
    // **   ==  ==  ==  ==  ==  ==
    //      Iterator    ==  ==  ==
    // **   ==  ==  ==  ==  ==  ==
    
    // Preconditions: None
    // Postcondition: An iterator for the current list is returned
    public Iterator<T> iterator() {
        return new ListIterator();
    }
    
   private class ListIterator implements Iterator<T> {
       private Node<T> current;
       private int modNumber;   // Excpected mod count in the parent list
       private boolean nextHasBeenCalled;

       public ListIterator() {
           this.current = CircularLinkedList.this.sentinel;
           this.modNumber = CircularLinkedList.this.modNumber;
           this.nextHasBeenCalled = false;
        }
        
        // Preconditions: None
        // Postcondition: A boolean is returned showing if there is another item in the list
        public boolean hasNext() {
            return (this.current.getNext() != CircularLinkedList.this.sentinel);
        }
        
        // Preconditions: None
        // Postcondition: A reference to the nextStorage object in the list is returned. Throws exceptions if the list is out of sync or if there are no more objects in the list
        public T next() throws ConcurrentModificationException, NoSuchElementException {
            if (this.modNumber != CircularLinkedList.this.modNumber)
                throw new ConcurrentModificationException("Iterator " + this.hashCode() + " is out of sync");
                
            if(!this.hasNext())
                throw new NoSuchElementException("List " + CircularLinkedList.this.hashCode() + " has no more elements");
            
            this.nextHasBeenCalled = true;
            this.current = this.current.getNext();
            
            return this.current.getData();
        }
        
        // Preconditions: nextStorage() has been called, otherwise an exception is returned
        // Postcondition: The current item pointed to by the iterator is removed. An exception is thrown if the list is out of sync or if nextStorage has not been called
        public void remove() throws ConcurrentModificationException {
            if (this.modNumber != CircularLinkedList.this.modNumber)
                throw new ConcurrentModificationException("Iterator " + this.hashCode() + " is out of sync");
                
            if (!this.nextHasBeenCalled)
                throw new ConcurrentModificationException("Next has not been called on iterator " + this.hashCode());
            
            this.nextHasBeenCalled = false;
            
            Node<T> target = this.current;                  // Remove the Node from the list
            this.current = this.current.getPrevious();      //
                                                            //
            this.current.setNext(target.getNext());         //
            target.getNext().setPrevious(this.current);     //
                                                            //
            this.modNumber++;                               //
            CircularLinkedList.this.modNumber++;            //
            CircularLinkedList.this.count--;                //
        }
    }
}