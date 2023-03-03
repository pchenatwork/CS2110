package cs2110;

import java.util.Iterator;
import java.util.NoSuchElementException;

/*
 * Assignment metadata
 * Name(s) and NetID(s): Stanley Chen (syc33)
 * Hours spent on assignment: TODO
 */

/**
 * A list of elements of type `T` implemented as a singly linked list.  Null elements are not
 * allowed.
 */
public class LinkedSeq<T> implements Seq<T> {

    /**
     * Number of elements in the list.  Equal to the number of linked nodes reachable from `head`.
     */
    private int size;

    /**
     * First node of the linked list (null if list is empty).
     */
    private Node<T> head;

    /**
     * Last node of the linked list starting at `head` (null if list is empty).  Next node must be
     * null.
     */
    private Node<T> tail;

    /**
     * Assert that this object satisfies its class invariants.
     */
    private void assertInv() {
        assert size >= 0;
        if (size == 0) {
            assert head == null;
            assert tail == null;
        } else {
            assert head != null;
            assert tail != null;
/*## Paul's note ## */

            // TODO 0: check that the number of linked nodes is equal to this list's size and that
            // the last linked node is the same object as `tail`.
            var temp = head; 
            int iCount = head==null? 0 : 1;  /** PCHEN Explain ** */
            while(temp.next()!=null){
                temp = temp.next();
                iCount++;
            }
            assert temp == tail :  "assertInv() Fail: 'tail' doesn't match.";
            assert tail.next() == null :  "assertInv() Fail: 'tail' is not pointing to 'null'.";
            assert size == iCount :  "assertInv() Fail: 'count' doesn't match to 'size'.";
            // End of To Do 0
/*## End ## */

            /* ## Stanley's implementation # can be optimized in Big(O)
            // Check that the number of linked nodes is equal to this list's size
            int count = 0;
            Node<T> curr = head;
            while (curr != null) {
                count++;
                curr = curr.next();
            }
            assert count == size : "assertInv() Fail: 'count' doesn't match to 'size'.";

            // Check that the last linked node is the same object as `tail`.
            curr = head;
            while (curr.next() != null) {
                curr = curr.next();
            }
            assert curr == tail : "assertInv() Fail: 'tail' doesn't match.";
 ## */
            // TODO 0: check that the number of linked nodes is equal to this list's size and that
            // the last linked node is the same object as `tail`.
        }
    }

    /**
     * Create an empty list.
     */
    public LinkedSeq() {
        size = 0;
        head = null;
        tail = null;

        assertInv();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void prepend(T elem) {
        assertInv();
        assert elem != null;

        head = new Node<>(elem, head);
        // If list was empty, assign tail as well
        if (tail == null) {
            tail = head;
        }
        size += 1;

        assertInv();
    }

    /**
     * Return a text representation of this list with the following format: the string starts with
     * '[' and ends with ']'.  In between are the string representations of each element, in
     * sequence order, separated by ", ".
     * <p>
     * Example: a list containing 4 7 8 in that order would be represented by "[4, 7, 8]".
     * <p>
     * Example: a list containing two empty strings would be represented by "[, ]".
     * <p>
     * The string representations of elements may contain the characters '[', ',', and ']'; these
     * are not treated specially.
     */
    @Override
    public String toString() {
        String str = "[";
        Node<T> curr = head;

        /*## Paul's implementation */
        StringBuilder sb = new StringBuilder();

        int iCount = 0 ; // ###PCHEN### add a counter to exit circular loop 
        while (curr != null){
            sb.append(curr.data());

            //## un-comment me to see circular reference ###
            //### if (curr == tail) break; // ###PCHEN### break the loop when reach tail

            curr = curr.next();            
            if (curr != null) {
                sb.append(", ");
            }
            // ###PCHEN### add a counter to exit circular loop 
            iCount++;
            if (iCount > 100) {
                sb.append(" ..... ");
                 break; 
            }
        }
        str += sb.toString();
        /* end of Paul's ##*/

        /**## Stanley's implementation ##
        while (curr != null) {
            str += curr.data();
            curr = curr.next();
            if (curr != null) {
                str += ", ";
            }
        }
        ## **/
        // TODO 1: Complete the implementation of this method according to its specification.
        // Unit tests have already been provided (you do not need to add additional cases).
        str += "]";
        return str;
    }

    @Override
    public boolean contains(T elem) {
        Node<T> curr = head;
        while (curr != null) {
            if (curr.data().equals(elem)) {
                return true;
            }
            curr = curr.next();
        }
        return false;
        // TODO 2: Write unit tests for this method, then implement it according to its
        // specification.  Tests must check for `elem` in a list that does not contain `elem`, in a
        // list that contains it once, and in a list that contains it more than once.
    }

    @Override
    public T get(int index) {
        assertInv();
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        Node<T> curr = head;
        for (int i = 0; i < index; i++) {
            curr = curr.next();
        }
        assertInv(); /**## no need to have assertInv() here for no data operation ##**/
        return curr.data();
        // TODO 3: Write unit tests for this method, then implement it according to its
        // specification.  Tests must get elements from at least three different indices.
    }

    @Override
    public void append(T elem) {
        // Condion check, make sure the operation is legit.
        assert elem != null;


        Node<T> successor = new Node<>(elem, null);
        if (head == null) {
            /*## ##head = successor; ##*/
            head = tail = successor;
        } else {
            Node<T> curr = head;
            while (curr.next() != null) {
                curr = curr.next();
            }
            curr.setNext(successor);
            tail = successor; /**## You forgot to set tail after 'append()' ##*/
        }
        size++;
        // TODO 4: Write unit tests for this method, then implement it according to its
        // specification.  Tests must append to lists of at least three different sizes.
        // Implementation constraint: efficiency must not depend on the size of the list.
    }

    @Override
    public void insertBefore(T elem, T successor) {
        //## Paul's Implementation ## assertions check
        assert contains(successor): "'Successor' does not exist."; /**## ##**/
        assert elem != null : "'elem' can not be null."; 

        // 'prev' is the Node before 'curr'
        var curr = head; // ini 'curr' pointing to header.
        Node<T> prev = null; // node before 'curr',  ini to null 
        while (curr!=null) {
            if (curr.data().equals(successor)){ 
                // successor found. 
                // creating 'NewNode' that is pointing to 'successor'
                var myNewNode = new Node<T>(elem, curr);
                // if curr is head (myNode is null), myNewNode becomes the new head
                if (prev == null) {
                    head = myNewNode;
                } 
                else {
                    prev.setNext(myNewNode);
                }    
                size++; 
                return;   // exit loop  
            }
            prev = curr;
            curr = curr.next();
        }
        /** ## End of Paul's Implementation ## */

        /** ## Stanley's implementation ##
        Node<T> newNode = new Node<>(elem, null);
        if (head == null) {
            throw new NoSuchElementException("The list is empty.");
        }
        Node<T> curr = head;
        Node<T> prev = null;
        while (curr != null && !curr.data().equals(successor)) {
            prev = curr;
            curr = curr.next();
        }
        if (curr == null) {
            throw new NoSuchElementException("Successor not found.");
        }
        if (prev == null) {
            head = newNode;
        } else {
            prev.setNext(newNode);
        }
        newNode.setNext(curr);

        size++;
        ## */
        // Tip: Since there is a precondition that `successor` is in the list, you don't have to
        // handle the case of the empty list.  Asserting this precondition is optional.
        // TODO 5: Write unit tests for this method, then implement it according to its
        // specification.  Tests must insert into lists where `successor` is in at least three
        // different positions.
    }

    @Override
    public boolean remove(T elem) {
        /* ### Paul's implementation ### */
        // 'prev' is the Node before 'curr'
// $$ condition check $$ make sure 'remove' operation is legit

        Node<T> prev = null; // node before 'curr',  ini to null 
        Node<T> curr = head;// ini 'curr' pointing to header.
        int iRemovedCount = 0;  
        while (curr!=null) {
            if (curr.data().equals(elem)){ 
                // ** Node (curr) located **. 
                // if curr is head (prev is null), reset head
                // if curr is tail (curr.next()) == null), reset tail
                // otherwise ....
                if (curr == head || curr ==tail) {
                    head = (curr == head) ? head.next() : head;
                    if (curr == tail) {
                        // set 'prev' as last node
                        tail = prev;
                        if (prev!=null) {
                            tail.setNext(null);
                        }                    
                    }
                } else {
                    prev.setNext(curr.next());
                }
                iRemovedCount ++;
                size--; 
            }
            prev = curr;
            curr = curr.next();
        }
        return iRemovedCount>0;

        /* ## Stanly's implementation ## has bug ##
        Node<T> prev = null;
        Node<T> curr = head;
        boolean removed = false;
        while (curr != null) {
            if (curr.data().equals(elem)) {
                if (prev == null) {
                    head = curr.next();
                } else {
                    prev.setNext(curr.next());
                }
                size--;
                removed = true;
            } else {
                prev = curr;
            }
            curr = curr.next();
        }
        return removed;
        ## */
        // TODO 6: Write unit tests for this method, then implement it according to its
        // specification.  Tests must remove `elem` from a list that does not contain `elem`, from a
        // list that contains it once, and from a list that contains it more than once.
    }

    /**
     * Return whether this and `other` are `LinkedSeq`s containing the same elements in the same
     * order.  Two elements `e1` and `e2` are "the same" if `e1.equals(e2)`.  Note that `LinkedSeq`
     * is mutable, so equivalence between two objects may change over time.  See `Object.equals()`
     * for additional guarantees.
     */
    @Override
    public boolean equals(Object other) {
        // Note: In the `instanceof` check, we write `LinkedSeq` instead of `LinkedSeq<T>` because
        // of a limitation inherent in Java generics: it is not possible to check at run-time
        // what the specific type `T` is.  So instead we check a weaker property, namely,
        // that `other` is some (unknown) instantiation of `LinkedSeq`.  As a result, the static
        // type returned by `currNodeOther.data()` is `Object`.
        if (!(other instanceof LinkedSeq)) {
            return false;
        }
        LinkedSeq otherSeq = (LinkedSeq) other;
        Node<T> currNodeThis = head;
        Node currNodeOther = otherSeq.head;

        while (currNodeThis != null && currNodeOther != null) {
            if (!currNodeThis.data().equals(currNodeOther.data())) {
                return false;
            }
            currNodeThis = currNodeThis.next();
            currNodeOther = currNodeOther.next();
        }
        return true;

        // TODO 7: Write unit tests for this method, then finish implementing it according to its
        // specification.  Tests must compare at least three different pairs of lists.
    }

    /**
     * ###PCHEN### Custom method to demostrate shared node
     * @param right
     */
    public void joinLinkedSeq(LinkedSeq<T> right)
    {
        if (head==null) {
            head = right.head;
            tail = right.tail;
        } else {
            tail.setNext(right.head);
            tail=right.tail;
        }
        size += right.size;
    }
    /**
     * ###PCHEN### Custom method to demostrate shared node
     * Read : https://www.geeksforgeeks.org/function-overloading-c/
     * @param index
     * @param elem
     */
    public void updateNode(int index, T elem){
        Node<T> node = head;
        for (int i = 0; i < index; i++) {
            node = node.next();
        }
        node.data(elem);
    }

    /*
     * There is no need to read the remainder of this file for the purpose of completing the
     * assignment.  We have not yet covered the implementation of these concepts in class.
     */

    /**
     * Returns a hash code value for the object.  See `Object.hashCode()` for additional
     * guarantees.
     */
    @Override
    public int hashCode() {
        // Whenever overriding `equals()`, must also override `hashCode()` to be consistent.
        // This hash recipe is recommended in _Effective Java_ (Joshua Bloch, 2008).
        int hash = 1;
        for (T e : this) {
            hash = 31 * hash + e.hashCode();
        }
        return hash;
    }

    /**
     * Return an iterator over the elements of this list (in sequence order).  By implementing
     * `Iterable`, clients can use Java's "enhanced for-loops" to iterate over the elements of the
     * list.  Requires that the list not be mutated while the iterator is in use.
     */
    @Override
    public Iterator<T> iterator() {
        assertInv();

        // Return an instance of an anonymous inner class implementing the Iterator interface.
        // For convenience, this uses Java features that have not eyt been introduced in the course.
        return new Iterator<>() {
            private Node<T> next = head;

            public T next() throws NoSuchElementException {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T result = next.data();
                next = next.next();
                return result;
            }

            public boolean hasNext() {
                return next != null;
            }
        };
    }
}
