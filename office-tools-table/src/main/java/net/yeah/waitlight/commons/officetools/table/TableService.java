package net.yeah.waitlight.commons.officetools.table;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

public interface TableService<T> {

    T build(Collection<Object> data);

    Future<T> buildAsync(Collection<Object> data);

    <D> List<D> read(InputStream inputStream, Class<D> klass);
}
