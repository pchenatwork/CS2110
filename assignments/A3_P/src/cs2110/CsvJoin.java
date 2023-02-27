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

    public static Seq<Seq<String>> join(Seq<Seq<String>> left, Seq<Seq<String>> right){
       // Not implemented yet, return left just to be compilable. 
        return left;
    }
}
