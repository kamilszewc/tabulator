package io.github.kamilszewc.tabulator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.kamilszewc.tabulator.exceptions.NotImplementedException;
import io.github.kamilszewc.tabulator.exceptions.TooLongWordException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static io.github.kamilszewc.tabulator.General.*;


@Builder
@AllArgsConstructor
@Setter
@Getter
public class Table<T> {

    private List<T> object;

    @Builder.Default
    private int maxColumnWidth = 30;

    String header;

    @Builder.Default
    List<String> selectedColumns = null;

    @Builder.Default
    boolean multiLine = false;

    @Builder.Default
    boolean rowSeparators = true;

    public Table(List<T> object) {
        this.object = object;
    }

    public String getJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        return objectMapper.writeValueAsString(object);
    }

    public String getTable() throws TooLongWordException {

        List<List<String>> columns = new ArrayList<>();

        if (selectedColumns == null) {
            selectedColumns = ObjectProcessor.getListOfKeys(this.object.get(0));
        }

        var keys = ObjectProcessor.getMapOfMethodNameAndValue(this.object.get(0), selectedColumns)
                .entrySet().stream()
                .map(entry -> entry.getKey())
                .toList();

        keys.stream().forEach(element -> {
            columns.add(new ArrayList<>(){{ add(element); }});
        });

        for (Object object : this.object) {
            var values = ObjectProcessor.getMapOfMethodNameAndValue(object, selectedColumns)
                    .entrySet().stream()
                    .map(entry -> entry.getValue())
                    .toList();

            for (int i=0; i<values.size(); i++) {
                columns.get(i).add(values.get(i));
            }
        }

        // Get column Widths
        List<Integer> columnWidths = getColumnWidths(columns, maxColumnWidth);

        // Calculate the total width
        int width = columnWidths.stream().reduce(0, (a, b) -> a + b) + columns.size() + 1;

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
                    .map(entry -> entry.getValue())
                    .toList();

            stringBuilder.append(getLine(values, columnWidths, maxColumnWidth));
            if (rowSeparators) stringBuilder.append(getSeparationLine(columnWidths));
        }
        if (!rowSeparators) stringBuilder.append(getSeparationLine(columnWidths));

        return stringBuilder.toString();
    }


    public static class TableBuilder<T> {

        public String getJson() throws JsonProcessingException {
            return build().getJson();
        }

        public String getTable() throws TooLongWordException, NotImplementedException {
            return build().getTable();
        }
    }

}
