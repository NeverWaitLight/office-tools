package net.yeah.waitlight.commons.tools.core.converter;

public class DefaultConvertService implements ConversionService<Object, Object> {
    @Override
    public Object convert(Object source) {
        return source;
    }
}
