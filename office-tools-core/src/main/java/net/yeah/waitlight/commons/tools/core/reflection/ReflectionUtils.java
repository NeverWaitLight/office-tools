package net.yeah.waitlight.commons.tools.core.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class ReflectionUtils {

    private ReflectionUtils() {
        throw new UnsupportedOperationException("This is util");
    }

    private static final ConcurrentMap<Class<?>, ConcurrentMap<String, Method>> GETTER_CACHE = new ConcurrentHashMap<>();
    private static final ConcurrentMap<Class<?>, ConcurrentMap<String, Method>> SETTER_CACHE = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Class<?>, List<FieldDescriptor>> DP_CACHE = new ConcurrentHashMap<>();

    public static <D, A extends Annotation> List<FieldDescriptor> getFieldDescriptors(D obj, Class<A> fieldAnnoType) {
        Objects.requireNonNull(obj);
        Objects.requireNonNull(fieldAnnoType);

        return DP_CACHE.computeIfAbsent(
                obj.getClass(),
                key -> Arrays.stream(key.getDeclaredFields())
                        .filter(field -> field.isAnnotationPresent(fieldAnnoType))
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

}