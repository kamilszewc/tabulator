package eu.integrable;

import eu.integrable.exceptions.TooLongWordException;

import java.util.ArrayList;
import java.util.List;

public class General {

    static List<String> getStringRows(String text, int width) throws TooLongWordException {

        // Get maximal width of string in row
        int maxStringLength = width - 3;

        // Get array of words
        String[] words = text.split(" ");

        // Construct rows
        List<String> rows = new ArrayList<>() {{{
            add("");
        }}};

        for (String word : words) {
            if (rows.get(rows.size()-1).length() + word.length() + 2 > maxStringLength) {
                rows.add("");
            }

            // Rise exception if the word in header is too long
            if (word.length() + 2 > maxStringLength) {
                throw new TooLongWordException("Word " + word + " is too long for requested structure");
            }

            rows.set(rows.size()-1, rows.get(rows.size()-1) + " " + word);
        }

        return rows;
    }

    static List<String> getHeaderRows(String header, int width) throws TooLongWordException {

        List<String> rows = getStringRows(header, width);

        List<String> headerRows = new ArrayList<>();
        for (String row : rows) {
            int position = width / 2 - row.length() / 2 - 1;
            String line = "";
            line += "|";
            line += " ".repeat(position);
            line += row;
            line += " ".repeat(width - position - row.length() - 2);
            line += "|\n";
            headerRows.add(line);
        }

        return headerRows;
    }
}
