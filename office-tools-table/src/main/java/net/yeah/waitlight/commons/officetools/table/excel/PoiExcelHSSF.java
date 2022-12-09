package net.yeah.waitlight.commons.officetools.table.excel;

import lombok.extern.slf4j.Slf4j;
import net.yeah.waitlight.commons.officetools.common.convert.ConversionService;
import net.yeah.waitlight.commons.officetools.table.TableHandler;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

@Slf4j
public class PoiExcelHSSF extends AbstractPoiExcel implements TableHandler {

    public PoiExcelHSSF(ConversionService conversionService) {
        super(conversionService);
    }

    @Override
    public void write(Collection<Object> data, OutputStream outputStream) throws IOException {
        if (CollectionUtils.isEmpty(data)) return;
        try (HSSFWorkbook workbook = new HSSFWorkbook()) {
            fillWorkbook(data, workbook);
            workbook.write(outputStream);
        }
    }

    @Override
    public <T> List<T> read(InputStream inputStream, Class<T> klass) throws IOException {
        try (HSSFWorkbook workbook = new HSSFWorkbook(inputStream)) {
            return readWorkbook(workbook, klass);
        }
    }
}
