//###############
// FILE : SyncedHashSet.java
// WRITER : Elia Grady, eliagrady, 300907060
// EXERCISE : oop ex3 2011
// DESCRIPTION : Represents a synchronized hash set.
//###############
package oop.ex3.resources;
import java.util.*;

/**
 * Represents a synchronized hash set
 */
public class SyncedHashSet<T> {
       private HashSet<T> _hashSet;
       
       /**
        * constructs a new empty synchronized hash set.
        */
       public SyncedHashSet() {
    	   _hashSet = new HashSet<T>();
       }
       
       /**
        * Return a list of current items in the set.
        * @return an array list that contains all items currently in the set.
        */
       public ArrayList<T> getList() {
               synchronized(this) {
                       return new ArrayList<T>(_hashSet);
               }
       }
       
       /**
        * Check if there's an instance of the given item already in the set
        * @param item the item to check for it's existence.
        * @return true if the set contains the given item, false otherwise.
        */
       public boolean contains(T item) {
               synchronized(this) {
                       return _hashSet.contains(item);
               }
       }
       
       /**
        * adds the given item to the list, if absent
        * @param item the item to add
        */
       public void putIfAbsent(T item) {
               synchronized(this) {
            	   _hashSet.add(item);
               }
       }
      
       /**
        * Removes item from the set
        * @param item the item to remove
        */
       public void remove(String item) {
               synchronized(this) {
            	   _hashSet.remove(item);
               }
       }
}