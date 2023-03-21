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

    // These helper methods create a copy of each Professor object, which would normally be seen as
    // wasteful.  They do so to help expose bugs involving the use of `==` instead of `.equals()`.
    private static PhDTree tree1() {
        return new PhDTree(new Professor(prof1));
    }

    private static PhDTree tree2() {
        return new PhDTree(new Professor(prof4));
    }

    private static PhDTree tree3() throws NotFound {
        PhDTree t = new PhDTree(new Professor(prof1));
        t.insert(prof1.name(), new Professor(prof2));
        t.insert(prof2.name(), new Professor(prof3));
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
        assertEquals(0, t.numAdvisees());

        // TODO: Add three additional tests of `numAdvisees()` using your own tree(s)

    }

    @Test
    public void testSize() throws NotFound {
        PhDTree t = tree3();
        assertEquals(3, t.size());

        // TODO: Add three additional tests of `size()` using your own tree(s)

    }

    @Test
    public void testMaxDepth() throws NotFound {
        PhDTree t = tree3();
        assertEquals(3, t.maxDepth());

        // TODO: Add three additional tests of `maxDepth()` using your own tree(s)

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

    }

    @Test
    public void testPrintProfessors() throws NotFound {
        {  // Restrict scope to one test case
            PhDTree t = tree3();

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
            String[] expected = {
                    "Amy Huang - 2023",
                    "Maya Leong - 2023",
                    "Matthew Hui - 2025"
            };
            assertArrayEquals(lines, expected);
        }

        // TODO: Add three additional tests of `commonAncestor()` using your own tree(s)
        // Feel free to define a helper method to avoid duplicated testing code.

    }
}
