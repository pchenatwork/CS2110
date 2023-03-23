import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import cs2110.*;

public class App {
    public static void main(String[] args) throws Exception {
        /*/
        System.out.println("Hello, World!");

        var prof1 = new Professor("A", 1900);
        var prof2 = new Professor("B", 1900);

        if (prof1.compareTo(prof2) > 0) {
            System.out.println("Prof 1 > Prof 2");
        } else {
            System.out.println("Prof 1 < Prof 2");
        }
         */

        // PhDTree t = tree1();
        // testPrintProfessors(t);
        /*
         * A unicode value = 65
         * B unicode value = 66
         * g == 31
         * AB.hashCode = 65 x31 + 66 = 2081
         *  calculating string unique code
 int hash = 0;
int n = s.length();
for (int i = 0; i < n; i++)
hash = g * hash + s.charAt(i);
         */
        var A = "A".hashCode();
        var A1 = ((Object)"A").hashCode();
        var B = "B".hashCode();
        var AB = "AB".hashCode();

        SetDemo();
    }    
    /**
     * <pre> tree1
     * 
     *      [0] (Root)
     *    /--|-----------\
     *  [1] [2]          [3]
     *   |   |----\----\      
     * [11] [21] [22] [23]
     *            |
     *           [221]
     *            |
     *           [2211]
     * 
     * </pre>
     */
    private static PhDTree tree1() throws NotFound {
        PhDTree t = new PhDTree(Prof_0);
        t.insert(Prof_0.name(), Prof_3);
        t.insert(Prof_0.name(), Prof_2);
        t.insert(Prof_0.name(), Prof_1);
        t.insert(Prof_1.name(), Prof_11);
        t.insert(Prof_2.name(), Prof_23);
        t.insert(Prof_2.name(), Prof_22);
        t.insert(Prof_2.name(), Prof_21);
        t.insert(Prof_22.name(), Prof_221);
        t.insert(Prof_221.name(), Prof_2211);
        return t;
    }
    private static final Professor Prof_0 = new Professor("Prof_0", 2023);    
    private static final Professor Prof_1 = new Professor("Prof_1", 2023);
    private static final Professor Prof_2 = new Professor("Prof_2", 2023);
    private static final Professor Prof_3 = new Professor("Prof_3", 2023);    
    private static final Professor Prof_11 = new Professor("Prof_11", 2023);
    private static final Professor Prof_21 = new Professor("Prof_21", 2024);
    private static final Professor Prof_22 = new Professor("Prof_22", 2024);
    private static final Professor Prof_23 = new Professor("Prof_23", 2024);
    private static final Professor Prof_221 = new Professor("Prof_221", 2025);
    private static final Professor Prof_2211 = new Professor("Prof_2211", 2025);
    
    private static void testPrintProfessors(PhDTree tree) throws NotFound {
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
        for(var str : lines){
            System.out.println(str);
        }
    }
    private static void SetDemo()
    { 
        // Creating an instance of SortedSet
        // String type
        SortedSet<String> ts = new TreeSet<String>();
 
        // Adding elements into the TreeSet
        // using add()
        ts.add("D");
        ts.add("C");
        ts.add("E");
        ts.add("B");
        ts.add("AA");
        ts.add("Aa");
 
        // Adding the duplicate element
        // again simply using add() method
        ts.add("D");
 
        // Print and display TreeSet
        System.out.println(ts);
 
        // Removing items from TreeSet
        // using remove() method
        ts.remove("Ojaswi");
 
        // Display message
        System.out.println("Iterating over set:");
 
        // Iterating over TreeSet items
        Iterator<String> i = ts.iterator();
 
        // Condition holds true till there is single element
        // remaining in the object
        while (i.hasNext()) 
            // Printing elements
            System.out.println(i.next());
    }
}
