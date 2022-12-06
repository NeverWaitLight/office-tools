package net.yeah.waitlight.commons.tools.core.reflection;

import net.yeah.waitlight.commons.tools.core.excel.ExcelColumn;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class ReflectionUtils {

    private ReflectionUtils() {
        throw new UnsupportedOperationException("This is util");
    }

    private static final ConcurrentMap<Class<?>, List<Field>> FIELD_CACHE = new ConcurrentHashMap<>();
    private static final ConcurrentMap<Class<?>, ConcurrentMap<String, Method>> GETTER_CACHE = new ConcurrentHashMap<>();
    private static final ConcurrentMap<Class<?>, ConcurrentMap<String, Method>> SETTER_CACHE = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Class<?>, List<FieldDescriptor>> DP_CACHE = new ConcurrentHashMap<>();

    public static List<FieldDescriptor> getFieldDescriptors4Excel(Object obj) {
        Objects.requireNonNull(obj);

        return DP_CACHE.computeIfAbsent(
                obj.getClass(),
                key -> Arrays.stream(key.getDeclaredFields())
                        .filter(field -> field.isAnnotationPresent(ExcelColumn.class))
                        .sorted(Comparator.comparingInt(field -> field.getAnnotation(ExcelColumn.class).order()))
                        .map(field -> new FieldDescriptor()
                                .setField(field)
                                .setSetter(getSetter(obj, field))
                                .setGetter(getGetter(obj, field))
                        )
                        .toList()
        );
    }

    public static <T> Method getGetter(T obj, Field field) {
        return getGetter(obj, field.getName());
    }

    public static <T> Method getGetter(T obj, String fieldName) {
        Objects.requireNonNull(obj);
        Method getter = GETTER_CACHE.computeIfAbsent(obj.getClass(), klass -> getAllMethods("get", obj))
                .get(fieldName);
        return Objects.requireNonNull(getter);
    }

    public static <T> Method getSetter(T obj, Field field) {
        return getSetter(obj, field.getName());
    }

    public static <T> Method getSetter(T obj, String fieldName) {
        Objects.requireNonNull(obj);
        Method setter = SETTER_CACHE.computeIfAbsent(obj.getClass(), klass -> getAllMethods("set", obj))
                .get(fieldName);
        return Objects.requireNonNull(setter);
    }

    public static <O> Object getValue(O obj, Method getter) {
        Objects.requireNonNull(obj);
        Objects.requireNonNull(getter);

        try {
            return getter.invoke(obj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> ConcurrentMap<String, Method> getAllMethods(String prefix, T obj) {
        return Arrays.stream(obj.getClass().getDeclaredMethods())
                .filter(m -> m.canAccess(obj))
                .filter(m -> m.getName().startsWith(prefix))
                .collect(Collectors.toConcurrentMap(m -> {
                    String name = m.getName();
                    name = name.substring(3);
                    return name.substring(0, 1).toLowerCase() + name.substring(1);
                }, m -> m));
    }

    public static List<Field> getFields(Class<?> klass) {
        return FIELD_CACHE.computeIfAbsent(klass, key -> List.of(klass.getDeclaredFields()));
    }

    public static List<? extends Annotation> getAnnotations(Class<?> klass, Class<? extends Annotation> annoType) {
        if (Objects.isNull(klass) || Objects.isNull(annoType)) return Collections.emptyList();

        return getFields(klass).stream()
                .filter(f -> f.isAnnotationPresent(annoType))
                .map(f -> f.getAnnotation(annoType))
                .toList();
    }

    public static Object getValue(Method method, Object obj) {
        try {
            return method.invoke(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}