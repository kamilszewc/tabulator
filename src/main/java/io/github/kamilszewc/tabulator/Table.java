package io.github.kamilszewc.tabulator;

import io.github.kamilszewc.tabulator.exceptions.TooLongWordException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.github.kamilszewc.tabulator.General.*;

/**
 * Table class representing the table-view of given list of objects.
 * It can be used to generate pretty looking strings describing given list of objects.
 * @param <T> Class type
 */
@Builder
@AllArgsConstructor
@Setter
@Getter
public class Table<T> {

    /**
     * Constructor
     */
    public Table() {}

    /**
     * The list of objects to be represented as table
     */
    private List<T> object;

    /**
     * Sets the maximum allowed table column width
     */
    @Builder.Default
    private int maxColumnWidth = 80;

    /**
     * Sets the table header
     */
    String header;

    /**
     * Allows to select columns to be presented in table
     */
    @Builder.Default
    List<String> selectedColumns = null;

    /**
     * Allow/disallow multi-line entries
     */
    @Builder.Default
    boolean multiLine = false;

    /**
     * Turn on/off the tables row separators
     */
    @Builder.Default
    boolean rowSeparators = true;

    /**
     * Returns a table as string that represents given list of objects
     * @return table as a string
     * @throws TooLongWordException if the word in card is longer then allowed
     */
    public String getTable() throws TooLongWordException {

        List<List<String>> columns = new ArrayList<>();

        if (selectedColumns == null) {
            selectedColumns = ObjectProcessor.getListOfKeys(this.object.get(0));
        }

        var keys = ObjectProcessor.getMapOfMethodNameAndValue(this.object.get(0), selectedColumns)
                .entrySet().stream()
                .map(Map.Entry::getKey)
                .toList();

        keys.stream().forEach(element -> columns.add(new ArrayList<>(){{ add(element); }}));

        for (Object object : this.object) {
            var values = ObjectProcessor.getMapOfMethodNameAndValue(object, selectedColumns)
                    .entrySet().stream()
                    .map(Map.Entry::getValue)
                    .toList();

            for (int i=0; i<values.size(); i++) {
                columns.get(i).add(values.get(i));
            }
        }

        // Get column Widths
        List<Integer> columnWidths = getColumnWidths(columns, maxColumnWidth);

        // Calculate the total width
        int width = columnWidths.stream().reduce(0, Integer::sum) + columns.size() + 1;

        // Create header
        StringBuilder stringBuilder = new StringBuilder();
        if (this.header != null) {
            // The top separation line
            stringBuilder.append("+" + "-".repeat(width-2) + "+\n");

            // Actual header
            List<String> headerRows = getHeaderRows(this.header, width);
            for (String row : headerRows) {
                stringBuilder.append(row);
            }
        }
        stringBuilder.append(getSeparationLine(columnWidths));

        // Create body
        // Keys
        stringBuilder.append(getLine(keys, columnWidths, maxColumnWidth));
        stringBuilder.append(getSeparationLine(columnWidths));
        // Values
        for (T object : this.object) {
            var values = ObjectProcessor.getMapOfMethodNameAndValue(object, selectedColumns)
                    .entrySet().stream()
                    .map(Map.Entry::getValue)
                    .toList();

            stringBuilder.append(getLine(values, columnWidths, maxColumnWidth));
            if (rowSeparators) stringBuilder.append(getSeparationLine(columnWidths));
        }
        if (!rowSeparators) stringBuilder.append(getSeparationLine(columnWidths));

        return stringBuilder.toString();
    }


    /**
     * Table-builder class.
     * @param <T> Class type
     */
    public static class TableBuilder<T> {

        /**
         * Constructor
         */
        public TableBuilder() {}

        /**
         * Returns a table as string that represents given list of objects
         * @return table as a string
         * @throws TooLongWordException if the word in card is longer then allowed
         */
        public String getTable() throws TooLongWordException {
            return build().getTable();
        }
    }

}
