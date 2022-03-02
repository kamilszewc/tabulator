package eu.integrable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder
@Setter
@Getter
public class Tabulator {

    private Object object;

    public Tabulator(Object object) {
        this.object = object;
    }

    public String getJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        return objectMapper.writeValueAsString(object);
    }

    public String getTable() {
        var map = ObjectProcessor.getMapOfMethodNameAndValue(object);

        return "will be developed soon";
    }

    public static class TabulatorBuilder {

        public String getJson() throws JsonProcessingException {
            return build().getJson();
        }

        public String getTable() {
            return build().getTable();
        }
    }

}
