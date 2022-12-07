package net.yeah.waitlight.commons.officetools.common.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ReflectionUtils {

    private ReflectionUtils() {
        throw new UnsupportedOperationException("This is util");
    }

    private static final ConcurrentMap<Class<?>, List<Field>> FIELD_CACHE = new ConcurrentHashMap<>();
    private static final ConcurrentMap<Class<?>, ConcurrentMap<String, Method>> GETTER_CACHE = new ConcurrentHashMap<>();
    private static final ConcurrentMap<Class<?>, ConcurrentMap<String, Method>> SETTER_CACHE = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Class<?>, List<FieldDescriptor>> DP_CACHE = new ConcurrentHashMap<>();

    public static List<FieldDescriptor> getFieldDescriptors4Excel(Class<?> klass, Class<? extends Annotation> annoType, Function<Field, Integer> orderFunc) {
        Objects.requireNonNull(klass);

        return DP_CACHE.computeIfAbsent(
                klass,
                key -> Arrays.stream(key.getDeclaredFields())
                        .filter(field -> field.isAnnotationPresent(annoType))
                        .sorted(Comparator.comparingInt(field -> {
                            if (Objects.isNull(orderFunc)) return 0;
                            return orderFunc.apply(field);
                        }))
                        .map(field -> new FieldDescriptor()
                                .setField(field)
                                .setSetter(getSetter(klass, field))
                                .setGetter(getGetter(klass, field))
                        )
                        .toList()
        );
    }

    public static Method getGetter(Class<?> klass, Field field) {
        return getGetter(klass, field.getName());
    }

    public static Method getGetter(Class<?> klass, String fieldName) {
        Objects.requireNonNull(klass);
        Method getter = GETTER_CACHE.computeIfAbsent(klass, key -> getAllMethods("get", klass))
                .get(fieldName);
        return Objects.requireNonNull(getter);
    }

    public static Method getSetter(Class<?> klass, Field field) {
        return getSetter(klass, field.getName());
    }

    public static Method getSetter(Class<?> klass, String fieldName) {
        Objects.requireNonNull(klass);
        Method setter = SETTER_CACHE.computeIfAbsent(klass, key -> getAllMethods("set", klass))
                .get(fieldName);
        return Objects.requireNonNull(setter);
    }

    public static ConcurrentMap<String, Method> getAllMethods(String prefix, Class<?> klass) {
        return Arrays.stream(klass.getDeclaredMethods())
                .filter(m -> Modifier.isPublic(m.getModifiers()))
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

    public static Object invoke(Object obj, Method method, Object... args) {
        Objects.requireNonNull(obj);
        Objects.requireNonNull(method);
        try {
            return method.invoke(obj, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({"unchecked"})
    public static <D> D getInstance(Class<D> klass) {
        Constructor<?> constructor = klass.getConstructors()[0];
        Objects.requireNonNull(constructor);

        try {
            return (D) constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Class<?> getEnumType(Class<?> targetType) {
        Class<?> enumType = targetType;
        while (enumType != null && !Enum.class.equals(enumType)) {
            enumType = enumType.getSuperclass();
        }
        return enumType;
    }

    public static Class<?> getNumberType(Class<?> targetType) {
        Class<?> numberType = targetType;
        while (numberType != null && !Number.class.equals(numberType)) {
            numberType = numberType.getSuperclass();
        }
        return numberType;
    }
}