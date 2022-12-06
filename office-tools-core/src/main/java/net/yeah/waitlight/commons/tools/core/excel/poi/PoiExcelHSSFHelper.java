package net.yeah.waitlight.commons.tools.core.excel.poi;

import lombok.extern.slf4j.Slf4j;
import net.yeah.waitlight.commons.tools.core.excel.ExcelHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
public class PoiExcelHSSFHelper extends AbstractPoiExcelHelper implements ExcelHelper<HSSFWorkbook> {

    @Override
    public <D> HSSFWorkbook buildExcel(Collection<D> data) {
        if (CollectionUtils.isEmpty(data)) return new HSSFWorkbook();
        HSSFWorkbook workbook = new HSSFWorkbook();
        fillWorkbook(data, workbook);
        return workbook;
    }

    @Override
    public <D> List<D> readExcel(HSSFWorkbook workbook, Class<D> klass) {
        if (Objects.isNull(workbook)) return Collections.emptyList();
        return readWorkbook(workbook, klass);
    }
}
