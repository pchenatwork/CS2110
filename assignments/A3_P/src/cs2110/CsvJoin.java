package cs2110;
import java.io.*;
import java.nio.file.Paths;
import java.util.Scanner;   

public class CsvJoin {
    /**
     * FILE_PATH defines the absolute folder location where program will locate CSV files.
     * Default to [package's root]/tests.
     */
    //https://stackoverflow.com/questions/4871051/how-to-get-the-current-working-directory-in-java
    public static String FILE_PATH =  Paths.get("").toAbsolutePath().toString() + "\\tests\\";    

    /**
     * Load a table from a Simplified CSV file and return a row-major list-of-lists representation.
     * The CSV file is assumed to be in the platform's default encoding. Throws an IOException if
     * there is a problem reading the file.
     * ** Note: FILE_PATH is initiated to [workspace's root]/tests/
     */
    public static Seq<Seq<String>> csvToList(String fileName) throws IOException{  
        //  ### Note : throw IOException because  testCsvToList() throws IOException  ###

        // https://www.tutorialspoint.com/how-to-read-the-data-from-a-csv-file-in-java#:~:text=We%20can%20read%20a%20CSV,by%20using%20an%20appropriate%20index.
        
        File file = new File(FILE_PATH + fileName);

        // Step 1: make sure input file is readable/accessable by program
        assert file.isFile() : "File not exists '" + fileName + "'. Make sure absolute path 'FILE_PATH' is correct.";
        if (!file.isFile())
            throw new IOException("File '" + fileName + "' can not be read. Make sure absolute path 'FILE_PATH' is correct.");

        String delimiter = ",";
        Seq<Seq<String>> tbl = new LinkedSeq<Seq<String>>();
        // use "java.util.Scanner" to read the file stream
        Scanner  scanner = new Scanner(file);
        while(scanner.hasNextLine()) {
            var line = scanner.nextLine();

            // Split(string, n): Split the line into String[] array (tokens)
            /* Note If n is zero then the pattern will be applied as many times as possible, the array can have any length, 
                and trailing empty strings will be discarded. */
            /*## https://stackoverflow.com/questions/24701197/string-split-method-zero-and-negative-limit */
            var tokens = line.split(delimiter, -1); 
            
            var row = new LinkedSeq<String>();
            for(String token : tokens) {  
                // ### Enhanced for loop ###
                /* ## https://www.geeksforgeeks.org/difference-between-for-loop-and-enhanced-for-loop-in-java/ ##  */
                // append each token to row, 
                row.append(token.trim());
            }
            // append 'row' to tbl
            tbl.append(row);
        }

        // Close Scanner to clean up memory
        scanner.close();      
        return tbl;
    }
    /**
     * Return the left outer join of tables `left` and `right`, joined on their first column. Result
     * will represent a rectangular table, with empty strings filling in any columns from `right`
     * when there is no match. Requires that `left` and `right` represent rectangular tables with at
     * least 1 column.
     */
    public static Seq<Seq<String>> join(Seq<Seq<String>> left, Seq<Seq<String>> right) throws IOException {
        //  ### Note : throw IOException because  testJoin() throws IOException  ###

        /** defensive programming practices  **
         * assert that preconditions are satisfied *
         * Condition check  **/
        assertInputFile(left);
        assertInputFile(right);       
        
        // line = Seq<String>
        // file = Seq<line> = Seq<Seq<String>> 
        Seq<Seq<String>> newFile = new LinkedSeq<Seq<String>>();

        // making 'rightDummyLine' the same size as firstRow of 'right', with a value of "empty string"
        // in case 'right line' is not found, we will use a dummy line to do left join
        LinkedSeq<String> rightDummyLine = new LinkedSeq<>(); 
        
        //for (int i = 0; i<right.get(0).size(); i++) {  // using int to loop through all elements
        for (var token: right.get(0)) {   // using enhanced for-loop to loop through all elements
            //using the 'first line (index=0)' in 'right' file as reference to populate a 'DummyLine'
            rightDummyLine.append("");  
        }

        for(var leftLine : left) {
            String key = leftLine.get(0); // Get line[0] as key (to matching value with right );
            boolean bKeyFound = false ; // Assume key not found in 'right'
            for (var rightLine : right){
                if (rightLine.get(0).equals(key)){
                    // key found in rightLine
                    bKeyFound = true;   
                    // use helper mergeLine() to merge 'leftLine' and 'rightLine'
                    // and append to 'file'
                    newFile.append(mergeLine(leftLine, rightLine));
                }
            }
            if (!bKeyFound){
                // if key not found in 'right', merge to a dummy rightLine                
                newFile.append(mergeLine(leftLine, rightDummyLine));
            }
        }
        return newFile;
    }

    /**
     * Helper function to make sure input 'file' is valid
     * @param file in Seq<Seq<String>> form
     * @throws IOException if 'file' is invalid
     */
    private static void assertInputFile(Seq<Seq<String>> file) throws IOException  {

        /* ### Note: throw IOException because testJoinHelper() throws IOException */

        // assert input at least 1 row
        // assert input is rectangular
        assert file != null && file.size() > 0 : "Input table is null or empty."; 
        if (file==null || file.size()==0)
            throw new IOException("Input table is null or empty");

        int iMaxColCount = 0;
        int iMinColCount = Integer.MAX_VALUE;
        for (var row : file){  // enhanced for-loop
            // get min/max Column Count from every 'row' in the 'file'. 
            // file is rectangular only when iMinColCount== iMaxColCount
            if (row.size() < iMinColCount) iMinColCount = row.size();
            if (row.size() > iMaxColCount) iMaxColCount = row.size();
        }
        //assert iMinColCount > 0 : "input file has empty row";
        assert iMinColCount == iMaxColCount : "Input file is not rectangular.";
        if ( iMinColCount != iMaxColCount)
            throw new IOException("Input table is not rectangular.");
    }
    /* **
     * Helper function to merger two LinkedSeq<>(left, right), skip rightLine[0] as it is matching key to leftLine[0]
     */
    private static Seq<String> mergeLine(Seq<String> leftLine, Seq<String> rightLine){
        var newLine = new LinkedSeq<String>();
        // Copy every token from leftLine to newLine
        for(var token : leftLine){
            newLine.append(token);
        }
        // copy tokens from rightLine to newLine. index start from 1, skip [0] as it is assumed key
        for (int i = 1; i< rightLine.size(); i++){
            newLine.append(rightLine.get(i));
        }
        return newLine;
    }
    
    /**
     * Helper function to convert "Seq< String >" to ', ' seperated string
     */
    public static String toCSV(Seq<String> line){
        String delimiter = ", ";
        // Use StringBuilder() (mutable object) to do string manipulation
        // String is immutable
        StringBuilder sb = new StringBuilder();
        for (var token: line){
            sb.append(token).append(delimiter);
        }
        // remove trailing 'delimiter'
        sb.delete(sb.length() - delimiter.length(), sb.length());
        // append line break
        // sb.append("\n");
        return sb.toString();
    }
    public static void main (String[] args){
        System.out.println("=============================================================================" ); 
        System.out.println("* cs2110.CsvJoin expected two arguments: <left_table.csv> <right_table.csv> " ); 
        System.out.println("* 'FILE_PATH' = \"" + FILE_PATH +"\""); 
        System.out.println("=============================================================================" );

        if (args.length==2) {
            // If arguments <left_table.csv> <right_table.csv> are given
            try {
                Seq<Seq<String>> left = csvToList(args[0]);
                Seq<Seq<String>> right = csvToList(args[1]);
                var joinedFile = join(left, right);
                System.out.println("Left join result is : "); 
                for(var line : joinedFile){
                    System.out.println(toCSV(line));
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } else {
            // If arguments <left_table.csv> <right_table.csv> are not given
            // we will prompt user for <left_table.csv> <right_table.csv>
            System.out.println("* Enter 'exit' to exit program *"); 
            var sc = new Scanner(System.in);        
            while (true) { 
                Seq<Seq<String>> left = readFileFromCommandHelper(sc, "Please enter <left_table.csv>: ");
                if (left==null) break;    
                Seq<Seq<String>> right = readFileFromCommandHelper(sc, "Please enter <right_table.csv>: ");
                if (right==null) break;  
                try {      
                    var joinedFile = join(left, right);
                    System.out.println("Left join result is : "); 
                    for(var line : joinedFile){
                        System.out.println(toCSV(line));
                    }
                }
                catch (Exception e) {
                    // Show error message
                    System.err.println(e.getMessage());
                }
            }    
            // Scanner needs to be closed to free up memory  
            sc.close();
        }
        return;     
    }
    /**
     * Helper function for main() to read input file from command prompt. 
     * 'exit' will return 'null', which will signal main() to exit program
     * @param sc Scanner to read command line input
     * @param prompt Command Prompt Message
     * @return {@code Seq<Seq<String>>}}
     */
    private static Seq<Seq<String>> readFileFromCommandHelper(Scanner sc, String prompt) {
        // command prompt message
        System.out.print(prompt);  
        // read user command line input
        String input = sc.nextLine().trim();  
        // if user input is 'exit' exit program with a 'null' file
        if (input.split(" ")[0].toLowerCase().equals("exit")) return null;
        
        Seq<Seq<String>> file = null;
        try{
            file = csvToList(input);
        }
        catch (Exception e) {
            // Show error message
            System.err.println(e.getMessage());
            // self-calling readFileFromCommandHelper() to re-do user input
            file = readFileFromCommandHelper(sc, prompt);
        }
        return file;
    }
}
