package cs2110;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.*;

public class _MyTest {
    
    @Test 
    public void sharedNodesDemo(){
        /* Demostrate LinkedSeq() with shared nodes
         * joinLinkedSeq() + updateNode() are introduced
         * 1. Node.java was immutable, remove 'final' to make Node mutable
         * 2. make a 'setter' to Node.java
         * 3. introduced joinLinkedSeq() to append LinkedSeq<>
         * 4. introduced updateNode(index, value) to update a Node in LinkedSeq<>
         * This is very interesting example to demo the linked list with circular reference
         */
        var line1 = new LinkedSeq<String>();
        var line2 = new LinkedSeq<String>();
        line1.append("a1");
        line1.append("a2");
        line2.append("b1");
        line2.append("b2");          
        System.out.println("Line1 before  = " + line1.toString()); 
        System.out.println("Line2 before  = " + line2.toString()); 

        var shared = new LinkedSeq<String>();
        shared.append("X0");
        shared.append("X1");
        
        System.out.println("shared  = " + shared.toString()); 

        line1.joinLinkedSeq(shared);
        line2.joinLinkedSeq(shared);    
        
        System.out.println("Line1 after1  = " + line1.toString()); 
        System.out.println("Line2 after1  = " + line2.toString()); 

        shared.updateNode(0, "0X");
        System.out.println("Shared[0]  => " + "0X"); 
        
        System.out.println("Line1 after2  = " + line1.toString()); 
        System.out.println("Line2 after2  = " + line2.toString()); 

        line1.joinLinkedSeq(line2);
        System.out.println("Line 1 + line 2  => " + line1.toString()); 
        System.out.println("line 2  => " + line2.toString()); 

        assertEquals(1, 1);
    }

    @Test
    public void nullTester(){
                
        /// https://www.geeksforgeeks.org/interesting-facts-about-null-in-java/

        /// https://courses.engr.illinois.edu/cs225/sp2023/resources/stack-heap/
        /**
         *    The Java programming language has a built-in null type, called “null”, 
         *    which is a subtype of all reference types
         *  it cannot be used as a type for a variable, because it doesn’t have any instance and cannot be instantiated.
         */
        String NullStr=null;

        //String NullStr;
        Student S = null;
        assertEquals(NullStr, S); // TRUE

        Course C = null;
        assertEquals(C, S);  // TRUE

        Date D = null;
        assertEquals(NullStr, D);  // TRUE
        
        System.out.println("Date(null) =  '" + D + "'");
        System.out.println("String(null) =  '" + NullStr + "'");
        // System.out.println("S(null) =  " + S.toString()); /* ## will fail because not initiated :  ## */

        String a1 = null;
        String a2 = null;
        assertEquals(a1, a2); // TRUE

        //assertNotEquals(a1, a2);
         a1 = " ";
         a2 = a1.trim();

        assertEquals("", a2);

    }
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
        System.out.println("new lst =  " + lst.toString()); // ToDo1 toString() test

        // ToDo2 contains() test
        // Test Case 1: null element is not allowed
        assertEquals(lst.contains(null), false);
        // Test case 2: 
        lst.append("s1");
        System.out.println("after append('s1') :   " + lst.toString());
        assertEquals(lst.size(), 1);

        lst.insertBefore("s0", "s1");
        System.out.println("after inserBefore('s0', 's1')  " + lst.toString());
        lst.insertBefore("s2", "s1");
        System.out.println("after inserBefore('s2', 's1')  " + lst.toString());
        
        var x0 = lst.get(0);
        var x2 = lst.get(1);
        var x1 = lst.get(2);

        lst.append("s2");
        System.out.println("after append('s2') :   " + lst.toString());

        lst.insertBefore("s20", "s2");
        System.out.println("after inserBefore('s20', 's2')   " + lst.toString());

        lst.prepend("s2");        
        System.out.println("after prepend 's2':   " + lst.toString());

        lst.remove("s1");
        System.out.println("after remove 's1':   " + lst.toString());
        lst.insertBefore("s1", "s0");
        System.out.println("after inserBefore('s1', 's0')   " + lst.toString());



//        assertEquals(lst.get(1), "s0");
//        System.out.println("get(1) shoulbe be 's1', actual  " + lst.get(1));

        assertEquals(lst.contains("s1"), true);
        assertEquals(lst.contains("S1"), false);
    }
}
