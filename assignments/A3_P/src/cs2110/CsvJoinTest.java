package cs2110;

//import static org.junit.jupiter.api.Assertions.*;
//import org.junit.jupiter.api.Test;
import org.junit.*;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

/***
 * Multiple ways of loading file to program:
 * Scanner / BufferedReader / FileReader
 * https://www.geeksforgeeks.org/difference-between-scanner-and-bufferreader-class-in-java/
 * https://www.geeksforgeeks.org/difference-between-bufferedreader-and-filereader-in-java/?ref=rp
 */

public class CsvJoinTest {

    /* <STUDENT> */
    // TODO: Uncomment these tests after implementing the corresponding methods in `CsvJoin`.
    // You must also have implemented `LinkedSeq.toString()` and `LinkedSeq.equals()`.

    @Test
    public void testCsvToList() throws IOException {

        ///Seq<Seq<String>> table = CsvJoin.csvToList("input-tests/example/input2.csv");        
        //Seq<Seq<String>> table = CsvJoin.csvToList("./input2.csv");
        Seq<Seq<String>> table = CsvJoin.csvToList("C:\\.Repos\\pchenatwork\\CS2110_\\assignments\\A3_X\\tests\\input2.csv");
        String expectedString = "[[netid, grade], [def456, junior], [ghi789, first-year], [abc123, senior]]";
        
        System.out.println("table =  " + table.toString());

        assertEquals(expectedString, table.toString());

        //table = CsvJoin.csvToList("tests/testCsvToList/non-rectangular.csv");
        table = CsvJoin.csvToList("C:\\.Repos\\pchenatwork\\CS2110_\\assignments\\A3_P\\tests\\non-rectangular.csv");
        expectedString = "[[1], [1, 2], [1, 2, 3], [1, , , 4], [1, , 3], [1, , ], [1]]";
        
        System.out.println("Exp =  " + expectedString);
        System.out.println("Act =  " + table.toString());
        assertEquals(expectedString, table.toString());

        //table = CsvJoin.csvToList("tests/testCsvToList/empty.csv");
        table = CsvJoin.csvToList("C:\\.Repos\\pchenatwork\\CS2110_\\assignments\\A3_P\\tests\\empty.csv");
        expectedString = "[]";
        assertEquals(expectedString, table.toString());

        //table = CsvJoin.csvToList("tests/testCsvToList/no-cols.csv");
        table = CsvJoin.csvToList("C:\\.Repos\\pchenatwork\\CS2110_\\assignments\\A3_P\\tests\\no-cols.csv");
        expectedString = "[[], [], []]";
        assertEquals(expectedString, table.toString());
    }
//
//    /**
//     * Assert that joining "input-tests/dir/input1.csv" and "input-tests/dir/input2.csv" yields the
//     * table in "input-tests/dir/output.csv".  Requires that tables in "input1.csv" and "input2.csv"
//     * be rectangular with at least one column.
//     */
//    static void testJoinHelper(String dir) throws IOException {
//        Seq<Seq<String>> left = CsvJoin.csvToList("input-tests/" + dir + "/input1.csv");
//        Seq<Seq<String>> right = CsvJoin.csvToList("input-tests/" + dir + "/input2.csv");
//        Seq<Seq<String>> expected = CsvJoin.csvToList("input-tests/" + dir + "/output.csv");
//        Seq<Seq<String>> join = CsvJoin.join(left, right);
//        assertEquals(expected, join);
//    }
//
//    @Test
//    void testJoin() throws IOException {
//        testJoinHelper("example");
//
//        testJoinHelper("states");
//
//        // TODO: Run at least two of your own input-tests here
//    }
    /* </STUDENT> */


}