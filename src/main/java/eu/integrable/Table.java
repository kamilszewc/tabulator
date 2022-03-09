package eu.integrable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.integrable.exceptions.NotImplementedException;
import eu.integrable.exceptions.TooLongWordException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static eu.integrable.General.*;


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

    public String getTable() throws TooLongWordException, NotImplementedException {

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

        List<Integer> maxElementWidths = columns.stream()
                .map(column -> {
                    int maxWidth =  column.stream()
                            .map(element -> element.length())
                            .sorted(Comparator.reverseOrder())
                            .findFirst()
                            .get();
                    return maxWidth;
                }).collect(Collectors.toUnmodifiableList());

        List<Integer> columnWidths = new ArrayList<>();
        for (int i=0; i<columns.size(); i++) {
            int maxElementWidth = maxElementWidths.get(i);
            int columnWidth = maxElementWidth < maxColumnWidth ? (maxElementWidth + 2) : (maxColumnWidth + 2);
            columnWidths.add(columnWidth);
        }

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
        stringBuilder.append(getLine(keys, columnWidths));
        stringBuilder.append(getSeparationLine(columnWidths));
        // Values
        for (T object : this.object) {
            var values = ObjectProcessor.getMapOfMethodNameAndValue(object, selectedColumns)
                    .entrySet().stream()
                    //.filter(entry -> selectedColumns.contains(entry.getKey()))
                    .map(entry -> entry.getValue())
                    .toList();

            stringBuilder.append(getLine(values, columnWidths));
            if (rowSeparators) stringBuilder.append(getSeparationLine(columnWidths));
        }
        if (!rowSeparators) stringBuilder.append(getSeparationLine(columnWidths));

        return stringBuilder.toString();
    }

    private String getLine(List<String> elements, List<Integer> columnWidths) throws NotImplementedException, TooLongWordException {

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


    public static class TableBuilder<T> {

        public String getJson() throws JsonProcessingException {
            return build().getJson();
        }

        public String getTable() throws TooLongWordException, NotImplementedException {
            return build().getTable();
        }
    }

}
