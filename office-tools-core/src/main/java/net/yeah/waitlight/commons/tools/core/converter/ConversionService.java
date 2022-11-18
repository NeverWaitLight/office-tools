package net.yeah.waitlight.commons.tools.core.converter;

public interface ConversionService<S, D> {

    D convert(S source);
}
