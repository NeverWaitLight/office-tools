package net.yeah.waitlight.commons.officetools.table;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public record CellDescriptor(
        Object obj,
        Field field,
        Method getter,
        Method setter,
        Object value
) {

    public CellDescriptor(Object value) {
        this(null, null, null, null, value);
    }
}
