package eu.integrable;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.integrable.exceptions.NotImplementedException;
import eu.integrable.exceptions.TabulatorException;
import eu.integrable.exceptions.TooLongElementException;
import eu.integrable.exceptions.TooLongWordException;
import lombok.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public class TableTest {

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
        private LocalDateTime time = LocalDateTime.of(2022, Month.AUGUST, 22, 21, 20, 12);
        private String thirdOption;
        private String forthOption;
    }

    @Test
    public void convertingBasicClassToTable() throws TabulatorException {

        Basic basicOne = Basic.builder()
                .first(1L)
                .second("Second entry")
                .thirdOption("Third Option")
                .build();

        Basic basicTwo = Basic.builder()
                .first(2L)
                .second("Second entry")
                .thirdOption("Third Option")
                .build();

        List<Object> basics = List.of(basicOne, basicTwo);

        String table = Table.builder()
                .object(basics)
                .header("Basic class")
                .getTable();

        String expected = """
                +-------------------------------------------------------------------------+
                |                               Basic class                               |
                +-------+-------------+--------------+--------------+---------------------+
                | first | forthOption | second       | thirdOption  | time                |
                +-------+-------------+--------------+--------------+---------------------+
                | 1     |             | Second entry | Third Option | 2022-08-22T21:20:12 |
                +-------+-------------+--------------+--------------+---------------------+
                | 2     |             | Second entry | Third Option | 2022-08-22T21:20:12 |
                +-------+-------------+--------------+--------------+---------------------+
                """;

        Assertions.assertEquals(expected, table);
    }

    @Test
    public void convertingBasicClassToTableWithTooLongHeader() throws TabulatorException {

        Basic basicOne = Basic.builder()
                .first(1L)
                .second("Second entry")
                .thirdOption("Third Option")
                .build();

        Basic basicTwo = Basic.builder()
                .first(2L)
                .second("Second entry")
                .thirdOption("Third Option")
                .build();

        List<Object> basics = List.of(basicOne, basicTwo);

        String table = Table.builder()
                .object(basics)
                .header("Basic class with too long header -> I need more and more and more and more and more text here")
                .getTable();

        String expected = """
                +-------------------------------------------------------------------------+
                |  Basic class with too long header -> I need more and more and more and  |
                |                         more and more text here                         |
                +-------+-------------+--------------+--------------+---------------------+
                | first | forthOption | second       | thirdOption  | time                |
                +-------+-------------+--------------+--------------+---------------------+
                | 1     |             | Second entry | Third Option | 2022-08-22T21:20:12 |
                +-------+-------------+--------------+--------------+---------------------+
                | 2     |             | Second entry | Third Option | 2022-08-22T21:20:12 |
                +-------+-------------+--------------+--------------+---------------------+
                """;

        Assertions.assertEquals(expected, table);
    }

    @Test
    public void convertingBasicClassToTableWithTooLongWordException() throws TabulatorException {

        Basic basicOne = Basic.builder()
                .first(1L)
                .second("Second entry")
                .thirdOption("Third Option")
                .build();

        Basic basicTwo = Basic.builder()
                .first(2L)
                .second("Second entry")
                .thirdOption("Third Option")
                .build();

        List<Object> basics = List.of(basicOne, basicTwo);

        Exception exception = Assertions.assertThrows(TooLongElementException.class, () -> {
                    String table = Table.builder()
                            .object(basics)
                            .maxColumnWidth(10)
                            .header("Basic class with too long header -> I need more and more and more and more and more text here")
                            .getTable();
                });

        Assertions.assertEquals(exception.getMessage(), "Element 'forthOption' is too long for table entry -> use multiLine=true");
    }

    @Test
    public void convertingBasicClassToTableWithTooLongElementButMultiLineTrue() throws TabulatorException {

        Basic basicOne = Basic.builder()
                .first(1L)
                .second("Second entry")
                .thirdOption("Third Option")
                .build();

        Basic basicTwo = Basic.builder()
                .first(2L)
                .second("Second entry")
                .thirdOption("Third Option that is too long")
                .build();

        List<Object> basics = List.of(basicOne, basicTwo);

        String table = Table.builder()
                .object(basics)
                .maxColumnWidth(20)
                .multiLine(true)
                .header("Basic class with too long header -> I need more and more and more and more and more text here")
                .getTable();

        System.out.println(table);

//        String expected = """
//                +-------------------------------------------------------------------------+
//                |  Basic class with too long header -> I need more and more and more and  |
//                |                         more and more text here                         |
//                +-------+-------------+--------------+--------------+---------------------+
//                | first | forthOption | second       | thirdOption  | time                |
//                +-------+-------------+--------------+--------------+---------------------+
//                | 1     |             | Second entry | Third Option | 2022-08-22T21:20:12 |
//                +-------+-------------+--------------+--------------+---------------------+
//                | 2     |             | Second entry | Third Option | 2022-08-22T21:20:12 |
//                +-------+-------------+--------------+--------------+---------------------+
//                """;
//
//        Assertions.assertEquals(expected, table);
    }
}
