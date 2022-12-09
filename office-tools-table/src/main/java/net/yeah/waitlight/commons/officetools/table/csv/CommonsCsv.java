package net.yeah.waitlight.commons.officetools.table.csv;

import net.yeah.waitlight.commons.officetools.common.convert.ConversionService;
import net.yeah.waitlight.commons.officetools.common.reflection.FieldDescriptor;
import net.yeah.waitlight.commons.officetools.common.reflection.ReflectionUtils;
import net.yeah.waitlight.commons.officetools.table.AbstractTableHandler;
import net.yeah.waitlight.commons.officetools.table.TableColumn;
import net.yeah.waitlight.commons.officetools.table.TableHandler;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class CommonsCsv extends AbstractTableHandler implements TableHandler {

    private final ConversionService conversionService;

    public CommonsCsv(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public void write(Collection<Object> data, OutputStream outputStream) throws IOException {
        if (Objects.isNull(data) || data.isEmpty()) return;
        if (Objects.isNull(outputStream)) return;

        Class<?> dataType = getAnyDatum(data).getClass();
        List<String> titles = resolveTitleRow(dataType).getValues(String::valueOf);
        List<Object[]> csvRecords = data.stream()
                .map(datum -> resolveDataRow(datum).getValues(o -> o).toArray(new Object[0]))
                .toList();

        OutputStreamWriter writer = new OutputStreamWriter(outputStream);

        CSVFormat.Builder csvFormatBuilder = CSVFormat.Builder.create().setHeader(titles.toArray(new String[0]));
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormatBuilder.build())) {
            csvPrinter.printRecords(csvRecords);
        }
    }

    @Override
    public <T> List<T> read(InputStream inputStream, Class<T> klass) throws IOException {
        Objects.requireNonNull(inputStream);
        Objects.requireNonNull(klass);

        CSVFormat csvFormat = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build();

        List<FieldDescriptor> fieldDescriptors = ReflectionUtils.getFieldDescriptors4Excel(klass, TableColumn.class,
                field -> field.getAnnotation(TableColumn.class).order());

        List<T> data = new ArrayList<>();
        try (CSVParser csvParser = new CSVParser(new InputStreamReader(inputStream), csvFormat)) {
            for (CSVRecord csvRecord : csvParser.getRecords()) {
                T datum = ReflectionUtils.getInstance(klass);
                int cellnum = 0;
                for (String recordValue : csvRecord) {
                    FieldDescriptor fieldDescriptor = fieldDescriptors.get(cellnum++);
                    Object value = conversionService.convert(recordValue, fieldDescriptor.getType());
                    ReflectionUtils.invoke(datum, fieldDescriptor.getSetter(), value);
                }
                data.add(datum);
            }
        }
        return data;
    }
}
