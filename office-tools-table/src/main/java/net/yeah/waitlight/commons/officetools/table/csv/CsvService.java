package net.yeah.waitlight.commons.officetools.table.csv;

import net.yeah.waitlight.commons.officetools.table.TableHelper;
import org.apache.commons.csv.CSVPrinter;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

public class CsvService implements TableHelper<CSVPrinter> {
    private final CommonsCsv commonsCsv = new CommonsCsv();

    public void build(Collection<Object> data, OutputStream out) {
        commonsCsv.build(data, out);
    }

    @Override
    public <T> List<T> read(CSVPrinter csvPrinter, Class<T> klass) {
        return null;
    }
   
    @Override
    public <D> List<D> read(InputStream inputStream, Class<D> klass) {
        return null;
    }
}
