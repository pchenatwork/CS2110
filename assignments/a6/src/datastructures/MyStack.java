package datastructures;

 //* ============ Notes: PCHEN ===========
 // Created a Linked-list stack (FILO) to track to visited 'floor tile' in the game board
 // Only push(), pop(), isEmpty() are needed for our usage
 //* ======================================
 
public class MyStack<T> {
    /**
     * Number of elements in the stack.  
     */
    private int _size;
    
    /**
     * First node of the linked list (null if list is empty).
     */
    private Node<T> _head;
    
    /** Creates: an empty queue with linked-list implementation
     */
    public MyStack(){
        _size = 0;
        _head = null;
    } 

    public void push(T t){
        assert t!=null;  // make sure NULL object can not be pushed to the Stack
        Node<T> newNode = new Node<T>(t, _head);
        _head = newNode;
        _size++;
    }
    public T pop(){
        if (_head!=null){
            Node<T> node = _head;
            _head = _head.next();
            _size --;
            return node.data();
        }
        return null;
    }
    public boolean isEmpty(){return _size==0;}    
}
