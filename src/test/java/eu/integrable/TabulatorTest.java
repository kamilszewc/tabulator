package eu.integrable;

import lombok.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
    }

    @Test
    public void convertingBasicClassToTable() {

        Basic basic = Basic.builder()
                .first(1L)
                .second("test")
                .build();

        Tabulator tabulator = new Tabulator(basic);
        System.out.println(tabulator.getTable());

        Assertions.assertEquals(1, 1);
    }
}
