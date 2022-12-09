package net.yeah.waitlight.commons.officetools.table;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

public interface TableHandler {

    void write(Collection<Object> data, OutputStream out) throws IOException;

    <T> List<T> read(InputStream inputStream, Class<T> klass) throws IOException;
}
