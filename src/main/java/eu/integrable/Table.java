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
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static eu.integrable.General.getHeaderRows;


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
    boolean multiLine = false;

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

        // Get names of keys from the first object
        var keys = ObjectProcessor.getListOfKeys(this.object.get(0));
        keys.stream().forEach(element -> {
            columns.add(new ArrayList<>(){{ add(element); }});
        });

        for (Object object : this.object) {
            var values = ObjectProcessor.getListOfValues(object);

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
        String header = "";
        if (this.header != null) {
            // The top separation line
            header += "+" + "-".repeat(width-2) + "+\n";

            // Actual header
            List<String> headerRows = getHeaderRows(this.header, width);
            for (String row : headerRows) {
                header += row;
            }
        }
        header += getSeparationLine(columnWidths);

        String body = getLine(keys, columnWidths);
        body += getSeparationLine(columnWidths);
        for (T object : this.object) {
            var values = ObjectProcessor.getListOfValues(object);
            body += getLine(values, columnWidths);
            body += getSeparationLine(columnWidths);
        }

        return header + body;
    }

    private String getLine(List<String> elements, List<Integer> columnWidths) throws NotImplementedException, TooLongWordException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("| ");
        for (int i=0; i<elements.size(); i++) {
            String value = elements.get(i).trim();
            stringBuilder.append(value);
            int numberOfEmpties = columnWidths.get(i) - value.length() - 2;

            // if number of empties is negative -> we need to no fit in max width
            if (numberOfEmpties < 0) {
                if (multiLine == true) {
                    throw new NotImplementedException("Multi line is not implemented yet");
                } else {
                    throw new TooLongWordException("Word " + value + " is too long for table entry -> use multiLine=true");
                }
            }

            stringBuilder.append(" ".repeat(numberOfEmpties));
            if (i < elements.size()-1) stringBuilder.append(" | ");
        }
        stringBuilder.append(" |\n");
        return stringBuilder.toString();
    }

    private static String getSeparationLine(List<Integer> columnWidths) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer width : columnWidths) {
            stringBuilder.append("+");
            stringBuilder.append("-".repeat(width));
        }
        stringBuilder.append("+\n");
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
