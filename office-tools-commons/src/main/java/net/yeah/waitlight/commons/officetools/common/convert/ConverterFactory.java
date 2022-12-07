package net.yeah.waitlight.commons.officetools.common.convert;

public interface ConverterFactory<S, R> {
    <T extends R> Converter<S, T> getConverter(Class<T> targetType);
}
