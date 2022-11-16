package net.yeah.waitlight.commons.tools.core.reflection;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class ReflectionUtils {

    private static final ConcurrentMap<Class<?>, ConcurrentMap<String, Method>> GETTER_CACHE = new ConcurrentHashMap<>();


    public static <O, R> Optional<R> getValue(O obj, String field) {
        ConcurrentMap<String, Method> getters = GETTER_CACHE.computeIfAbsent(obj.getClass(), k -> getAllMethods("get", k));
        Method getter = getters.get(field);
        if (Objects.isNull(getter)) return Optional.empty();
        if (!getter.isAccessible()) return Optional.empty();
        try {
            return Optional.ofNullable((R) getter.invoke(obj));
        } catch (ClassCastException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static ConcurrentMap<String, Method> getAllMethods(String prefix, Class<?> klass) {
        return Arrays.stream(klass.getDeclaredMethods())
                .filter(AccessibleObject::isAccessible)
                .filter(m -> m.getName().startsWith(prefix))
                .collect(Collectors.toConcurrentMap(m -> {
                    String name = m.getName();
                    name = name.substring(3);
                    return name.substring(0, 1).toLowerCase() + name.substring(1);
                }, m -> m));
    }

}