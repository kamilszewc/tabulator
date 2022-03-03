package eu.integrable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;


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

    public String getTable() {
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
            int position = width / 2 - this.header.length() / 2 - 1;
            header += "+" + "-".repeat(width-2) + "+\n";
            header += "|";
            header += " ".repeat(position);
            header += this.header;
            header += " ".repeat(width - position - this.header.length() - 2);
            header += "|\n";
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

    public static class CardBuilder {

        public String getJson() throws JsonProcessingException {
            return build().getJson();
        }

        public String getCard() {
            return build().getTable();
        }
    }

}
