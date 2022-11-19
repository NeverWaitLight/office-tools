package net.yeah.waitlight.commons.tools.core.converter;

public class Boolean2StringService implements ConversionService<Boolean, String> {
    @Override
    public String convert(Boolean source) {
        if (Boolean.TRUE.equals(source)) return "是";
        return "否";
    }
}
