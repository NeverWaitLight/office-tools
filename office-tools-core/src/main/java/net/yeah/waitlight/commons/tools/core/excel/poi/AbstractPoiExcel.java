package net.yeah.waitlight.commons.tools.core.excel.poi;

import lombok.extern.slf4j.Slf4j;
import net.yeah.waitlight.commons.tools.core.excel.CellDescriptor;
import net.yeah.waitlight.commons.tools.core.excel.ExcelColumn;
import net.yeah.waitlight.commons.tools.core.excel.RowDescriptor;
import net.yeah.waitlight.commons.tools.core.excel.SheetDescriptor;
import net.yeah.waitlight.commons.tools.core.reflection.ReflectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public abstract class AbstractPoiExcel {

    private static final ConcurrentMap<Class<?>, List<CellDescriptor>> CDP_CACHE = new ConcurrentHashMap<>();

    protected void fillWorkbook(Map<String, Collection<Object>> allData, Workbook workbook) {
        if (MapUtils.isEmpty(allData)) return;
        Objects.requireNonNull(workbook);
        allData.forEach((sheetName, dataOfSheet) -> {
            Sheet sheet = workbook.createSheet(sheetName);
            fillSheet(new SheetDescriptor(sheet, dataOfSheet));
        });
    }

    protected void fillSheet(SheetDescriptor sheetDescriptor) {
        Objects.requireNonNull(sheetDescriptor);
        final Sheet sheet = sheetDescriptor.getSheet();

        sheetDescriptor.getData()
                .stream()
                .findAny()
                .ifPresent(anyDatum -> {
                    RowDescriptor rowDescriptor = resolveTitleRow(sheet.createRow(0), anyDatum.getClass());
                    fillRow(rowDescriptor);
                });

        int rownum = 1;
        for (Object datum : sheetDescriptor.getData()) {
            fillRow(resolveDataRow(sheet.createRow(rownum++), datum));
        }
    }

    protected RowDescriptor resolveTitleRow(Row row, Class<?> klass) {
        List<CellDescriptor> cellDescriptors = ReflectionUtils.getAnnotations(klass, ExcelColumn.class)
                .stream()
                .map(anno -> (ExcelColumn) anno)
                .sorted(Comparator.comparingInt(ExcelColumn::order))
                .map(ExcelColumn::title)
                .map(title -> new CellDescriptor(null, title))
                .toList();
        return new RowDescriptor(null, row, cellDescriptors);
    }

    protected RowDescriptor resolveDataRow(Row row, Object datum) {
        Objects.requireNonNull(datum);

        List<CellDescriptor> cellDescriptors = ReflectionUtils.getFieldDescriptors4Excel(datum)
                .stream()
                .map(fdp -> {
                    CellDescriptor cellDescriptor = new CellDescriptor();
                    cellDescriptor.setField(fdp.getField())
                            .setGetter(fdp.getGetter())
                            .setSetter(fdp.getSetter());
                    cellDescriptor.setValue(ReflectionUtils.getValue(fdp.getGetter(), datum));
                    return cellDescriptor;
                })
                .toList();
        return new RowDescriptor(datum, row, cellDescriptors);
    }

    protected void fillRow(RowDescriptor rowDescriptor) {
        Objects.requireNonNull(rowDescriptor);
        final Row row = rowDescriptor.getRow();

        int cellnum = 0;
        for (CellDescriptor cellDescriptor : rowDescriptor.getCellDescriptors()) {
            cellDescriptor.setCell(row.createCell(cellnum++));
            fillCell(cellDescriptor);
        }
    }

    protected void fillCell(CellDescriptor cellDescriptor) {
        Objects.requireNonNull(cellDescriptor);
        final Cell cell = cellDescriptor.getCell();

        Object value = cellDescriptor.getValue();
        cell.setCellValue(String.valueOf(value));
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
