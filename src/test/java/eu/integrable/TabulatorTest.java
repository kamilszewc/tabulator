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

        Tabulator tabulator = new Tabulator(basic);
        System.out.println(tabulator.getTable());
        System.out.println(tabulator.getJson());

        Assertions.assertEquals(1, 1);
    }
}
