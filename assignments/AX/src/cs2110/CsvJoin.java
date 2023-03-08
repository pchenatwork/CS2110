package cs2110;
import java.io.*;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingDeque;

public class CsvJoin {

    /**
     * Load a table from a Simplified CSV file and return a row-major list-of-lists representation.
     * The CSV file is assumed to be in the platform's default encoding. Throws an IOException if
     * there is a problem reading the file.
     */

    public static Seq<Seq<String>> csvToList(String fileName) throws IOException {
        File file = new File(fileName);
        assert file.isFile() : "File '" + fileName + "' does not exist.";
        if (!file.isFile()) {
            throw new IOException("File '" + fileName + "' cannot be read.");
        }
        String delimiter = ",";
        Seq<Seq<String>> table = new LinkedSeq<>();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] tokens = line.split(delimiter, -1);
                Seq<String> row = new LinkedSeq<>();
                for (String token : tokens) {
                    row.append(token.trim());
                }
                table.append(row);
            }
        }
        return table;
    }

    /**
     * Return the left outer join of tables `left` and `right`, joined on their first column. Result
     * will represent a rectangular table, with empty strings filling in any columns from `right`
     * when there is no match. Requires that `left` and `right` represent rectangular tables with at
     * least 1 column.
     */
    public static Seq<Seq<String>> join(Seq<Seq<String>> left, Seq<Seq<String>> right) throws IOException {
        assertInputFile(left);
        assertInputFile(right);
        Seq<Seq<String>> newFile = new LinkedSeq<>();
        LinkedSeq<String> rightNewLine = new LinkedSeq<>(); right.get(0);
        for (int i = 0; i < right.get(0).size(); i++) {
            rightNewLine.append("");
        }
        for (Seq<String> leftLine : left) {
            String key = leftLine.get(0);
            boolean keyFound = false;
            for (Seq<String> rightLine : right) {
                if (rightLine.get(0).equals(key)) {
                    keyFound = true;
                    newFile.append(merge(leftLine, rightLine));
                }
            }
            if (!keyFound) {
                newFile.append(merge(leftLine, rightNewLine));
            }
        }
        return newFile;
    }

    private static void assertInputFile(Seq<Seq<String>> file) throws IOException {
        assert file != null && file.size() > 0 : "Input file is null or empty.";
        if (file == null || file.size()==0) {
            throw new IOException("Input table is null or empty");
        }
        int maxColumns = 0;
        int minColumns = Integer.MAX_VALUE;
        for (Seq<String> row : file) {
            if (row.size() < minColumns) {
                minColumns = row.size();
            }
            if (row.size() > maxColumns) {
                maxColumns = row.size();
            }
        }
        assert minColumns == maxColumns : "Input file is not rectangular.";
        if (minColumns != maxColumns)
            throw new IOException("Input table is not rectangular.");
    }


    private static Seq<String> merge(Seq<String> left, Seq<String> right){
        var newLine = new LinkedSeq<String>();
        for (String token : left) {
            newLine.append(token);
        }
        for (int i = 1; i < right.size(); i++) {
            newLine.append(right.get(i));
        }
        return newLine;
    }

    /**
     * Main method that merges two CSV files using a left outer join, and outputs the resulting CSV.
     * Expects exactly 2 program arguments, which are the filenames of the two CSV files to join.
     */
    public static void main(String[] args) throws IOException {
       if (args.length != 2) {
           System.out.println("Usage: cs2110.CsvJoin <left-csv-file> <right-csv-file>");
       //    System.exit(1);
       }
       String leftCsvFile = args[0];
       String rightCsvFile = args[1];
       Seq<Seq<String>> leftTable = csvToList(leftCsvFile);
       // Seq<Seq<String>> rightTable = csvToList(rightCsvFile);
       //  Seq<Seq<String>> joinedTable = join(leftTable, rightTable);
       Seq<Seq<String>> joinedTable = new LinkedSeq<>();
       Seq<String> line = new LinkedSeq<>();
       line.append("11");
       line.append("12");
       line.append("13");
       joinedTable.append(line);
       line = new LinkedSeq<>();
       line.append("21");
       line.append("22");
       line.append("23");
       joinedTable.append(line);

        for (Seq<String> row : joinedTable) {
            System.out.println(String.join(",", row));
        }
    }
}
