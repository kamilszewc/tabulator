package eu.integrable;

import eu.integrable.exceptions.TooLongWordException;
import lombok.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

public class CardTest {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Basic {
        private Long first;
        private String second;
        @Builder.Default
        private LocalDateTime time = LocalDateTime.of(2022, Month.AUGUST, 10, 11, 12, 12);
        private String thirdOption;
        private String fourthOption;
    }

    @Test
    public void convertingBasicClassToCard() throws TooLongWordException {

        Basic basic = Basic.builder()
                .first(1L)
                .second("Second entry")
                .thirdOption("Third Option")
                .build();

        String card = Card.builder()
                .object(basic)
                .header("Basic class")
                .getCard();

        String expected = """
                +------------------------------------+
                |             Basic class            |
                +--------------+---------------------+
                | first        | 1                   |
                | fourthOption |                     |
                | second       | Second entry        |
                | thirdOption  | Third Option        |
                | time         | 2022-08-10T11:12:12 |
                +--------------+---------------------+
                """;

        Assertions.assertEquals(expected, card);
    }

    @Test
    public void convertingBasicClassToCardWithTooLongHeader() throws TooLongWordException {

        Basic basic = Basic.builder()
                .first(1L)
                .second("Second entry")
                .thirdOption("Third Option")
                .build();

        String card = Card.builder()
                .object(basic)
                .header("This is very long header that does not fit in one raw")
                .getCard();

        String expected = """
                +------------------------------------+
                |    This is very long header that   |
                |       does not fit in one raw      |
                +--------------+---------------------+
                | first        | 1                   |
                | fourthOption |                     |
                | second       | Second entry        |
                | thirdOption  | Third Option        |
                | time         | 2022-08-10T11:12:12 |
                +--------------+---------------------+
                """;

        Assertions.assertEquals(expected, card);
    }


    @Test
    public void convertingBasicClassToCardWithTooLongOneWordHeader() {

        Basic basic = Basic.builder()
                .first(1L)
                .second("Second entry")
                .thirdOption("Third Option")
                .build();

        Exception exception = Assertions.assertThrows(TooLongWordException.class, () -> {
            String card = Card.builder()
                    .object(basic)
                    .header("ThisIsVeryLongHeaderThatDoesNotFitInOneRaw")
                    .getCard();
        });

        Assertions.assertEquals(exception.getMessage(), "Word ThisIsVeryLongHeaderThatDoesNotFitInOneRaw is too long for header");
    }
}
