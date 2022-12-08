package net.yeah.waitlight.commons.officetools.table.excel.poi;

import lombok.extern.slf4j.Slf4j;
import net.yeah.waitlight.commons.officetools.common.convert.ConversionService;
import net.yeah.waitlight.commons.officetools.common.reflection.FieldDescriptor;
import net.yeah.waitlight.commons.officetools.common.reflection.ReflectionUtils;
import net.yeah.waitlight.commons.officetools.table.excel.CellDescriptor;
import net.yeah.waitlight.commons.officetools.table.excel.ExcelColumn;
import net.yeah.waitlight.commons.officetools.table.excel.RowDescriptor;
import net.yeah.waitlight.commons.officetools.table.excel.SheetDescriptor;
import org.apache.commons.collections4.MapUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.*;

@Slf4j
public abstract class AbstractPoiExcel {

    private final ConversionService conversionService = new ConversionService();

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
                .map(ExcelColumn.class::cast)
                .sorted(Comparator.comparingInt(ExcelColumn::order))
                .map(ExcelColumn::title)
                .map(title -> new CellDescriptor(null, title))
                .toList();
        return new RowDescriptor(row, cellDescriptors);
    }

    protected RowDescriptor resolveDataRow(Row row, Object datum) {
        Objects.requireNonNull(datum);

        List<CellDescriptor> cellDescriptors = ReflectionUtils.getFieldDescriptors4Excel(datum.getClass(), ExcelColumn.class,
                        field -> field.getAnnotation(ExcelColumn.class).order())
                .stream()
                .map(fdp -> {
                    CellDescriptor cellDescriptor = new CellDescriptor();
                    cellDescriptor.setField(fdp.getField())
                            .setGetter(fdp.getGetter())
                            .setSetter(fdp.getSetter());
                    cellDescriptor.setValue(ReflectionUtils.invoke(datum, fdp.getGetter()));
                    return cellDescriptor;
                })
                .toList();
        return new RowDescriptor(row, cellDescriptors);
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

        final List<D> data = new ArrayList<>();
        for (int sheetnum = 0; sheetnum < workbook.getNumberOfSheets(); sheetnum++) {
            Sheet sheet = workbook.getSheetAt(sheetnum);
            data.addAll(readSheet(sheet, klass));
        }
        return data;
    }

    protected <D> List<D> readSheet(Sheet sheet, Class<D> klass) {
        final List<D> data = new ArrayList<>();
        for (int rownum = 1; rownum <= sheet.getLastRowNum(); rownum++) {
            Row row = sheet.getRow(rownum);
            data.add(readRow(row, klass));
        }
        return data;
    }

    protected <D> D readRow(Row row, Class<D> klass) {
        D datum = ReflectionUtils.getInstance(klass);
        List<FieldDescriptor> fieldDescriptors = ReflectionUtils.getFieldDescriptors4Excel(datum.getClass(), ExcelColumn.class,
                field -> field.getAnnotation(ExcelColumn.class).order());
        for (int cellnum = 0; cellnum < row.getLastCellNum(); cellnum++) {
            Cell cell = row.getCell(cellnum);
            FieldDescriptor fieldDescriptor = fieldDescriptors.get(cellnum);
            Object value = readCell(cell);
            value = conversionService.convert(value, fieldDescriptor.getType());
            try {
                ReflectionUtils.invoke(datum, fieldDescriptor.getSetter(), value);
            } catch (Exception e) {
                log.error("Cast {} to {} has error", value.getClass().getName(), fieldDescriptor.getType().getName(), e);
            }
        }
        return datum;
    }

    protected Object readCell(Cell cell) {
        Objects.requireNonNull(cell);
        return switch (cell.getCellType()) {
            case BOOLEAN -> cell.getBooleanCellValue();
            case NUMERIC -> cell.getNumericCellValue();
            case STRING -> cell.getStringCellValue();
            default -> null;
        };
    }


}
