package com.computinglaboratory;

import com.computinglaboratory.exceptions.TooLongWordException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
                throw new TooLongWordException("Word " + word + " is too long -> consider increasing maxColumnWidth");
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
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("|");
            stringBuilder.append(" ".repeat(position));
            stringBuilder.append(row);
            stringBuilder.append(" ".repeat(width - position - row.length() - 2));
            stringBuilder.append("|\n");
            headerRows.add(stringBuilder.toString());
        }

        return headerRows;
    }

    // Returns line like this: +--------+-------+------+
    static String getSeparationLine(List<Integer> columnWidths) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer width : columnWidths) {
            stringBuilder.append("+");
            stringBuilder.append("-".repeat(width));
        }
        stringBuilder.append("+\n");
        return stringBuilder.toString();
    }

    public static List<Integer> getColumnWidths(List<List<String>> columns, int maxColumnWidth) {
        List<Integer> maxElementWidths = columns.stream()
                .map(column -> {
                    int maxWidth =  column.stream()
                            .map(element -> element.length())
                            .sorted(Comparator.reverseOrder())
                            .findFirst()
                            .get();
                    return maxWidth;
                }).collect(Collectors.toUnmodifiableList());


        // Create columnWidiths
        List<Integer> columnWidths = new ArrayList<>();
        for (int i=0; i<columns.size(); i++) {
            int maxElementWidth = maxElementWidths.get(i);
            int columnWidth = maxElementWidth < maxColumnWidth ? (maxElementWidth + 2) : (maxColumnWidth + 2);
            columnWidths.add(columnWidth);
        }

        return columnWidths;
    }

    public static String getLine(List<String> elements, List<Integer> columnWidths, int maxColumnWidth) throws TooLongWordException {

        List<List> entries = new ArrayList<>();

        int numberOfRows = 1;

        for (int i=0; i<elements.size(); i++) {
            String value = elements.get(i).trim();

            List<String> stringRows = getStringRows(value, maxColumnWidth);

            int finalI = i;
            stringRows = stringRows.stream()
                    .map(row ->  row + " ".repeat(columnWidths.get(finalI) - row.length()))
                    .collect(Collectors.toUnmodifiableList());

            if (numberOfRows < stringRows.size()) numberOfRows = stringRows.size();

            entries.add(stringRows);
        }

        int numberOfColumns = entries.size();

        StringBuilder stringBuilder = new StringBuilder();
        for (int i=0; i<numberOfRows; i++) {
            stringBuilder.append("|");
            for (int j=0; j<numberOfColumns; j++) {
                try {
                    stringBuilder.append(entries.get(j).get(i));
                } catch (IndexOutOfBoundsException ex) {
                    stringBuilder.append(" ".repeat(columnWidths.get(j)));
                }
                if (j < numberOfColumns - 1) stringBuilder.append("|");
            }
            stringBuilder.append("|\n");
        }

        return stringBuilder.toString();
    }
}
