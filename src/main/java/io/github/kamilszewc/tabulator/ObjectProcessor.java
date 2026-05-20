package io.github.kamilszewc.tabulator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

class ObjectProcessor {

    public static List<Method> getAllGetMethods(Class<?> aClass) {
        return Arrays.stream(aClass.getDeclaredMethods())
                .filter(c -> c.getName().startsWith("get"))
                .sorted(Comparator.comparing(Method::getName))
                .collect(Collectors.toList());
    }

    public static List<String> getListOfKeys(Object object) {
        return getAllGetMethods(object.getClass()).stream()
                .map(ObjectProcessor::extractKeyFormMethod)
                .collect(Collectors.toList());
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
                .collect(Collectors.toList());
    }

    public static String extractKeyFormMethod(Method method) {
        String name = method.getName();

        if (name.startsWith("get") && name.length() > 3) {
            name = name.substring(3);
        } else if (name.startsWith("is") && name.length() > 2) {
            name = name.substring(2);
        } else {
            return name;
        }

        return Character.toLowerCase(name.charAt(0)) + name.substring(1);


//        String name = method.getName().replaceFirst("get", "");
//        StringBuilder sb = new StringBuilder(name);
//        sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
//        return sb.toString();
    }

    public static Map<String, String> getMapOfMethodNameAndValue(Object object, List<String> selectedKeys) {

        if (object == null) {
            return Collections.emptyMap();
        }

        Map<String, String> map = new LinkedHashMap<>();


//        if (selectedKeys == null) return getMapOfMethodNameAndValue(object);
//
//        Map<String, String> map = new LinkedHashMap<>();
//
        List<Method> methods = getAllGetMethods(object.getClass());
//
        // If no selected keys -> fallback
        if (selectedKeys == null || selectedKeys.isEmpty()) {
            return getMapOfMethodNameAndValue(object);
        }

        for (String key : selectedKeys) {
            if (key == null || key.isEmpty()) {
                continue;
            }

            Method method = methods.stream()
                    .filter(m -> {
                        String name = extractKeyFormMethod(m);
                        return key.equals(name);
                    })
                    .findFirst()
                    .orElse(null);

            if (method == null) {
                continue;
            }

            try {
                Object result = method.invoke(object);
                String value = (result != null) ? result.toString() : "";
                String name = extractKeyFormMethod(method);
                map.put(name, value);

            } catch (IllegalAccessException | InvocationTargetException ex) {
                ex.printStackTrace();
            }
        }

        return map;
//        selectedKeys.stream().forEach(key -> {
//            Method method;
//            try {
//                method = methods.stream()
//                        .filter(m -> {
//                            String name = extractKeyFormMethod(m);
//                            return name.equals(key);
//                        })
//                        .findFirst()
//                        .get();
//            } catch (NoSuchElementException ex) {
//                return;
//            }
//
//            try {
//                String name = extractKeyFormMethod(method);
//                Object result = method.invoke(object);
//                if (result != null) {
//                    map.put(name, method.invoke(object).toString());
//                } else {
//                    map.put(name, "");
//                }
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            }
//        });
//
//        return map;
    }

    public static Map<String, String> getMapOfMethodNameAndValue(Object object) {

        if (object == null) {
            return Collections.emptyMap();
        }

        Map<String, String> map = new LinkedHashMap<>();

        List<Method> methods = getAllGetMethods(object.getClass());
        for (Method method : methods) {
            try {
                String name = extractKeyFormMethod(method);
                Object result = method.invoke(object);
                map.put(name, result != null ? result.toString() : "");

            } catch (IllegalAccessException | InvocationTargetException ex) {
                ex.printStackTrace();
            }
        }

        return map;
//        methods.stream().forEach(method -> {
//            try {
//                String name = extractKeyFormMethod(method);
//                Object result = method.invoke(object);
//                if (result != null) {
//                    map.put(name, method.invoke(object).toString());
//                } else {
//                    map.put(name, "");
//                }
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            }
//        });
//
//        return map;
    }
}
