package storage;

// Author: Hayden Cheers
// Student No: 3163842
// Course: SENG2200

// Class Name: SortedList (Includes SortedIterator)
// Date: 12/4/14
// Description: A Sorted LinkedList that uses java generics. Can only hold Comparable<T> objects.
//              Implements Iteratble<T>. Modified to be in a package.

import java.util.Iterator;
import java.lang.Iterable;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

public class SortedList<T extends Comparable<T>> implements Iterable<T> {
    private final Node<T> sentinel; // Sentinel Node
    private int count;              // Count of objects in the list
    private int modCount;           // Modification count
    
    public SortedList() {
        this.sentinel = new Node<T>(null, null, null);
        this.sentinel.setNext(sentinel);
        this.sentinel.setPrevious(sentinel);
        
        this.count = 0;
        this.modCount = 0;
    }
    
    // **   ==  ==  ==  ==  ==  ==
    //      Insertion   ==  ==  ==
    // **   ==  ==  ==  ==  ==  ==
    
    //Insertion -> theres no point in making a sorted list that allows objects to be inserted unordered
    // Preconditions: productionObjects is not null
    // Postcondition: The productionObjects object is inserted into the list in inserted order
    public void insert(T data) {
        SortedIterator iterator = new SortedIterator();
            
        while (iterator.hasNext()) {
            if (data.compareTo(iterator.next()) >= 0) { // Break out of the loop when the current object
                break;                                  // is larger or qual to the nextStorage object
            }
            
            if (iterator.current.getNext() == this.sentinel) {  // If the nextStorage object is the sentinel node, insert it last
                Node<T> newNode = new Node<T>(data, this.sentinel, this.sentinel.getPrevious());
                    
                this.sentinel.getPrevious().setNext(newNode);
                this.sentinel.setPrevious(newNode);
                    
                this.count++;
                this.modCount++;
                    
                return;
            }
        }
            
        // Insert the object before the iterators current Node
        Node<T> newNode = new Node<T>(data, iterator.current, iterator.current.getPrevious());
            
        iterator.current.getPrevious().setNext(newNode);
        iterator.current.setPrevious(newNode);
            
        this.count++;
        this.modCount++;
    }
    
    // **   ==  ==  ==  ==  ==  ==
    //      Retreival   ==  ==  ==
    // **   ==  ==  ==  ==  ==  ==
    
    // Preconditions: None
    // Postcondition: The first object in the list is removed from the list and returned.
    //                null is returned if there are no objects in the list
    public T removeFirst() {
        Node<T> temp = this.sentinel.getNext();
        
        this.sentinel.setNext(temp.getNext());
        temp.getNext().setPrevious(this.sentinel);
        
        if (this.count > 0)
            this.count--;
        this.modCount++;
            
        return temp.getData();
    }
    
    // Preconditions: None
    // Postcondition: The last object in the list is removed and returned.
    //                null is returned if there are no objects in the list
    public T removeLast() {
        Node<T> target = this.sentinel.getPrevious(); 
        
        this.sentinel.setPrevious(target.getPrevious());
        target.getPrevious().setNext(this.sentinel);
        
        if (this.count > 0)                         
            this.count--;
        this.modCount++;
        
        return target.getData();                    
    }
    
    // Preconditions: productionObjects is not null (will throw exception if null)
    // Postcondition: The object is removed from the list
    public void removeObject(T data) throws ArrayStoreException {
        SortedIterator iterator = new SortedIterator();
        
        while (iterator.hasNext()) {
            if (iterator.next() == data) {
                iterator.remove();
                return;
            }
        }
        
        throw new ArrayStoreException("Object " + data + " was not found");
    }
    
    // Preconditions: productionObjects is not null
    // Postcondition: The comparable productionObjects object is removed from the list
    public void removeComparedObject(T data) throws ArrayStoreException {
        SortedIterator iterator = new SortedIterator();
        
        while (iterator.hasNext()) {
            if (data.compareTo(iterator.next()) == 0) {
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
    // Postcondition: The Count of objects in the list is returned
    public int count() {
        return this.count;
    }
    
    // Preconditions: None
    // Postcondition: A reference to the first object in the list is returned. null is returned if the list is empty
    public T firstObject() {
        return this.sentinel.getNext().getData();
    }
    
    // Preconditions: None
    // Postcondition: A reference to the last object in the list is returned. null is returned if the list is empty
    public T lastObject() {
        return this.sentinel.getPrevious().getData();
    }
    
    // Preconditions: None
    // Postcondition: A boolean is returned  stating if the list is empty
    public boolean isEmpty() {
        return (this.sentinel.getNext() == this.sentinel);
    }
    
    // **   ==  ==  ==  ==  ==  ==
    //      Output  ==  ==  ==  ==
    // **   ==  ==  ==  ==  ==  ==
    
    // Preconditions: None
    // Postcondition: A string containing the contents of the list is returned
    public String toString() {
        StringBuffer buffer = new StringBuffer(this.hashCode() + " {\n");   // Create a StringBuffer (in lieu of a mutable string)
        
        SortedIterator iterator = new SortedIterator();
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
        return new SortedIterator();
    }
    
    private class SortedIterator implements Iterator<T> {
        private Node<T> current;
        private int modCount;   // expected mod count
        private boolean nextHasBeenCalled;
        
        public SortedIterator() {
            this.current = SortedList.this.sentinel;
            this.modCount = SortedList.this.modCount;
            this.nextHasBeenCalled = false;
        }
        
        // Preconditions: None
        // Postcondition: A boolean is returned showing if there is another item in the list
        public boolean hasNext() {
            return (this.current.getNext() != SortedList.this.sentinel);
        }
        
        // Preconditions: None
        // Postcondition: A reference to the nextStorage object in the list is returned. Throws exceptions if the list is out of sync or if there are no more objects in the list
        public T next() throws ConcurrentModificationException, NoSuchElementException {
            if (this.modCount != SortedList.this.modCount)
                throw new ConcurrentModificationException("Iterator " + this.hashCode() + " is out of sync");
            
            if (!this.hasNext())
                throw new NoSuchElementException("List " + SortedList.this.hashCode() + " has no more elements");
            
            this.nextHasBeenCalled = true;          // Set the nextHasBeenCalled flag to true
            this.current = this.current.getNext();  // Increment the current pointer
            
            return this.current.getData();
        }
        
        // Preconditions: nextStorage() has been called, otherwise an exception is returned
        // Postcondition: The current item pointed to by the iterator is removed. An exception is thrown if the list is out of sync or if nextStorage has not been called
        public void remove() {
            if (this.modCount != SortedList.this.modCount)
                throw new ConcurrentModificationException("Iterator " + this.hashCode() + " is out of sync");
                
            if (!this.nextHasBeenCalled)
                throw new ConcurrentModificationException("Next has not been called on iterator " + this.hashCode());
            
            this.nextHasBeenCalled = false;
            
            Node<T> target = this.current;              // Remove the Node from the list
            this.current = this.current.getPrevious();  //
                                                        //
            this.current.setNext(target.getNext());     //
            target.getNext().setPrevious(this.current); //
                                                        //
            this.modCount++;                            //
            SortedList.this.modCount++;                 //
            SortedList.this.count--;                    //
        }
    }
}