package net.yeah.waitlight.commons.tools.core.excel.impl;

import net.yeah.waitlight.commons.tools.core.excel.anno.ExcelColumn;
import net.yeah.waitlight.commons.tools.core.excel.dp.CellDescriptor;
import net.yeah.waitlight.commons.tools.core.excel.dp.RowDescriptor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public abstract class AbstractPoiExcelHelper {
    private static final ConcurrentHashMap<Class<?>, List<CellDescriptor>> DP_CACHE = new ConcurrentHashMap<>();

    protected <D> RowDescriptor<D> map2Dp(D datum) {
        Class<?> klass = datum.getClass();
        List<CellDescriptor> cellDescriptors = DP_CACHE.computeIfAbsent(klass, k -> Arrays.stream(klass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(ExcelColumn.class))
                .map(field -> {
                    ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);
                    if (!excelColumn.canImport()) return null;
                    return new CellDescriptor().setOrder(excelColumn.order())
                            .setTitle(excelColumn.title())
                            .setField(field)
                            .setSetter(takeSetter(klass, field))
                            .setGetter(takeGetter(klass, field));
                }).collect(Collectors.toList()));
        return new RowDescriptor<>(datum, cellDescriptors);
    }

    protected <K> Method takeGetter(Class<K> klass, Field field) {
        return takeMethod("get", klass, field);
    }

    protected <K> Method takeSetter(Class<K> klass, Field field) {
        return takeMethod("set", klass, field);
    }

    protected <K> Method takeMethod(String prefix, Class<K> klass, Field field) {
        String name = field.getName();
        String setter = prefix + name.substring(0, 1).toUpperCase() + name.substring(1);
        return Arrays.stream(klass.getMethods()).filter(m -> setter.equals(m.getName())).findFirst().orElseThrow(() -> new RuntimeException(String.format("Not found %s method", setter)));
    }

    protected <D> void fillWorkbook(Workbook workbook, Collection<D> data) {
        fillSheet(workbook.createSheet(), data);
    }

    protected <D> void fillSheet(Sheet sheet, Collection<D> data) {
        int rowNumber = 0;
        for (D datum : data) {
            fillRow(sheet.createRow(rowNumber), map2Dp(datum));
            rowNumber++;
        }
    }

    protected <D> void fillRow(Row row, RowDescriptor<D> rowDescriptor) {
        int cellNumber = 0;
        for (CellDescriptor cellDescriptor : rowDescriptor.getCellDescriptors()) {
            fillCell(row.createCell(cellNumber), rowDescriptor.getObj(), cellDescriptor);
            cellNumber++;
        }
    }

    protected <D> void fillCell(Cell cell, D obj, CellDescriptor cellDescriptor) {
        try {
            Object invoke = cellDescriptor.getGetter().invoke(obj);
            cell.setCellValue(String.valueOf(invoke));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
