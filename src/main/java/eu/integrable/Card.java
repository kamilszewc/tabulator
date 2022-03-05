package eu.integrable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.integrable.exceptions.TooLongWordException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Builder
@AllArgsConstructor
@Setter
@Getter
public class Card {

    private Object object;

    @Builder.Default
    private int maxColumnWidth = 30;

    String header;

    public Card(Object object) {
        this.object = object;
    }

    public String getJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        return objectMapper.writeValueAsString(object);
    }

    public String getCard() throws TooLongWordException {
        var map = ObjectProcessor.getMapOfMethodNameAndValue(object);

        int maxKeyLength = map.keySet().stream()
                .map(key -> key.length())
                .sorted(Comparator.reverseOrder())
                .findFirst()
                .get();

        int maxValueLength = map.values().stream()
                .map(key -> key.length())
                .sorted(Comparator.reverseOrder())
                .findFirst()
                .get();

        int keyColumnWidth = maxKeyLength < maxColumnWidth ? (maxKeyLength + 2) : (maxColumnWidth + 2);
        int valueColumnWidth = maxValueLength < maxColumnWidth ? (maxValueLength + 2) : (maxColumnWidth + 2);
        int width = keyColumnWidth + valueColumnWidth + 3;

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
        header += "+" + "-".repeat(keyColumnWidth) + "+" + "-".repeat(valueColumnWidth) + "+\n";
        String footer = "+" + "-".repeat(keyColumnWidth) + "+" + "-".repeat(valueColumnWidth) + "+\n";

        StringBuilder bodyBuilder = new StringBuilder();
        map.entrySet().stream().forEach(entry -> {

            String key = entry.getKey();
            String value = entry.getValue();

            bodyBuilder.append("| ");
            bodyBuilder.append(key);
            bodyBuilder.append(" ".repeat(keyColumnWidth - 2 - key.length()));
            bodyBuilder.append(" | ");
            bodyBuilder.append(value);
            bodyBuilder.append(" ".repeat(valueColumnWidth - 2 - value.length()));
            bodyBuilder.append(" |\n");
        });
        String body = bodyBuilder.toString();

        return header + body + footer;
    }

    private static List<String> getStringRows(String text, int width) throws TooLongWordException {

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

    private static List<String> getHeaderRows(String header, int width) throws TooLongWordException {

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

    public static class CardBuilder {

        public String getJson() throws JsonProcessingException {
            return build().getJson();
        }

        public String getCard() throws TooLongWordException {
            return build().getCard();
        }
    }

}
