package datastructures;

 //* ============ Notes: PCHEN ===========
 // A Linked-list stack (FILO)
 // Introduce to track the current 'route' that has taken in the game board from Entrance
 // push(), pop(), peek(), isEmpty(), exists(), size() are exposed
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
    /**
     * Push an element into the Stack. The element gets pushed onto the top of the Stack.
     * @param t
     */
    public void push(T t){
        assert t!=null;  // make sure NULL object can not be pushed to the Stack
        Node<T> newNode = new Node<T>(t, _head);
        _head = newNode;
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
    /**
     * Check to see if an element is in the Stack
     * 
     * @param v
     * @return true if the element is in the Stack
     */ 
    public boolean exists(T v) {
        var node = _head;
        while(node != null){{
            if (node.data().equals(v)){
                return true;
            }
            node = node.next();
        }}
        return false;
    }
    public int size(){
        return _size;
    }
}
