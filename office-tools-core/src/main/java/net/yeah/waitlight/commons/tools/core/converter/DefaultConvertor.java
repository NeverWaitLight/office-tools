package net.yeah.waitlight.commons.tools.core.converter;

public class DefaultConvertor implements ConversionService<Object, Object> {
    @Override
    public Object convert(Object source) {
        return source;
    }
}
