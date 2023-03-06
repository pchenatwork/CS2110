package cs2110;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;   

public class CsvJoin {

    //private static String FILE_PATH = "X:/Repos/pchenatwork/CS2110/assignments/A3_P/tests/input-tests/Left-Multi-Right-Multi-Test/";  
    //private static String FILE_PATH = "X:/Repos/pchenatwork/CS2110/assignments/A3_P/tests/"; 
    
    // get FILE_PATH == current path
    public static final String FILE_PATH =  Paths.get("").toAbsolutePath().toString();
    //https://stackoverflow.com/questions/4871051/how-to-get-the-current-working-directory-in-java

    /**
     * Load a table from a Simplified CSV file and return a row-major list-of-lists representation.
     * The CSV file is assumed to be in the platform's default encoding. Throws an IOException if
     * there is a problem reading the file.
     */
    public static Seq<Seq<String>> csvToList(String fileNameWithFullPath) throws FileNotFoundException{
        // https://www.tutorialspoint.com/how-to-read-the-data-from-a-csv-file-in-java#:~:text=We%20can%20read%20a%20CSV,by%20using%20an%20appropriate%20index.
        

        String delimiter = ",";
        Seq<Seq<String>> tbl = new LinkedSeq<Seq<String>>();
        //Scanner scanner = null;
      //  try {
            File file1 = new File(FILE_PATH, fileNameWithFullPath);
            File file = new File(fileNameWithFullPath);
            // make sure input file is readable/accessable by program
            assert file.isFile() : "File not exists '" + fileNameWithFullPath + "'. Make sure 'fileNameWithFullPath' is absolute full path";

            // use "java.util.Scanner" to read the file stream
        Scanner  scanner = new Scanner(file);

            //FileReader fr = new FileReader(file);
            //BufferedReader br = new BufferedReader(fr);
            while(scanner.hasNextLine()) {
            ///while((line = br.readLine()) != null) {
                // create a new 'row' using LinkedSeq() data structure 
                var line = scanner.nextLine();
                var row = new LinkedSeq<String>();

                // Split(string, n): Split the line into String[] array 
                /* Note If n is zero then the pattern will be applied as many times as possible, the array can have any length, 
                    and trailing empty strings will be discarded. */
                /*## https://stackoverflow.com/questions/24701197/string-split-method-zero-and-negative-limit */
                var tokens = line.split(delimiter, -1); 
                tokens = line.split(delimiter); 
                /* ## tokens = line.split(delimiter); // === line.split(delimiter, 0) ## */
                for(String token : tokens) {  
                    // ### Enhanced for loop ###
                    /* ## https://www.geeksforgeeks.org/difference-between-for-loop-and-enhanced-for-loop-in-java/ ##  */
                    // append each token to row
                    row.append(token.trim());
                }
                // append 'row' to tbl
                tbl.append(row);
            }
            ///br.close();
        //} catch(IOException ioe) {
        //       ioe.printStackTrace();
        //}
        //finally {
            if(scanner!=null)
                   scanner.close();
       // }

        return tbl;
        
        /* ## 
        var line1 = new LinkedSeq<String>();
        var line2 = new LinkedSeq<String>();
        Seq<Seq<String>> file = new LinkedSeq<Seq<String>>();
        file.append(line1);
        file.append(line2);
        return file ;
        ## */
    }
    /**
     * Return the left outer join of tables `left` and `right`, joined on their first column. Result
     * will represent a rectangular table, with empty strings filling in any columns from `right`
     * when there is no match. Requires that `left` and `right` represent rectangular tables with at
     * least 1 column.
     */
    public static Seq<Seq<String>> LeftJoin(Seq<Seq<String>> left, Seq<Seq<String>> right){
        /** defensive programming practices  **
         * assert that preconditions are satisfied *
         * Condition check  **/
        assertInputFile(left);
        assertInputFile(right);       
        
        // line = Seq<String>
        // file = Seq<line> = Seq<Seq<String>> 
        Seq<Seq<String>> newFile = new LinkedSeq<Seq<String>>();

        // making 'rightDummyLine' the same size as firstRow of 'right', and a value of "empty string"
        // in case 'right line' is not found, we will use a dummy line to do left join
        LinkedSeq<String> rightDummyLine = new LinkedSeq<>(); 
        // right.get(0);
        for (int i = 0; i<right.get(0).size(); i++) {  // using int to loop through all elements
        //for (var token: right.get(0)) {   // using enhanced for-loop to loop through all elements
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

    private static void assertInputFile(Seq<Seq<String>> file) 
    {
        // assert input at least 1 row
        // assert input is rectangular
        assert file != null && file.size() > 0 : "input file is null or empty."; 

        int iMaxColCount = 0;
        int iMinColCount = Integer.MAX_VALUE;
        for (var row : file){  // enhanced for-loop
            // Assert 'row' has at least one element AND the element is not empty
            //?if (row.size()==1) {
            //?    var firstElement =  row.get(0);
            //?    assert firstElement != null && firstElement.length()>0 : "input file has empty row";
            //?}
            // get min/max Column Count from every 'row'
            if (row.size() < iMinColCount) iMinColCount = row.size();
            if (row.size() > iMaxColCount) iMaxColCount = row.size();
        }
        //assert iMinColCount > 0 : "input file has empty row";
        assert iMinColCount == iMaxColCount : "input file is not rectangular.";
    }
    /* **
     * Helper function to merger two LinkedSeq<>(left, right), skip right[0]
     */
    private static Seq<String> mergeLine(Seq<String> left, Seq<String> right){
        var newLine = new LinkedSeq<String>();
        for(var token : left){
            newLine.append(token);
        }
        // index start from 1, skip [0] as it is assume key
        for (int i = 1; i< right.size(); i++){
            newLine.append(right.get(i));
        }
        return newLine;
    }
    
    /**
     * output "Seq<String>" to ', ' seperated string
     */
    public static String toCSV(Seq<String> line){
        String delimiter = ", ";
        StringBuilder sb = new StringBuilder();
        for (var token: line){
            sb.append(token);
            sb.append(delimiter);
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
        System.out.println("* Folder location: " + FILE_PATH  ); 
        System.out.println("* Make change to 'CvsJoin->FILE_PATH' to  " ); 
        System.out.println("* Type 'exit' to exit the program"); 
        System.out.println("============================================================================ " ); 

        Path currentRelativePath = Paths.get("");
        String s1 = currentRelativePath.toAbsolutePath().toString();
        System.out.println(s1); 
        String s2 = Paths.get("").toAbsolutePath().toString();
        System.out.println(s2); 
        String s3= FileSystems.getDefault().getPath("").toAbsolutePath().toString();
        System.out.println(s3); 



        var sc = new Scanner(System.in);        
        while (true) {          
            Seq<Seq<String>> left = readFile(sc, "Please enter <left_table.csv>: ");
            if (left==null) break;    
            Seq<Seq<String>> right = readFile(sc, "Please enter <right_table.csv>: ");
            if (right==null) break;
            var joinedFile = LeftJoin(left, right);
            for(var line : joinedFile){
                System.out.println(toCSV(line));
            }
        }      
        sc.close();
        return;     
    }
    /**
     * Helper function to read input file from command prompt 
     * @param sc Scanner
     * @param prompt Command Prompt Message
     * @return Seq<Seq<String>>
     */
    private static Seq<Seq<String>> readFile(Scanner sc, String prompt) {
        Seq<Seq<String>> file = null;
        System.out.print(prompt);
        String input = sc.nextLine().trim();
        if (input.split(" ")[0].toLowerCase().equals("exit")) return file;
        try{
            file = csvToList(FILE_PATH + input);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            file = readFile(sc, prompt);
        }
        return file;
    }
}
