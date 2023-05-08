package datastructures;

 //* ============ Notes: PCHEN ===========
 // A Linked-list Queue (FIFO)
 //* ======================================

public class MyQueue <T> {
    /**
     * Number of elements in the stack.  
     */
    private int _size;
    
    /**
     * First node of the linked list (null if list is empty).
     */
    private Node<T> _head;
    private Node<T> _tail;
    
    /** Creates: an empty queue with linked-list implementation
     */
    public MyQueue(){
        _size = 0;
        _head = _tail = null;
    } 
    /**
     * Push an element into the Queue. The element gets pushed to the tail of the Queue.
     * @param t
     */
    public void push(T t){
        assert t!=null;  // make sure NULL object can not be pushed to the Stack
        Node<T> newNode = new Node<T>(t, null);
        if (_size==0){
            _head=_tail=newNode;
        } 
        else {
            _tail.next(newNode);
        }
        _size++;
    }
    /**
     * Returns the element present at the top of the stack and then removes it.
     * @return
     */
    public T pop(){
        if (_head!=null){
            Node<T> node = _head;
            _head = _head.next();
            _size --;
            return node.data();
        }
        return null;
    }
    /**
     * Returns the element at the top of the Stack else returns NULL if the Stack is empty.
     */
    public T peek(){
        if (_head!=null) return _head.data();
        return null;
    }
    public boolean isEmpty(){return _size==0;}    
}

