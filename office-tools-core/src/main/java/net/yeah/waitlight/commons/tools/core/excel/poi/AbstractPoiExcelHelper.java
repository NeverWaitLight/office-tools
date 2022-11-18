package net.yeah.waitlight.commons.tools.core.excel.poi;

import lombok.extern.slf4j.Slf4j;
import net.yeah.waitlight.commons.tools.core.converter.ConversionService;
import net.yeah.waitlight.commons.tools.core.excel.CellDescriptor;
import net.yeah.waitlight.commons.tools.core.excel.ExcelColumn;
import net.yeah.waitlight.commons.tools.core.excel.RowDescriptor;
import net.yeah.waitlight.commons.tools.core.reflection.FieldDescriptor;
import net.yeah.waitlight.commons.tools.core.reflection.ReflectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public abstract class AbstractPoiExcelHelper {
    private static final ConcurrentMap<Class<?>, List<CellDescriptor>> CDP_CACHE = new ConcurrentHashMap<>();

    protected <T> List<CellDescriptor> map2Dp(T datum) {
        Objects.requireNonNull(datum);

        return CDP_CACHE.computeIfAbsent(
                datum.getClass(),
                key -> ReflectionUtils.getFieldDescriptors(datum, ExcelColumn.class)
                        .stream()
                        .map(fdp -> {
                            ExcelColumn anno = fdp.getAnnotation(ExcelColumn.class);
                            return new CellDescriptor()
                                    .setCellnum(anno.order())
                                    .setFdp(fdp);
                        })
                        .sorted(Comparator.comparingInt(CellDescriptor::getCellnum))
                        .toList()
        );
    }

    protected <D> void fillWorkbook(Collection<D> data, Workbook workbook) {
        Objects.requireNonNull(workbook);
        Objects.requireNonNull(data);
        fillSheet(data, workbook.createSheet());
    }

    protected <D> void fillSheet(Collection<D> data, Sheet sheet) {
        int rownum = 1;

        List<CellDescriptor> cellDescriptors;
        for (D datum : data) {
            cellDescriptors = map2Dp(datum);
            fillRow(new RowDescriptor<>(datum, cellDescriptors), sheet.createRow(rownum++));
        }
    }

    protected <D> void fillRow(RowDescriptor<D> rowDescriptor, Row row) {
        int cellnum = 0;
        for (CellDescriptor cdp : rowDescriptor.getCellDescriptors()) {
            fillCell(cdp, row.createCell(cellnum++));
        }
    }

    protected void fillCell(CellDescriptor cdp, Cell cell) {
        Object value = cdp.getValueSupplier().get();
        FieldDescriptor fdp = cdp.getFdp();
        ExcelColumn annotation = fdp.getAnnotation(ExcelColumn.class);
        Class<? extends ConversionService> aClass = annotation.conversionService();
        ConversionService conversionService = null;
        try {
            Constructor<? extends ConversionService> constructor = aClass.getConstructor();
            conversionService = constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Class<?> type = value.getClass();
        if (String.class.isAssignableFrom(type)) {
            cell.setCellValue(String.valueOf(value));
        }

        if (Number.class.isAssignableFrom(type)) {
            cell.setCellValue(((Number) value).doubleValue());
        }

        // todo: i18N
        if (Boolean.class.isAssignableFrom(type)) {
            Boolean v = (Boolean) value;
            Object convert = conversionService.convert(v);
            cell.setCellValue(convert.toString());
        }

        // todo: i18N
        if (Enum.class.isAssignableFrom(type)) {
            cell.setCellValue(((Enum<?>) value).name());
        }
    }

    protected <D> List<D> readWorkbook(Workbook workbook, Class<D> klass) {
        if (Objects.isNull(workbook) || Objects.isNull(klass)) return Collections.emptyList();

        int activeSheetIndex = workbook.getActiveSheetIndex();
        for (int sheetnum = 0; sheetnum <= activeSheetIndex; sheetnum++) {
            Sheet sheet = workbook.getSheetAt(sheetnum);
            readSheet(sheet, klass);
        }

        return Collections.emptyList();
    }

    protected <D> List<D> readSheet(Sheet sheet, Class<D> klass) {
        int lastRowNum = sheet.getLastRowNum();
        for (int rownum = 0; rownum <= lastRowNum; rownum++) {
            Row row = sheet.getRow(rownum);
            readRow(row, klass);
        }
        return Collections.emptyList();
    }

    protected <D> List<D> readRow(Row row, Class<D> klass) {
        int lastCellNum = row.getLastCellNum();
        for (int cellnum = 0; cellnum <= lastCellNum; cellnum++) {
            Cell cell = row.getCell(cellnum);
            readCell(cell);
        }
        return Collections.emptyList();
    }

    protected <F> F readCell(Cell cell) {
        if (Objects.isNull(cell)) {
            log.debug("Cell is empty!");
            return null;
        }
        switch (cell.getCellType()) {
            case BOOLEAN:
                break;
            case NUMERIC:
                break;
            case STRING:
                break;
            case BLANK:
                break;
            case ERROR:
                break;
            default:
                break;
        }
        return null;
    }


}