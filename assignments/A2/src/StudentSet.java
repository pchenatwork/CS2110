//package cs2110;

/**
 * A mutable set of students.
 */
public class StudentSet {
    // Implementation: the StudentSet is implemented as a resizable array data structure.
    // Implementation constraint: do not use any classes from java.util.
    // Implementation constraint: assert preconditions for all method parameters and assert that the
    //     invariant is satisfied at least at the end of every method that mutates any fields.

    /**
     * Array containing the students in the set.  Elements `store[0..size-1]` contain the distinct
     * students in this set, none of which are null.  All elements in `store[size..]` are null.  The
     * length of `store` is the current capacity of the data structure and is at least 1.  Two
     * students `s1` and `s2` are distinct if `s1.equals(s2)` is false.
     */
    private Student[] store;

    /**
     * The number of distinct students in this set.  Non-negative and no greater than
     * `store.length`.
     */
    private int size;

    /**
     * Assert that this object satisfies its class invariants.
     */
    private void assertInv() {
        assert store != null && store.length > 0;
        assert size >= 0 && size <= store.length;

        for (int i = 0; i < size; ++i) {
            // Check that elements in use are non-null
            assert store[i] != null;

            // Check that students are all distinct
            for (int j = i + 1; j < size; ++j) {
                assert !store[i].equals(store[j]);
            }
        }

        // Check that unused capacity is all null
        for (int i = size; i < store.length; ++i) {  //*####*  ++i vs i++ ?? pre-increment vs post-increment *####*
       // for (int i = size; i < store.length; ++i) {  //*####*  ++i vs i++ ?? pre-increment vs post-increment *####*
            assert store[i] == null;
        }
    }

    /**
     * Create an empty set of students.
     */
    public StudentSet() {
        // TODO 9: Implement this constructor according to its specification
        // You will need to decide on an initial capacity for your backing array (the capacity is
        // not observable by the client, so this is not constrained by the method spec).
        // The choice is a tradeoff between potentially wasted space vs. potentially needing to
        // resize the backing array sooner.  Choose something "small," say, less than 20 (the exact
        // value is up to you).  Don't forget to assert that invariants are satisfied (this is the
        // last time we'll remind you).

        // throw new UnsupportedOperationException();

        store = new Student[3];
        size = 0;
        assertInv();
    }

    /**
     * Return the number of students in this set.
     */
    public int size() {
        // TODO 10: Implement this method according to its specification
        //throw new UnsupportedOperationException();
        return size;
    }

    /**
     * Effect: Add student `s` to the set.  Requires `s` is not already in the set.
     */
    public void add(Student s) {
        // TODO 11: Implement this method according to its specification
        // If the backing array runs out of space, create a new backing array with twice the
        // capacity and copy all elements from the old array to it.  Consider delegating this task
        // to a helper method.
        // We recommend proceeding as follows:
        // 1. Write a small test case to add a student and check the impact on the set's size.
        //    Confirm that it fails.
        // 2. Implement a basic version of `add()`, ignoring the resizing requirements above.
        //    Confirm that your test case passes.
        // 3. Add a larger test case that adds more students than your initial capacity (hint: use a
        //    loop).  Confirm that it fails.
        // 4. Implement the resizing logic for `add()`.  Confirm that your new test case passes.
        // If you're not sure how to check a precondition, leave yourself a TODO and move on; you
        // might be inspired by a later task.
        // throw new UnsupportedOperationException();
        
        // Step 1:  Make sure student 's' doesn't exists yet;
        if (contains(s))
            throw new UnsupportedOperationException("Student '" + s.toString() + "'' already exists!");
        assert !contains(s) : "Student '" + s.toString() + "'' already exists!";  // ?? Should we use Exception ??

        // Step 2: Make sure store[] is not full, if so increase store[]
        if (size == store.length) increaseStorage();

        // Step 3: Store 's' at array[size]
        store[size] = s;

        // Step 4: increase Size by 1
        size++;         
    }

    /**
     * Helper method to increase backing array to twice the capacity
     */
    private void increaseStorage(){
        int iNewSize = store.length * 2;

        // Create a new Student array with double the size
        var temp = new Student[iNewSize]; 
        // Copy students to new storage
        for(int i = 0; i<size; i++){
            temp[i] = store[i];
        }
        // Pointing the internal storage to new student array
        store = temp;
    }

    /**
     * Return whether this set contains student `s`.
     */
    public boolean contains(Student s) {
        // TODO 12: Implement this method according to its specification
        // throw new UnsupportedOperationException();
        boolean bFound = false; // assume not found
        for(int i = 0; i<size; i++){
             //* ### */  if (store[i].equals(s)){ /* ### ?? This doesn't work ????? */
             if (store[i].toString().equals(s.toString())){
                bFound = true; break; 
            }
        }
        return bFound;
    }

    /**
     * Effect: If student `s` is in this set, remove `s` from the set and return true. Otherwise
     * return false.
     */
    public boolean remove(Student s) {
        // TODO 13: Implement this method according to its specification
        // You are welcome to decompose this task into operations that can be performed by
        // "helper methods", which you may define below.
        // throw new UnsupportedOperationException();
        boolean bOK = false; // Assume failure
        if (!this.contains(s))
            return bOK;

        for(int i = 0; i<size; i++){
            if (store[i].toString().equals(s.toString())){
                // Student 's' is located in store[]
                // Step 1: Check if 'S' is the last item in the array, if not move all trailing items one spot ahead
                if (i<size-1) { 
                    // 's' is not the last item in the array, shift all trailing items
                    for (int j = i+1; j<size; j++){
                            store[j-1]=store[j];
                    }
                }
                // step 2: set array last item to NULL
                store[size-1] = null;
                // step 3: decrease array SIZE by 1
                size--;
                bOK = true;     
                break; // break out of the for loop           
            }
        }
        return bOK;
    }

    /**
     * Return the String representation of this StudentSet.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        for (int i = 0; i < size; ++i) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(store[i]);
        }
        sb.append("}");
        return sb.toString();
    }
}
