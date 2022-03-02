package eu.integrable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class Tabulator {

    private Class aClass;
    private Object object;

    public Tabulator(Object object) {

        this.aClass = object.getClass();
        this.object = object;
    }

    private static List<Method> getAllGetMethods(Class aClass) {
        return Arrays.stream(aClass.getDeclaredMethods())
                .filter(c -> c.getName().startsWith("get"))
                .sorted((a, b) -> a.getName().compareTo(b.getName()) )
                .collect(Collectors.toUnmodifiableList());
    }

    private static Map<String, String> getMapOfMethodNameAndValue(Object object) {
        Map<String, String> map = new LinkedHashMap<>();

        List<Method> methods = getAllGetMethods(object.getClass());
        methods.stream().forEach(method -> {
            try {
                String name = method.getName().replaceFirst("get", "");
                StringBuilder sb = new StringBuilder(name);
                sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
                name = sb.toString();
                map.put(name, method.invoke(object).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });

        return map;
    }

    public String getJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        return objectMapper.writeValueAsString(object);
    }

    public String getTable() {
        return "will be developed soon";
    }



}
