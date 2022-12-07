package net.yeah.waitlight.commons.officetools.table;

import java.util.Collection;
import java.util.Objects;

public final class ExcelContextHolder {

    private static final ThreadLocal<Collection<?>> dataHolder = new ThreadLocal<>();
    private static final ThreadLocal<Object> objHolder = new ThreadLocal<>();

    public static void clean() {
        dataHolder.remove();
        objHolder.remove();
    }

    public static void setData(Collection<?> data) {
        if (Objects.isNull(data)) return;
        dataHolder.set(data);
    }

    public static Collection<?> getData() {
        return dataHolder.get();
    }


    public static void setObj(Object datum) {
        objHolder.set(datum);
    }

    public static Object getObj() {
        return objHolder.get();
    }
}
