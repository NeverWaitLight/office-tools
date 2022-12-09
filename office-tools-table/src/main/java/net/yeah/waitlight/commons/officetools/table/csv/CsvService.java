package net.yeah.waitlight.commons.officetools.table.csv;

import net.yeah.waitlight.commons.officetools.common.convert.ConversionService;
import net.yeah.waitlight.commons.officetools.table.TableHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

public class CsvService implements TableHandler {
    private final CommonsCsv commonsCsv;

    public CsvService(ConversionService conversionService) {
        this.commonsCsv = new CommonsCsv(conversionService);
    }

    @Override
    public void write(Collection<Object> data, OutputStream out) throws IOException {
        commonsCsv.write(data, out);
    }

    @Override
    public <D> List<D> read(InputStream inputStream, Class<D> klass) throws IOException {
        return commonsCsv.read(inputStream, klass);
    }
}
