package net.yeah.waitlight.commons.officetools.common.convert;

public interface Converter<S, T> {
    T convert(S s);
}
