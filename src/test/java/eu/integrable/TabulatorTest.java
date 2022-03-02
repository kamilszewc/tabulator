package eu.integrable;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Locale;

public class TabulatorTest {

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
    }

    @Test
    public void convertingBasicClassToTable() throws JsonProcessingException {

        Basic basic = Basic.builder()
                .first(1L)
                .second("test")
                .build();

        Tabulator tabulatorOne = new Tabulator(basic);
        System.out.println(tabulatorOne.getTable());
        System.out.println(tabulatorOne.getJson());

        Tabulator tabulatorTwo = Tabulator.builder()
                .object(basic)
                .build();
        System.out.println(tabulatorTwo.getTable());
        System.out.println(tabulatorTwo.getJson());

        String table = Tabulator.builder()
                .object(basic)
                .getTable();
        System.out.println(table);

        Assertions.assertEquals(1, 1);
    }
}
