package net.yeah.waitlight.commons.officetools.table.excel.poi;

import lombok.extern.slf4j.Slf4j;
import net.yeah.waitlight.commons.officetools.table.TableHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

@Slf4j
public class PoiExcelHSSF extends AbstractPoiExcel implements TableHelper<HSSFWorkbook> {

    @Override
    public void build(Collection<Object> data, OutputStream outputStream) {

    }

    public HSSFWorkbook build(Collection<Object> data) {
        if (CollectionUtils.isEmpty(data)) return new HSSFWorkbook();
        return build(Map.of("sheet", data));
    }

    public HSSFWorkbook build(Map<String, Collection<Object>> data) {
        if (MapUtils.isEmpty(data)) return new HSSFWorkbook();
        HSSFWorkbook workbook = new HSSFWorkbook();
        fillWorkbook(data, workbook);
        return workbook;
    }

    @Override
    public <T> List<T> read(HSSFWorkbook workbook, Class<T> klass) {
        if (Objects.isNull(workbook)) return Collections.emptyList();
        return readWorkbook(workbook, klass);
    }

    @Override
    public <T> List<T> read(InputStream inputStream, Class<T> klass) {
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
            return read(workbook, klass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
