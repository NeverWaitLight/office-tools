package net.yeah.waitlight.commons.tools.core.excel;

import net.yeah.waitlight.commons.tools.core.ThreadPool;
import net.yeah.waitlight.commons.tools.core.excel.poi.PoiExcelHSSFHelper;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Collection;
import java.util.concurrent.Future;

public class ExcelService {

    Workbook buildExcel(Collection<Object> data) {
        PoiExcelHSSFHelper helper = new PoiExcelHSSFHelper();
        Future<HSSFWorkbook> submit = ThreadPool.POOL.submit(() -> helper.buildExcel(data));
        try {
            return submit.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
