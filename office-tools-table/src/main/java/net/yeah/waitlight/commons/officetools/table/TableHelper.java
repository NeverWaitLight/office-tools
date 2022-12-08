package net.yeah.waitlight.commons.officetools.table;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

public interface TableHelper<E> {

    void build(Collection<Object> data, OutputStream out);

    <T> List<T> read(E e, Class<T> klass);

    <T> List<T> read(InputStream inputStream, Class<T> klass);
}
