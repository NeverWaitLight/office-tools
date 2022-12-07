package net.yeah.waitlight.commons.officetools.table;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface TableHelper<E> {

    E build(Collection<Object> data);

    E build(Map<String, Collection<Object>> data);

    <T> List<T> read(E e, Class<T> klass);

    <T> List<T> read(InputStream inputStream, Class<T> klass);
}
