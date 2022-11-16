package net.yeah.waitlight.commons.tools.core.excel.impl;

import net.yeah.waitlight.commons.tools.core.excel.ExcelHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.Collection;
import java.util.List;

public class PoiExcelHSSFHelper extends AbstractPoiExcelHelper implements ExcelHelper<HSSFWorkbook> {

    @Override
    public <D> HSSFWorkbook buildExcel(Collection<D> data) {
        if (CollectionUtils.isEmpty(data)) return new HSSFWorkbook();
        HSSFWorkbook workbook = new HSSFWorkbook();
        fillWorkbook(workbook, data);
        return workbook;
    }

    @Override
    public <D> List<D> readFromExcel(HSSFWorkbook sheets) {
        return null;
    }
}
