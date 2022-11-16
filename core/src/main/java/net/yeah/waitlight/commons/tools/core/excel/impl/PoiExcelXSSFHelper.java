package net.yeah.waitlight.commons.tools.core.excel.impl;

import net.yeah.waitlight.commons.tools.core.excel.ExcelHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Collection;
import java.util.List;

public class PoiExcelXSSFHelper extends AbstractPoiExcelHelper implements ExcelHelper<XSSFWorkbook> {

    @Override
    public <D> XSSFWorkbook buildExcel(Collection<D> data) {
        if (CollectionUtils.isEmpty(data)) return new XSSFWorkbook();

        XSSFWorkbook workbook = new XSSFWorkbook();
        fillWorkbook(workbook, data);
        return workbook;
    }

    @Override
    public <D> List<D> readFromExcel(XSSFWorkbook sheets) {
        return null;
    }

}
