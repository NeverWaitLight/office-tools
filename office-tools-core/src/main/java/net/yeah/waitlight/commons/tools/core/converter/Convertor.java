package net.yeah.waitlight.commons.tools.core.converter;

import java.lang.reflect.Field;

public interface Convertor<S, T> {
    T convert(S source);


    T convert(Field source);
}
