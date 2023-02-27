package cs2110;

import java.util.Iterator;
import java.util.NoSuchElementException;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.*;

public class LinkedSeqTest {

    // Helper functions for creating lists used by multiple tests.  By constructing strings with
    // `new`, more likely to catch inadvertent use of `==` instead of `.equals()`.

    /**
     * Creates [].
     */
    static Seq<String> makeList0() {
        return new LinkedSeq<>();
    }

    /**
     * Creates ["A"].  Only uses prepend.
     */
    static Seq<String> makeList1() {
        Seq<String> ans = new LinkedSeq<>();
        ans.prepend(new String("A"));
        return ans;
    }

    /**
     * Creates ["A", "B"].  Only uses prepend.
     */
    static Seq<String> makeList2() {
        Seq<String> ans = new LinkedSeq<>();
        ans.prepend(new String("B"));
        ans.prepend(new String("A"));
        return ans;
    }

    /**
     * Creates ["A", "B", "C"].  Only uses prepend.
     */
    static Seq<String> makeList3() {
        Seq<String> ans = new LinkedSeq<>();
        ans.prepend(new String("C"));
        ans.prepend(new String("B"));
        ans.prepend(new String("A"));
        return ans;
    }

    /**
     * Creates a list containing the same elements (in the same order) as array `elements`.  Only
     * uses prepend.
     */
    static <T> Seq<T> makeList(T[] elements) {
        Seq<T> ans = new LinkedSeq<>();
        for (int i = elements.length; i > 0; i--) {
            ans.prepend(elements[i - 1]);
        }
        return ans;
    }

    @Test
    public void testConstructorSize() {
        Seq<String> list = new LinkedSeq<>();
        assertEquals(0, list.size());
    }

    @Test
    public void testPrependSize() {
        // List creation helper functions use prepend.
        Seq<String> list;

        list = makeList1();
        assertEquals(1, list.size());

        list = makeList2();
        assertEquals(2, list.size());

        list = makeList3();
        assertEquals(3, list.size());
    }

    @Test
    public void testToString() {
        Seq<String> list;

        list = makeList0();
        assertEquals("[]", list.toString());

        list = makeList1();
        assertEquals("[A]", list.toString());

        list = makeList2();
        assertEquals("[A, B]", list.toString());

        list = makeList3();
        assertEquals("[A, B, C]", list.toString());
    }

    // TODO: Add new test cases here as you implement each method in `LinkedSeq`.  You may combine
    // multiple tests for the _same_ method in the same @Test procedure, but be sure that each test
    // case is visibly distinct (comments are good for this).  You are welcome to compare against an
    // expected `toString()` output in order to check multiple aspects of the state at once (in
    // general, later tests may make use of methods that have previously been tested).

    @Test
    public void testContains() {
        LinkedSeq<String> emptyList = new LinkedSeq<>();
        assertFalse(emptyList.contains("hello"));

        LinkedSeq<String> listWithOneElement = new LinkedSeq<>();
        listWithOneElement.prepend("world");
        assertTrue(listWithOneElement.contains("world"));
        assertFalse(listWithOneElement.contains("hello"));

        LinkedSeq<Integer> listWithMultipleElements = new LinkedSeq<>();
        listWithMultipleElements.prepend(3);
        listWithMultipleElements.prepend(2);
        listWithMultipleElements.prepend(1);
        assertTrue(listWithMultipleElements.contains(2));
        assertTrue(listWithMultipleElements.contains(1));
        assertTrue(listWithMultipleElements.contains(3));
        assertFalse(listWithMultipleElements.contains(4));
    }

    @Test
    public void testGet() {
        LinkedSeq<String> seq = new LinkedSeq<>();
        seq.prepend("hello");
        seq.prepend("world");
        seq.prepend("!");

        // Test getting the first element
        assertEquals("!", seq.get(0));

        // Test getting the second element
        assertEquals("world", seq.get(1));

        // Test getting the last element
        assertEquals("hello", seq.get(2));

        // Test getting an out-of-bounds index (should throw an IndexOutOfBoundsException)
        boolean thrown = false;
        try {
            seq.get(3);
        } catch (IndexOutOfBoundsException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void testAppend() {
        LinkedSeq<Integer> list = new LinkedSeq<>();
        assertEquals(0, list.size());

        list.append(1);
        assertEquals(1, list.size());
        assertEquals("[1]", list.toString());

        list.append(2);
        assertEquals(2, list.size());
        assertEquals("[1, 2]", list.toString());

        list.append(3);
        assertEquals(3, list.size());
        assertEquals("[1, 2, 3]", list.toString());
    }

    @Test
    public void testInsertBefore() {
        LinkedSeq<String> list = new LinkedSeq<>();

        // Test inserting into an empty list
        //list.insertBefore("a", "b");
        list.append("a");
        assertEquals("[a]", list.toString());

        // Test inserting before the head
        list.insertBefore("b", "a");
        assertEquals("[b, a]", list.toString());

        // Test inserting before the tail
        list.append("c");
        list.insertBefore("d", "c");
        assertEquals("[b, a, d, c]", list.toString());

        // Test inserting before a middle node
        list.insertBefore("e", "a");
        assertEquals("[b, e, a, d, c]", list.toString());

        // Test inserting before a node that doesn't exist in the list
        try {
            list.insertBefore("f", "z");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Successor not found in list: z", e.getMessage());
        }
    }

    @Test
    public void testRemove() {
        LinkedSeq<Integer> list = new LinkedSeq<>();
        // Test removing from empty list
        assertFalse(list.remove(3));

        // Test removing from list with one element
        list.append(3);
        assertTrue(list.remove(3));
        assertFalse(list.contains(3));

        // Test removing from list with multiple elements
        list.append(3);
        list.append(5);
        list.append(3);
        // assertTrue(list.remove(3));
        
        System.out.println("list = " + list.toString());
        assertTrue(list.contains(5));
        assertTrue(list.contains(3));
        assertTrue(list.remove(3));
        assertFalse(list.contains(3));
        assertTrue(list.remove(5));
        assertFalse(list.contains(5));
        assertFalse(list.remove(7));
    }
    @Test
    public void testRemove2() {
        LinkedSeq<String> lst;
        // Test 1: Remove none exist value from an empty string
        lst = new LinkedSeq<>();
        assertFalse(lst.remove("s1")); 

        // Test : removed the only element in list
        lst = new LinkedSeq<>();
        lst.append("s1");
        assertTrue(lst.remove("s1")); 

        // Test : remove head 
        lst = new LinkedSeq<>();
        lst.append("s1");
        lst.append("s2");
        assertTrue(lst.remove("s1")); 

        System.out.println("lst = " + lst.toString());
        var x1 = lst.get(0);
   //     var x2 = lst.get(1);



        // Test : remove tail 
        lst = new LinkedSeq<>();
        lst.append("s1");
        lst.append("s2");
        assertTrue(lst.remove("s2")); 
        // Test : remove tail 
        lst = new LinkedSeq<>();
        lst.append("s1");
        lst.insertBefore("s0", "s1");
        lst.prepend("s0");
        lst.append("s0");

        System.out.println("before remove :  " + lst.toString());
        assertTrue(lst.remove("s0")); 
        System.out.println("after remove :  " + lst.toString());
    }

    @Test
    public void testEquals() {
        LinkedSeq<String> seq1 = new LinkedSeq<>();
        LinkedSeq<String> seq2 = new LinkedSeq<>();
        LinkedSeq<String> seq3 = new LinkedSeq<>();
        seq1.append("apple");
        seq1.append("banana");
        seq1.append("cherry");
        seq2.append("apple");
        seq2.append("banana");
        seq2.append("cherry");
        seq3.append("apple");
        seq3.append("cherry");
        seq3.append("banana");

        // Test reflexivity
        assertEquals(seq1, seq1);
        assertEquals(seq2, seq2);
        assertEquals(seq3, seq3);

        // Test symmetry
        assertEquals(seq1, seq2);
        assertEquals(seq2, seq1);
        assertNotEquals(seq1, seq3);
        assertNotEquals(seq3, seq1);

        // Test transitivity
        LinkedSeq<String> seq4 = new LinkedSeq<>();
        seq4.append("apple");
        seq4.append("banana");
        seq4.append("cherry");
        assertEquals(seq1, seq2);
        assertEquals(seq2, seq4);
        assertEquals(seq1, seq4);

        // Test with a non-LinkedSeq object
        assertNotEquals("apple,banana,cherry", seq1);
    }

    /*
     * There is no need to read the remainder of this file for the purpose of completing the
     * assignment.  We have not yet covered `hashCode()` or `assertThrows()` in class.
     */

    @Test
    public void testHashCode() {
        assertEquals(makeList0().hashCode(), makeList0().hashCode());

        assertEquals(makeList1().hashCode(), makeList1().hashCode());

        assertEquals(makeList2().hashCode(), makeList2().hashCode());

        assertEquals(makeList3().hashCode(), makeList3().hashCode());
    }

    @Test
    public void testIterator() {
        Seq<String> list;
        Iterator<String> it;

        list = makeList0();
        it = list.iterator();
        assertFalse(it.hasNext());
        Iterator<String> itAlias = it;
        assertThrows(NoSuchElementException.class, () -> itAlias.next());

        list = makeList1();
        it = list.iterator();
        assertTrue(it.hasNext());
        assertEquals("A", it.next());
        assertFalse(it.hasNext());

        list = makeList2();
        it = list.iterator();
        assertTrue(it.hasNext());
        assertEquals("A", it.next());
        assertTrue(it.hasNext());
        assertEquals("B", it.next());
        assertFalse(it.hasNext());
    }
}
