package eu.integrable;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.integrable.exceptions.TooLongWordException;
import lombok.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
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
        private LocalDateTime time = LocalDateTime.now();
        private String thirdOption;
        private String forthOption;
    }

    @Test
    public void convertingBasicClassToTable() throws TooLongWordException {

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
        System.out.println(table);

        Assertions.assertEquals(1, 1);
    }
}
