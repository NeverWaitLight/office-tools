package net.yeah.waitlight.commons.officetools.table.excel;

import lombok.extern.slf4j.Slf4j;
import net.yeah.waitlight.commons.officetools.common.convert.ConversionService;
import net.yeah.waitlight.commons.officetools.table.TableHandler;
import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Future;

import static net.yeah.waitlight.commons.officetools.common.OfficeToolsThreadPool.DEFAULT_POOL;

@Slf4j
public class ExcelService implements TableHandler {

    private final PoiExcelHSSF hssf;

    public ExcelService(ConversionService conversionService) {
        this.hssf = new PoiExcelHSSF(conversionService);
    }

    @Override
    public void write(Collection<Object> data, OutputStream outputStream) throws IOException {
        if (CollectionUtils.isEmpty(data)) return;
        Objects.requireNonNull(outputStream, "OutputStream is required");

        hssf.write(data, outputStream);
    }

    public Future<Void> buildAsync(Collection<Object> data, OutputStream outputStream) {
        if (CollectionUtils.isEmpty(data)) return null;
        Objects.requireNonNull(outputStream, "OutputStream is required");

        return DEFAULT_POOL.submit(() -> {
            write(data, outputStream);
            return null;
        });
    }

    @Override
    public <T> List<T> read(InputStream inputStream, Class<T> klass) throws IOException {
        Objects.requireNonNull(inputStream, "InputStream is required");
        Objects.requireNonNull(klass, "Klass is required");

        return hssf.read(inputStream, klass);
    }
}
