package net.yeah.waitlight.commons.officetools.table.excel;

import lombok.extern.slf4j.Slf4j;
import net.yeah.waitlight.commons.officetools.common.convert.ConversionService;
import net.yeah.waitlight.commons.officetools.common.reflection.FieldDescriptor;
import net.yeah.waitlight.commons.officetools.common.reflection.ReflectionUtils;
import net.yeah.waitlight.commons.officetools.table.AbstractTableHandler;
import net.yeah.waitlight.commons.officetools.table.CellDescriptor;
import net.yeah.waitlight.commons.officetools.table.RowDescriptor;
import net.yeah.waitlight.commons.officetools.table.TableColumn;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.*;

@Slf4j
public abstract class AbstractPoiExcel extends AbstractTableHandler {

    private final ConversionService conversionService;

    public AbstractPoiExcel(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    protected void fillWorkbook(Collection<Object> data, Workbook workbook) {
        if (CollectionUtils.isEmpty(data)) return;
        Objects.requireNonNull(workbook);

        Sheet sheet = workbook.createSheet();
        fillSheet(sheet, data);
    }

    protected void fillSheet(Sheet sheet, Collection<Object> data) {
        Objects.requireNonNull(sheet);

        RowDescriptor rowDescriptor = resolveTitleRow(data);
        fillRow(sheet.createRow(0), rowDescriptor);

        int rownum = 1;
        for (Object datum : data) {
            fillRow(sheet.createRow(rownum++), resolveDataRow(datum));
        }
    }

    protected void fillRow(Row row, RowDescriptor rowDescriptor) {
        Objects.requireNonNull(rowDescriptor);

        int cellnum = 0;
        for (CellDescriptor cellDescriptor : rowDescriptor.cellDescriptors()) {
            Cell cell = row.createCell(cellnum++);
            fillCell(cell, cellDescriptor);
        }
    }

    protected void fillCell(Cell cell, CellDescriptor cellDescriptor) {
        Objects.requireNonNull(cellDescriptor);

        Object value = cellDescriptor.value();
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
        List<FieldDescriptor> fieldDescriptors = ReflectionUtils.getFieldDescriptors4Excel(datum.getClass(), TableColumn.class,
                field -> field.getAnnotation(TableColumn.class).order());
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
