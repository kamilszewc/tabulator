package eu.integrable;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

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
        private LocalDateTime time = LocalDateTime.now();
        private String thirdOption;
        private String forthOption;
    }

    @Test
    public void convertingBasicClassToCard() {

        Basic basic = Basic.builder()
                .first(1L)
                .second("test")
                .thirdOption("Nie rozumiem tego")
                .build();

        String table = Card.builder()
                .object(basic)
                .header("Basic class xxx to ciekawe i nie rozu")
                .getCard();
        System.out.println(table);

        Assertions.assertEquals(1, 1);
    }
}
