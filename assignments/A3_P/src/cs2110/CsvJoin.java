package cs2110;
import java.io.*;   

public class CsvJoin {

    /**
     * Load a table from a Simplified CSV file and return a row-major list-of-lists representation.
     * The CSV file is assumed to be in the platform's default encoding. Throws an IOException if
     * there is a problem reading the file.
     */
    public static Seq<Seq<String>> csvToList(String fileNameWithPath){
        // https://www.tutorialspoint.com/how-to-read-the-data-from-a-csv-file-in-java#:~:text=We%20can%20read%20a%20CSV,by%20using%20an%20appropriate%20index.
        
        String delimiter = ",";
        Seq<Seq<String>> list = new LinkedSeq<Seq<String>>();
        try {
            File file = new File(fileNameWithPath);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            String[] tokens;

            LinkedSeq<String> row; 
            while((line = br.readLine()) != null) {
                // create a new 'row' using LinkedSeq() data structure 
                row = new LinkedSeq<String>();

                // Split the line into String[] array 
                /* Note If n is zero then the pattern will be applied as many times as possible, the array can have any length, 
                    and trailing empty strings will be discarded. */
                /*## https://stackoverflow.com/questions/24701197/string-split-method-zero-and-negative-limit */
                tokens = line.split(delimiter, -1); 
                /* ## tokens = line.split(delimiter); // === line.split(delimiter, 0) ## */
                for(String token : tokens) {  
                    // ### Enhanced for loop ###
                    /* ## https://www.geeksforgeeks.org/difference-between-for-loop-and-enhanced-for-loop-in-java/ ##  */
                    // append each token to row
                    row.append(token.trim());
                }
                // append 'row' to list
                list.append(row);
            }
            br.close();
        } catch(IOException ioe) {
               ioe.printStackTrace();
        }

        return list;
        
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
        /** Condition check  **/
        assertInputFile(left);
        assertInputFile(right);       
        
        // line = Seq<String>
        // file = Seq<line> = Seq<Seq<String>> 
        Seq<Seq<String>> newFile = new LinkedSeq<Seq<String>>();

        // making 'rightDummyLine' the same size as firstRow of 'right', and a value of "empty string"
        // in case 'right line' is not found, we will use a dummy line to do left join
        LinkedSeq<String> rightDummyLine = new LinkedSeq<>(); right.get(0);
        for (var token: right.get(0)) {
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
        // assert input at least 1 column
        // assert input is rectangular
        assert file != null && file.size() > 0 : "input file is null or empty."; 
        int iMaxColCount = 0;
        int iMinColCount = Integer.MAX_VALUE;
        for (var row : file){  // enhanced for-loop
            if (row.size() < iMinColCount) iMinColCount = row.size();
            else if (row.size() > iMaxColCount) iMaxColCount = row.size();
        }
        assert iMinColCount == iMaxColCount : "input file is not rectangular.";
        assert iMinColCount > 0 : "input file has empty row";
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
}