package com.computinglaboratory.tabulator;

import com.computinglaboratory.javaansitextcolorizer.Colorizer;
import com.computinglaboratory.tabulator.exceptions.TooLongWordException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.computinglaboratory.tabulator.General.*;

/**
 * Card class representing the card-view of given class object.
 * It can be used to generate pretty looking strings describing given object.
 * @param <T> Class type
 */
@Builder
@AllArgsConstructor
@Setter
@Getter
public class Card<T> {

    /**
     * Constructor
     */
    public Card() {}

    /**
     * The object to be represented as card
     */
    private T object;

    /**
     * Sets the maximum allowed card column width
     */
    @Builder.Default
    private int maxColumnWidth = 40;

    /**
     * Sets the card header
     */
    String header;

    /**
     * Sets the header color
     */
    Colorizer.Color headerColor;

    /**
     * Allow/disallow multi-line entries
     */
    @Builder.Default
    boolean multiLine = false;

    /**
     * Turn on/off the cards row separators
     */
    @Builder.Default
    boolean rowSeparators = false;

    /**
     * Returns a card as string that represents given object
     * @return card as a string
     * @throws TooLongWordException if the word in card is longer then allowed
     */
    public String getCard() throws TooLongWordException {

        List<List<String>> columns = new ArrayList<>();

        var map = ObjectProcessor.getMapOfMethodNameAndValue(object);

        columns.add(map.keySet().stream().collect(Collectors.toList()));
        columns.add(map.values().stream().collect(Collectors.toList()));

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
            List<String> headerRows = getHeaderRows(this.header, width, headerColor);
            for (String row : headerRows) {
                stringBuilder.append(row);
            }
        }
        stringBuilder.append(getSeparationLine(columnWidths));

        // Create body
        for (var e : map.entrySet()) {
            stringBuilder.append(getLine(List.of(e.getKey(), e.getValue()), columnWidths, maxColumnWidth));
            if (rowSeparators) stringBuilder.append(getSeparationLine(columnWidths));
        }
        if (!rowSeparators) stringBuilder.append(getSeparationLine(columnWidths));


        return stringBuilder.toString();
    }


    /**
     * Card-builder class.
     * @param <T> Class type
     */
    public static class CardBuilder<T> {

        /**
         * Constructor
         */
        public CardBuilder() {}

        /**
         * Returns a card as string that represents given object
         * @return card as a string
         * @throws TooLongWordException if the word in card is longer then allowed
         */
        public String getCard() throws TooLongWordException {
            return build().getCard();
        }
    }

}
