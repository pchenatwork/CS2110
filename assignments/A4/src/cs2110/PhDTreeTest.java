package cs2110;

import static org.junit.jupiter.api.Assertions.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;

public class PhDTreeTest {

    // These pre-defined Professor and PhDTree objects may be used to simplify the setup for your
    // test cases.  You are encouraged to add your own helper methods (even `tree3()` would be
    // considered "trivial", since no node has more than 1 child).
    private static final Professor prof1 = new Professor("Amy Huang", 2023);
    private static final Professor prof2 = new Professor("Maya Leong", 2023);
    private static final Professor prof3 = new Professor("Matthew Hui", 2025);
    private static final Professor prof4 = new Professor("Arianna Curillo", 2022);
    private static final Professor prof5 = new Professor("Michelle Gao", 2022);
    private static final Professor prof6 = new Professor("Isa Siu", 2024);
    
    private static final Professor Prof_22 = new Professor("Prof_22", 2025);
    private static final Professor Prof_23 = new Professor("Prof_23", 2024);
    private static final Professor Prof_231 = new Professor("Prof_231", 2024);
    private static final Professor Prof_232 = new Professor("Prof_232", 2024);
    private static final Professor Prof_2311 = new Professor("Prof_2311", 2025);
    private static final Professor Prof_23111 = new Professor("Prof_23111", 2025);

    // These helper methods create a copy of each Professor object, which would normally be seen as
    // wasteful.  They do so to help expose bugs involving the use of `==` instead of `.equals()`.
    /**
     * <pre> Tree1 structure: (Only one root node)
     * [Amy Huang] (Prof1)
     * */
    private static PhDTree tree1() {
        /***#### 
         * can be simplified to:
         * return new PhDTree(prof1);
         * $$$$ Why doing so ??? $$$$ ???
         */
        return new PhDTree(new Professor(prof1));
    }
    /**
     * <pre> Tree2 structure: (Only one root node)
     * [Arianna Curillo] (Prof4)
     * */
    private static PhDTree tree2() {
        return new PhDTree(new Professor(prof4));
    }
    /**
     * <pre> Tree3 structure: (Linear tree)
     * 
     * [Amy Huang] (Prof1) (Root)
     *  | 
     * [Maya Leong] (Prof2)
     *  |           
     * [Matthew Hui] (Prof3) (Leaf)
     * </pre>
     */
    private static PhDTree tree3() throws NotFound {
        PhDTree t = new PhDTree(new Professor(prof1));
        t.insert(prof1.name(), new Professor(prof2));
        t.insert(prof2.name(), new Professor(prof3));
        return t;
    }
/**
 * <pre> MyTree structure: 
 * 
 * [Amy Huang](Prof1)(Root)
 *  | ---------------------\--------------\
 * [Maya Leong](Prof2)     [Prof_23]    [Prof_22]
 *  |                       |------\
 * [Matthew Hui](Prof3) [Prof_231] [Prof_232]
 *                         |
 *                     [Prof_2311]
 *                        |
 *                    [Prof_23111]
 * </pre>
 */
    private static PhDTree mytree()  throws NotFound {
        PhDTree t = new PhDTree(new Professor(prof1));
        t.insert(prof1.name(), new Professor(prof2));
        t.insert(prof2.name(), new Professor(prof3));
        
        t.insert(prof1.name(), new Professor(Prof_22));
        //t.insert(prof1.name(), Prof_23); // $$$ Just use the static value to be part of the tree instead of making a copy. $$$
        t.insert(prof1.name(), new Professor(Prof_23));

        ////System.out.println(t.toString());

        t.insert("Prof_23", new Professor(Prof_232));
        t.insert("Prof_23", new Professor(Prof_231));
        t.insert("Prof_231", new Professor("Prof_2311", 2024));
        t.insert("Prof_2311", new Professor(Prof_23111));

        return t;
    }

    @Test
    public void testConstructorProfToString() {
        PhDTree t1 = tree1();
        assertEquals("Amy Huang", t1.toString());
        assertEquals(prof1, t1.prof());

        PhDTree t2 = tree2();
        assertEquals("Arianna Curillo", t2.toString());
        assertEquals(prof4, t2.prof());
    }

    @Test
    public void testNumAdvisees() throws NotFound {
        PhDTree t = tree1();
        assertEquals(0, t.numAdvisees());  //$$$ tree with only root node, advisees == 0

        // TODO: Add three additional tests of `numAdvisees()` using your own tree(s)
        //#################//
        t.insert(prof1.name(), Prof_23); // $$$ attach Prof_23 to Prof1(root), 
        assertEquals(1, t.numAdvisees()); // $$$ advisees == 1

        t.insert(prof1.name(), Prof_231); // $$$ attach Prof_231 to Prof1(root)
        assertEquals(2, t.numAdvisees()); // $$$now advisees == 2

        t = mytree(); // $$$ t pointing to mytree()
        assertEquals(3, t.numAdvisees());
    }

    @Test
    public void testSize() throws NotFound {
        PhDTree t = tree3();
        assertEquals(3, t.size());

        // TODO: Add three additional tests of `size()` using your own tree(s)
        // Create a new Tree
        t = new PhDTree(prof1);
        // Size should be 1
        assertEquals(t.size(), 1);  
        // Insert a new node to the tree, size should be 2 
        t.insert(prof1.name(), prof2);
        assertEquals(t.size(), 2);   

        // mytree 
        t = mytree();
        assertEquals(t.size(), 9);
    }

    @Test
    public void testMaxDepth() throws NotFound {
        PhDTree t = tree3();
        assertEquals(3, t.maxDepth());

        // TODO: Add three additional tests of `maxDepth()` using your own tree(s)
        t = tree1();  // The given tree1() has only one element, therefore depth = 1
        assertEquals(1, t.maxDepth());

        t = mytree(); //
        assertEquals(5, t.maxDepth());
    }

    @Test
    public void testFindTree() throws NotFound {
        PhDTree tree1 = tree1();
        tree1.insert(prof1.name(), prof2);
        tree1.insert(prof2.name(), prof3);
        PhDTree tree4 = new PhDTree(prof2);
        tree4.insert(prof2.name(), prof3);
        assertEquals(tree4.prof(), tree1.findTree(prof2.name()).prof());
        assertEquals("Maya Leong[Matthew Hui]", tree1.findTree(prof2.name()).toString());

        assertThrows(NotFound.class, () -> tree2().findTree(prof5.name()));
        assertThrows(NotFound.class, () -> tree1.findTree(prof4.name()));
        assertEquals(1, tree1.findTree(prof3.name()).size());

        // TODO: Add three additional tests of `findTree()` using your own tree(s)
        assertThrows(NotFound.class, () -> mytree().findTree(prof5.name()));
        PhDTree t1 = new PhDTree(prof1);  // find root 
        assertEquals(t1.prof(), mytree().findTree(prof1.name()).prof());
        PhDTree t2 = new PhDTree(Prof_23); // find middle node
        assertEquals(t2.prof(), mytree().findTree(Prof_23.name()).prof());
        PhDTree tx = new PhDTree(Prof_23111); // find leave node
        assertEquals(tx.prof(), mytree().findTree(Prof_23111.name()).prof());
    }

    @Test
    public void containsTest() throws NotFound {
        PhDTree t = tree3();
        assertTrue(t.contains("Amy Huang"));
        assertFalse(t.contains(prof6.name()));
    }

    @Test
    public void testInsert() throws NotFound {
        PhDTree t = tree1();
        t.insert(prof1.name(), prof2);
        t.insert(prof2.name(), prof3);
        assertEquals("Amy Huang[Maya Leong[Matthew Hui]]", t.toString());

        // TODO: Add three additional tests of `insert()` using your own tree(s)
    }

    @Test
    public void testFindAdvisor() throws NotFound {
        PhDTree t = tree3();
        assertEquals(prof2, t.findAdvisor(prof3.name()));
        assertThrows(NotFound.class, () -> t.findAdvisor(prof1.name()));

        // TODO: Add three additional tests of `findAdvisor()` using your own tree(s)

        PhDTree tx = mytree();
        assertEquals(Prof_23, tx.findAdvisor(Prof_232.name()));  // leaf node
        assertEquals(prof1, tx.findAdvisor(Prof_23.name()));  // middle node
    }

    @Test
    public void testFindAcademiLineage() throws NotFound {
        PhDTree t = tree3();
        List<Professor> lineage1 = new LinkedList<>();
        lineage1.add(prof1);
        lineage1.add(prof2);
        lineage1.add(prof3);
        assertEquals(lineage1, t.findAcademicLineage(prof3.name()));

        // TODO: Add three additional tests of `findAcademicLineage()` using your own tree(s)

    }

    @Test
    public void testCommonAncestor() throws NotFound {
        PhDTree t = tree3();
        assertEquals(prof2, t.commonAncestor(prof2.name(), prof3.name()));
        assertEquals(prof1, t.commonAncestor(prof1.name(), prof3.name()));
        assertThrows(NotFound.class, () -> t.commonAncestor(prof5.name(), prof3.name()));

        // TODO: Add three additional tests of `commonAncestor()` using your own tree(s)
        // Two same nodes should share "self" as common ancestor
        PhDTree t3=tree3();
        assertEquals(prof1, t3.commonAncestor(prof1.name(), prof1.name()));  // Root
        assertEquals(prof3, t3.commonAncestor(prof3.name(), prof3.name()));  // Leaf

        t3 = mytree();
        assertEquals(Prof_23, t3.commonAncestor(Prof_23111.name(), Prof_232.name()));  // middle node
        assertEquals(Prof_23, t3.commonAncestor(Prof_23.name(), Prof_232.name()));  // middle node
    }

    @Test
    public void testPrintProfessors() throws NotFound {
        PhDTree t = tree3();        
        String[] expected = {
                "Amy Huang - 2023",
                "Maya Leong - 2023",
                "Matthew Hui - 2025"
        };
        String[] lines = _PrintTree(t);        
        assertArrayEquals(lines, expected);
        
        // Print the output to console
        for(var str : lines){
            System.out.println(str);
        }

        t = mytree();
        String[] expected2 = {
            "Amy Huang - 2023",
            "Maya Leong - 2023",
            "Matthew Hui - 2025",
            "Prof_23 - 2024",
            "Prof_231 - 2024",
            "Prof_2311 - 2024",
            "Prof_23111 - 2025",
            "Prof_232 - 2024",
            "Prof_22 - 2025"
        };
        lines = _PrintTree(t);        
        assertArrayEquals(lines, expected2);
        
        // Print the output to console
        for(var str : lines){
            System.out.println(str);
        }

/*==== Hide original given code === *
        // Restrict scope to one test case
        // A StringWriter lets us capture output that might normally be written to a file, or
        // printed on the console, in a String instead.
        StringWriter out = new StringWriter();

        // Need to wrap our Writer in a PrintWriter to satisfy `printProfessors()` (but we save
        // the original StringWriter so we can access its string later).  Flush the PrintWriter
        // when we are done with it.
        PrintWriter pw = new PrintWriter(out);
        t.printProfessors(pw);
        pw.flush();

        // Split string into lines for easy comparison ("\\R" is a "regular expression" that
        // matches both Windows and Unix line separators; it only works in methods like
        // `split()`).
        String[] lines = out.toString().split("\\R");
        assertArrayEquals(lines, expected);

        out.flush();
        pw.flush();        
/*==== Hide original given code === */

    }

        // TODO: Add three additional tests of `commonAncestor()` using your own tree(s)
        // Feel free to define a helper method to avoid duplicated testing code.

     /**
      *  helper method to output a tree to String[] *   
      * */
    private String[] _PrintTree(PhDTree tree) {
        // A StringWriter lets us capture output that might normally be written to a file, or
        // printed on the console, in a String instead.
        StringWriter out = new StringWriter();

        // Need to wrap our Writer in a PrintWriter to satisfy `printProfessors()` (but we save
        // the original StringWriter so we can access its string later).  Flush the PrintWriter
        // when we are done with it.
        PrintWriter pw = new PrintWriter(out);
        tree.printProfessors(pw);
        pw.flush();

        // Split string into lines for easy comparison ("\\R" is a "regular expression" that
        // matches both Windows and Unix line separators; it only works in methods like
        // `split()`).
        String[] lines = out.toString().split("\\R");    
        return lines;  
    }
}
