package datastructures;

 //* ============ Notes: PCHEN ===========
 // Linked-list implementation of PriorityQueue
 //* ======================================
 
/**
 * Using listed-list to implement PriorityQueue, Node<T> class is borrowed from Assignment 3
 */
public class MyPQueue<E> implements PQueue<E> {
    record PrioElem<E>(E elem, double priority) {};   
    /**
     * Number of elements in the list.  Equal to the number of linked nodes reachable from `head`.
     */
    private int _size;

    /**
     * First node of the linked list (null if list is empty).
     */
    private Node<PrioElem<E>> _head;

    /** Creates: an empty queue with linked-list implementation
     */
    public MyPQueue(){
        _size = 0;
        _head = null;
    } 

    @Override public int size() { return _size; }
    @Override public boolean isEmpty() { return _size==0; }
    @Override public E peek() {
        // Head of the queue is always the one to be popped 
        return _head.data().elem;
    }
    @Override
    public void add(E e, double priority) throws IllegalArgumentException {
        Node<PrioElem<E>> noteNew = new Node<>(new PrioElem<>(e, priority), null);
        Node<PrioElem<E>> nodeBefore = null;  // Point to the node the 'NewNode' will be inserted aftger

        /* Loop through the list to
        // 1: check if 'e' already exists, AND
        // 2: to locate the node where the 'New Node' will be inserted after
        */
        Node<PrioElem<E>> nodeRunning = _head; 
        while (nodeRunning != null) {
            if (nodeRunning.data().elem.equals(e)) {
                // Throw exception if 'e' already exists
                throw new IllegalArgumentException();
            }
            if (nodeRunning.data().priority <= priority )
                nodeBefore = nodeRunning;
            nodeRunning = nodeRunning.next();
        }

        if (nodeBefore==null) {
            /* this happens when 
                1. all existing priorities are greater than this 'priority', or
                2. list is empty
              then add to 'head' of the list
            */
            noteNew.next(_head);
            _head = noteNew;
        } else {
            noteNew.next(nodeBefore.next());
            nodeBefore.next(noteNew);            
        }
        // increase the list size by 1
        _size ++;
    }

    @Override
    public E extractMin() {
        // with linked list implementation, extractMin means always pop the 'head'
        assert _size>0;
        
        if (_head != null) {
            var x = _head;
            _head = _head.next();
            _size--;
            return x.data().elem;          
        }  
        return null;  // Pop 'NULL' when list is empty;
    }

    @Override
    public void changePriority(E e, double priority) {
        // Loop throught the list until 'e' if found        
        Node<PrioElem<E>> nodeRunning = _head; 
        while (nodeRunning != null) {
            if (nodeRunning.data().elem.equals(e)) {
                nodeRunning.data(new PrioElem<>(e, priority));
                return;
            }
            nodeRunning = nodeRunning.next();
        }
        assert false;
    }
    /**
     * equally add priority to all existing items in the queue.
     * **Notes**: this is to be used in the McDiver.Seek() method
     * @param priority
     */
    public void addPriority(double priority){
        // Loop throught the list       
        Node<PrioElem<E>> nodeRunning = _head; 
        while (nodeRunning != null) {
            changePriority(nodeRunning.data().elem, nodeRunning.data().priority + priority);
            nodeRunning = nodeRunning.next();
        }
    }
}
