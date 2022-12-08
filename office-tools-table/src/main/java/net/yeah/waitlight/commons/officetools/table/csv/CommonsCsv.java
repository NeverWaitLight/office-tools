package net.yeah.waitlight.commons.officetools.table.csv;

import net.yeah.waitlight.commons.officetools.table.AbstractTableHelper;
import net.yeah.waitlight.commons.officetools.table.RowDescriptor;
import net.yeah.waitlight.commons.officetools.table.TableHelper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class CommonsCsv extends AbstractTableHelper implements TableHelper<CSVPrinter> {

    @Override
    public void build(Collection<Object> data, OutputStream outputStream) {
        if (Objects.isNull(data) || data.isEmpty()) return;
        if (Objects.isNull(outputStream)) return;

        Class<?> dataType = getAnyDatum(data).getClass();
        List<String> titles = resolveTitleRow(dataType).getValues(String::valueOf);
        List<Object[]> record = data.stream()
                .map(datum -> resolveDataRow(datum).getValues(o -> o).toArray(new Object[0]))
                .toList();

        OutputStreamWriter writer = new OutputStreamWriter(outputStream);

        CSVFormat.Builder csvFormatBuilder = CSVFormat.Builder.create().setHeader(titles.toArray(new String[0]));
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormatBuilder.build())) {
            csvPrinter.printRecords(record);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CSVPrinter build(Collection<Object> data) {
        if (Objects.isNull(data) || data.isEmpty()) return null;

        Class<?> dataType = (getAnyDatum(data)).getClass();
        List<String> titles = resolveTitleRow(dataType).getValues(String::valueOf);


        CSVFormat.Builder builder = CSVFormat.Builder.create().setHeader(titles.toArray(new String[0]));

        try (
                FileWriter out = new FileWriter("book_new.csv");
                CSVPrinter printer = new CSVPrinter(out, builder.build())
        ) {
            data.forEach(d -> {
                RowDescriptor rowDescriptor = resolveDataRow(d);
                try {
                    printer.printRecord(rowDescriptor.getValues(o -> o).toArray());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> List<T> read(CSVPrinter csvPrinter, Class<T> klass) {
        return null;
    }

    @Override
    public <T> List<T> read(InputStream inputStream, Class<T> klass) {
        return null;
    }
}
