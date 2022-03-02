package eu.integrable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ObjectProcessor {

    public static List<Method> getAllGetMethods(Class aClass) {
        return Arrays.stream(aClass.getDeclaredMethods())
                .filter(c -> c.getName().startsWith("get"))
                .sorted((a, b) -> a.getName().compareTo(b.getName()) )
                .collect(Collectors.toUnmodifiableList());
    }

    public static Map<String, String> getMapOfMethodNameAndValue(Object object) {
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
}
