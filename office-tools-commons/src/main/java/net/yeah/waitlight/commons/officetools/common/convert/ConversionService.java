package net.yeah.waitlight.commons.officetools.common.convert;

import net.yeah.waitlight.commons.officetools.common.reflection.ReflectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ConversionService {

    private final Map<ConverterCacheKey, Converter> converterCache = new HashMap<>();
    private final Map<ConverterCacheKey, ConverterFactory> converterFactoryCache = new HashMap<>();

    public ConversionService() {
        converterCache.put(new ConverterCacheKey(String.class, Boolean.class), new String2BooleanConverter());

        converterFactoryCache.put(new ConverterCacheKey(String.class, Enum.class), new String2EnumConverterFactory());
        converterFactoryCache.put(new ConverterCacheKey(String.class, Number.class), new String2NumberConverterFactory());
    }

    public <T> T convert(Object source, Class<T> targetType) {
        Objects.requireNonNull(source, "source can not be null");
        Objects.requireNonNull(targetType, "target type can not be null");

        final Class<?> sourceType = source.getClass();

        Converter convert = getConvert(sourceType, targetType);
        if (Objects.isNull(convert)) {
            return (T) source;
        }

        return (T) convert.convert(source);
    }

    private Converter getConvert(Class<?> sourceType, Class<?> targetType) {
        ConverterCacheKey key = new ConverterCacheKey(sourceType, getType(targetType));

        Converter converter = converterCache.get(key);
        if (Objects.nonNull(converter)) return converter;

        ConverterFactory factory = converterFactoryCache.get(key);
        if (Objects.isNull(factory)) return null;
        return factory.getConverter(targetType);
    }

    private Class<?> getType(Class<?> type) {
        if (Number.class.isAssignableFrom(type)) {
            return ReflectionUtils.getNumberType(type);
        }
        if (Enum.class.isAssignableFrom(type)) {
            return ReflectionUtils.getEnumType(type);
        }
        return type;
    }

    record ConverterCacheKey(
            Class<?> sourceType,
            Class<?> targetType
    ) {
    }
}
