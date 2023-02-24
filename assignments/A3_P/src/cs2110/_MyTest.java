package cs2110;

import cs2110.*;
import static org.junit.Assert.assertEquals;
import org.junit.*;

public class _MyTest {
    @Test
    public void Assignment2_Test(){

        var c1 = new Course("Course 1", 3, "Prof 1", "14417 Barclay Ave", 
            23, 30, 30); 
        var c2 = new Course("Course 2", 4, "Prof 2", "617 Liberty Ave", 
            12, 30, 45); 
        System.out.println(c1.formatStartTime());
        System.out.println(c2.formatStartTime());
        assertEquals(c1.overlaps(c2), false );

        var s1 = new Student("F1", "L1");
        var s2 = new Student("F1", "L1");
        var s3 = new Student("F3", "L3");
        var s4 = new Student("F4", "L4");
        var S5 = new Student("F5", "L5");

        assertEquals(c1.hasStudent(s1), false );
        c1.enrollStudent(s1);
        assertEquals(c1.hasStudent(s1), true );

        c1.enrollStudent(s2);
    }

    @Test
    public  void NodeTest(){
        Node<Integer> n1 = new Node<Integer>(111, null);
        
        Node<String> n2 = new Node<String>("S1", null);

        // * Need to cast integer to Integer *
        // **  assertEquals(n1.data(), 111);  // https://stackoverflow.com/questions/8660691/what-is-the-difference-between-integer-and-int-in-java
        assertEquals(n1.data(), (Integer)111); 
        assertEquals(n2.data(), "S1"); 

        //assertEquals(n1, n1.next()); 
        n1.setNext(n1);
        assertEquals(n1, n1.next()); 
        assertEquals(n1.data(), n1.next().data()); 
    } 

    @Test
    public void LinkedSeqTest(){
        var lst = new LinkedSeq<String>();
        assertEquals(lst.size(), 0);
        System.out.println("list is empty => " + lst.toString()); // ToDo1 toString() test

        // ToDo2 contains() test
        // Test Case 1: null element is not allowed
        assertEquals(lst.contains(null), false);
        // Test case 2: 
        lst.append("s1");
        System.out.println("Should be [s1], actual  " + lst.toString());
        assertEquals(lst.size(), 1);

        lst.prepend("");        
        System.out.println("Should be [, s1], actual  " + lst.toString());
        assertEquals(lst.get(2), "s1");
        System.out.println("get(2) shoulbe be 's1', actual  " + lst.get(2));

        assertEquals(lst.contains("s1"), true);
        assertEquals(lst.contains("S1"), false);
    }
}
