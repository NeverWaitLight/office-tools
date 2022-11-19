package net.yeah.waitlight.commons.tools.core.excel.poi;

import net.yeah.waitlight.commons.tools.core.LanguageCountry;
import net.yeah.waitlight.commons.tools.core.excel.ExcelHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Collection;
import java.util.List;

public class PoiExcelXSSFHelper extends AbstractPoiExcelHelper implements ExcelHelper<XSSFWorkbook> {

    @Override
    public <D> XSSFWorkbook buildExcel(Collection<D> data, LanguageCountry languageCountry) {
        if (CollectionUtils.isEmpty(data)) return new XSSFWorkbook();

        XSSFWorkbook workbook = new XSSFWorkbook();
        fillWorkbook(data, workbook);
        return workbook;
    }

    @Override
    public <D> List<D> readExcel(XSSFWorkbook sheets, Class<D> klass) {
        return null;
    }

}
