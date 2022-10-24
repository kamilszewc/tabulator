package io.github.kamilszewc.tabulator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class Card<T> {

    private T object;

    @Builder.Default
    private int maxColumnWidth = 30;

    String header;

    @Builder.Default
    boolean multiLine = false;

    @Builder.Default
    boolean rowSeparators = false;

    public Card(T object) {
        this.object = object;
    }

    public String getJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        return objectMapper.writeValueAsString(object);
    }

    public String getCard() throws TooLongWordException {

        List<List<String>> columns = new ArrayList<>();

        var map = ObjectProcessor.getMapOfMethodNameAndValue(object);

        columns.add(map.keySet().stream().toList());
        columns.add(map.values().stream().toList());

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
        for (var e : map.entrySet()) {
            stringBuilder.append(getLine(List.of(e.getKey(), e.getValue()), columnWidths, maxColumnWidth));
            if (rowSeparators) stringBuilder.append(getSeparationLine(columnWidths));
        };
        if (!rowSeparators) stringBuilder.append(getSeparationLine(columnWidths));


        return stringBuilder.toString();
    }


    public static class CardBuilder<T> {

        public String getJson() throws JsonProcessingException {
            return build().getJson();
        }

        public String getCard() throws TooLongWordException {
            return build().getCard();
        }
    }

}
