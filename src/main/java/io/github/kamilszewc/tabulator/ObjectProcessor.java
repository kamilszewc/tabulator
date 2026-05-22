package io.github.kamilszewc.tabulator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A utility class designed to inspect objects and extract property maps
 * using Java Reflection. It identifies getter methods, converts them into
 * human-readable property keys, and extracts their runtime values.
 * <p>
 * This class is highly useful for serialization, dynamic form building,
 * or quick object-to-map transformations.
 * </p>
 */
class ObjectProcessor {
    /**
     * Extracts a list of property names (keys) from an object's getter methods.
     * For example, a method named {@code getName()} will yield the key {@code "name"}.
     *
     * @param object the target object to inspect
     * @return a {@link List} of property keys extracted from the object's getters
     */
    public static List<String> getListOfKeys(Object object) {
        return getAllGetterMethods(object.getClass()).stream()
                .map(ObjectProcessor::extractKeyFormMethod)
                .collect(Collectors.toList());
    }

    /**
     * Generates a map of property keys and their corresponding string values.
     * <p>
     * If {@code selectedKeys} is null, all available keys are extracted. If a list of
     * keys is provided, the output is filtered and ordered to match that list exactly.
     * Missing keys are silently ignored, and null values default to an empty string.
     * </p>
     *
     * @param object       the object to extract values from
     * @param selectedKeys an optional {@link List} of specific keys to include and order
     * @return a {@link LinkedHashMap} containing the property keys and their string values
     */
    public static Map<String, String> getMapOfMethodNameAndValue(Object object, List<String> selectedKeys) {
        if (object == null) return new LinkedHashMap<>();

        // Build a baseline map of ALL available getters and values
        Map<String, String> allProperties = new LinkedHashMap<>();
        for (Method method : getAllGetterMethods(object.getClass())) {
            try {
                String key = extractKeyFormMethod(method);
                Object result = method.invoke(object);
                allProperties.put(key, result != null ? result.toString() : "");
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        // If no keys were specified, return everything
        if (selectedKeys == null) {
            return allProperties;
        }

        // Otherwise, filter and order the results based on selectedKeys
        Map<String, String> filteredMap = new LinkedHashMap<>();
        for (String key : selectedKeys) {
            if (allProperties.containsKey(key)) {
                filteredMap.put(key, allProperties.get(key));
            }
        }

        return filteredMap;
    }


    /**
     * Transforms a method's name into a clean property key by stripping standard
     * Java Bean prefixes ("get" or "is") and lowering the case of the first letter.
     *
     * <p><b>Examples:</b></p>
     * <ul>
     *   <li>{@code getName()} becomes {@code "name"}</li>
     *   <li>{@code isActive()} becomes {@code "active"}</li>
     *   <li>{@code getX()} becomes {@code "X"}</li>
     * </ul>
     *
     * @param method the {@link Method} whose name will be converted
     * @return the processed property key string
     */
    private static String extractKeyFormMethod(Method method) {

        String name = method.getName();
        String cutName = null;

        if (name.startsWith("get") && name.length() > 3) {
            cutName = name.substring(3);
        }
        if (name.startsWith("is") && name.length() > 2) {
            cutName = name.substring(2);
        }

        if (cutName == null || cutName.isEmpty()) {
            return name;
        }

        if (cutName.length() == 1) {
            return cutName.toUpperCase();
        }

        return Character.toLowerCase(cutName.charAt(0)) + cutName.substring(1);
    }

    /**
     * Retrieves all declared methods from a given class that start with the
     * standard Java Bean getter prefixes "get" or "is". The resulting list
     * is sorted alphabetically by method name.
     *
     * @param aClass the target {@link Class} to inspect
     * @return a sorted {@link List} of getter {@link Method} objects
     */
    private static List<Method> getAllGetterMethods(Class<?> aClass) {
        return Arrays.stream(aClass.getDeclaredMethods())
                .filter(method -> method.getName().startsWith("get") || method.getName().startsWith("is"))
                .sorted(Comparator.comparing(Method::getName))
                .collect(Collectors.toList());
    }
}
