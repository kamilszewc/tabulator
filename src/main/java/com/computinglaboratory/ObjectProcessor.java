package com.computinglaboratory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ObjectProcessor {

    public static List<Method> getAllGetMethods(Class aClass) {
        return Arrays.stream(aClass.getDeclaredMethods())
                .filter(c -> c.getName().startsWith("get"))
                .sorted((a, b) -> a.getName().compareTo(b.getName()) )
                .toList();
    }

    public static List<String> getListOfKeys(Object object) {
        return getAllGetMethods(object.getClass()).stream()
                .map(method -> extractKeyFormMethod(method))
                .toList();
    }

    public static List<String> getListOfValues(Object object) {
        return getAllGetMethods(object.getClass()).stream()
                .map(method -> {
                    try {
                        Object result = method.invoke(object);
                        if (result != null) {
                            return method.invoke(object).toString();
                        } else {
                            return "";
                        }
                    } catch (Exception ex) {
                        return "";
                    }
                })
                .toList();
    }

    public static String extractKeyFormMethod(Method method) {
        String name = method.getName().replaceFirst("get", "");
        StringBuilder sb = new StringBuilder(name);
        sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
        return sb.toString();
    }

    public static Map<String, String> getMapOfMethodNameAndValue(Object object, List<String> selectedKeys) {

        if (selectedKeys == null) return getMapOfMethodNameAndValue(object);

        Map<String, String> map = new LinkedHashMap<>();

        List<Method> methods = getAllGetMethods(object.getClass());

        selectedKeys.stream().forEach(key -> {
            Method method;
            try {
                method = methods.stream()
                        .filter(m -> {
                            String name = extractKeyFormMethod(m);
                            return name.equals(key);
                        })
                        .findFirst()
                        .get();
            } catch (NoSuchElementException ex) {
                return;
            }

            try {
                String name = extractKeyFormMethod(method);
                Object result = method.invoke(object);
                if (result != null) {
                    map.put(name, method.invoke(object).toString());
                } else {
                    map.put(name, "");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });

        return map;
    }

    public static Map<String, String> getMapOfMethodNameAndValue(Object object) {
        Map<String, String> map = new LinkedHashMap<>();

        List<Method> methods = getAllGetMethods(object.getClass());
        methods.stream().forEach(method -> {
            try {
                String name = extractKeyFormMethod(method);
                Object result = method.invoke(object);
                if (result != null) {
                    map.put(name, method.invoke(object).toString());
                } else {
                    map.put(name, "");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });

        return map;
    }
}
