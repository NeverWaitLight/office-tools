package net.yeah.waitlight.commons.officetools.table.excel;

import net.yeah.waitlight.commons.officetools.common.ThreadPool;
import net.yeah.waitlight.commons.officetools.table.TableHelper;
import net.yeah.waitlight.commons.officetools.table.excel.poi.PoiExcelHSSF;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

public class ExcelService implements TableHelper<Workbook> {

    private final PoiExcelHSSF hssf = new PoiExcelHSSF();

    public Workbook build(Collection<Object> data) {
        try {
            ExcelContextHolder.setData(data);
            return hssf.build(data);
        } finally {
            ExcelContextHolder.clean();
        }
    }

    public Future<Workbook> buildAsync(Collection<Object> data) {
        return ThreadPool.POOL.submit(() -> {
            ExcelContextHolder.setData(data);
            return hssf.build(data);
        });
    }

    @Override
    public void build(Collection<Object> data, OutputStream out) {

    }

    @Override
    public <T> List<T> read(Workbook sheets, Class<T> klass) {
        return null;
    }

    public <T> List<T> read(InputStream inputStream, Class<T> klass) {
        return hssf.read(inputStream, klass);
    }
}
