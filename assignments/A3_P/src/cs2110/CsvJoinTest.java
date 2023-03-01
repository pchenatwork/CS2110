package cs2110;

//import static org.junit.jupiter.api.Assertions.*;
//import org.junit.jupiter.api.Test;
import org.junit.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.util.Date;

/***
 * Multiple ways of loading file to program:
 * Scanner / BufferedReader / FileReader
 * https://www.geeksforgeeks.org/difference-between-scanner-and-bufferreader-class-in-java/
 * https://www.geeksforgeeks.org/difference-between-bufferedreader-and-filereader-in-java/?ref=rp
 */

public class CsvJoinTest {
    private static String FILE_PATH = "C:/.Repos/pchenatwork/CS2110_/assignments/A3_P/tests/";

    /* <STUDENT> */
    // TODO: Uncomment these tests after implementing the corresponding methods in `CsvJoin`.
    // You must also have implemented `LinkedSeq.toString()` and `LinkedSeq.equals()`.

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
    public void testCsvToList() throws IOException {


        ///Seq<Seq<String>> table = CsvJoin.csvToList("input-tests/example/input2.csv");        
        //Seq<Seq<String>> table = CsvJoin.csvToList("./input2.csv");
        Seq<Seq<String>> table = CsvJoin.csvToList(FILE_PATH + "input2.csv");
        String expectedString = "[[netid, grade], [def456, junior], [ghi789, first-year], [abc123, senior]]";
        
        System.out.println("table =  " + table.toString());

        assertEquals(expectedString, table.toString());

        //table = CsvJoin.csvToList("tests/testCsvToList/non-rectangular.csv");
        table = CsvJoin.csvToList(FILE_PATH + "non-rectangular.csv");
        expectedString = "[[1], [1, 2], [1, 2, 3], [1, , , 4], [1, , 3], [1, , ], [1]]";
        
        System.out.println("Exp =  " + expectedString);
        System.out.println("Act =  " + table.toString());
        assertEquals(expectedString, table.toString());

        //table = CsvJoin.csvToList("tests/testCsvToList/empty.csv");
        table = CsvJoin.csvToList(FILE_PATH + "empty.csv");
        expectedString = "[]";
        assertEquals(expectedString, table.toString());

        //table = CsvJoin.csvToList("tests/testCsvToList/no-cols.csv");
        table = CsvJoin.csvToList(FILE_PATH + "no-cols.csv");
        expectedString = "[[], [], []]";
        assertEquals(expectedString, table.toString());
    }

    /**
        * Assert that joining "input-tests/dir/input1.csv" and "input-tests/dir/input2.csv" yields the
        * table in "input-tests/dir/output.csv".  Requires that tables in "input1.csv" and "input2.csv"
        * be rectangular with at least one column.
        */
    static void testJoinHelper(String dir) throws IOException {
        Seq<Seq<String>> left = CsvJoin.csvToList(FILE_PATH + "input-tests/" + dir + "/input1.csv");
        Seq<Seq<String>> right = CsvJoin.csvToList(FILE_PATH + "input-tests/" + dir + "/input2.csv");
        Seq<Seq<String>> expected = CsvJoin.csvToList(FILE_PATH + "input-tests/" + dir + "/output.csv");
        Seq<Seq<String>> join = CsvJoin.LeftJoin(left, right);
        
        System.out.println("Left  =  " + left.toString());        
        System.out.println("Right =  " + right.toString());    
        System.out.println("Joined    =  " + join.toString()); 
        System.out.println("Expected  =  " + expected.toString());
        assertEquals(expected, join);
    }

    @Test
    public void testJoin() throws IOException {
        testJoinHelper("StudentCourse");
        testJoinHelper("StudentCourse2");

     //   testJoinHelper("states");

        // TODO: Run at least two of your own input-tests here
    }
    /* </STUDENT> */


}