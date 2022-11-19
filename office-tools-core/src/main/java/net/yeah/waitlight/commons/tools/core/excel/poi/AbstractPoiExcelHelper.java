package net.yeah.waitlight.commons.tools.core.excel.poi;

import lombok.extern.slf4j.Slf4j;
import net.yeah.waitlight.commons.tools.core.converter.ConversionService;
import net.yeah.waitlight.commons.tools.core.excel.CellDescriptor;
import net.yeah.waitlight.commons.tools.core.excel.ExcelColumn;
import net.yeah.waitlight.commons.tools.core.excel.RowDescriptor;
import net.yeah.waitlight.commons.tools.core.excel.SheetDescriptor;
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
import java.util.function.Supplier;

@Slf4j
public abstract class AbstractPoiExcelHelper {

    private static final ConcurrentMap<Class<?>, List<CellDescriptor>> CDP_CACHE = new ConcurrentHashMap<>();

    protected <T> List<CellDescriptor> datum2CellDescriptor(T datum) {
        Objects.requireNonNull(datum);

        return CDP_CACHE.computeIfAbsent(
                datum.getClass(),
                key -> ReflectionUtils.getFieldDescriptors(datum, ExcelColumn.class)
                        .stream()
                        .map(fdp -> {
                            ExcelColumn anno = fdp.getAnnotation(ExcelColumn.class);
                            return new CellDescriptor().setCellnum(anno.order()).setFdp(fdp);
                        })
                        .sorted(Comparator.comparingInt(CellDescriptor::getCellnum))
                        .toList()
        );
    }

    protected <T> void fillWorkbook(Collection<T> data, Workbook workbook) {
        Objects.requireNonNull(workbook);
        Objects.requireNonNull(data);

        T d = data.stream().findFirst().orElseThrow(RuntimeException::new);
        List<CellDescriptor> titleCellDescriptors = datum2CellDescriptor(d).stream()
                .peek(cdp -> cdp.setValueSupplier(() -> {
                    ExcelColumn excelColumn = cdp.getFdp().getAnnotation(ExcelColumn.class);
                    return excelColumn.title();
                }))
                .toList();
        RowDescriptor<String> titleRowDescriptor = new RowDescriptor<String>(null, titleCellDescriptors);
        SheetDescriptor<T> sheetDescriptor = new SheetDescriptor<T>(titleRowDescriptor, data);

        fillSheet(sheetDescriptor, workbook.createSheet());
    }

    protected <T> void fillSheet(SheetDescriptor<T> sheetDescriptor, Sheet sheet) {
        fillRow(sheetDescriptor.getTitleRow(), sheet.createRow(0));
        int rownum = 1;
        for (T datum : sheetDescriptor.getData()) {
            List<CellDescriptor> cellDescriptors = datum2CellDescriptor(datum);
            fillRow(new RowDescriptor<>(datum, cellDescriptors), sheet.createRow(rownum++));
        }
    }

    protected <T> void fillRow(RowDescriptor<T> rowDescriptor, Row row) {
        int cellnum = 0;
        T obj = rowDescriptor.getObj();
        for (CellDescriptor cdp : rowDescriptor.getCellDescriptors()) {
            if (Objects.nonNull(rowDescriptor.getObj())) {
                cdp.setValueSupplier(() -> ReflectionUtils.getValue(obj, cdp.getFdp().getGetter()));
            }
            fillCell(cdp, row.createCell(cellnum++));
        }
    }

    protected void fillCell(CellDescriptor cellDescriptor, Cell cell) {
        Supplier<?> supplier = cellDescriptor.getValueSupplier();
        if (Objects.isNull(supplier)) return;

        Object value = supplier.get();
        FieldDescriptor fdp = cellDescriptor.getFdp();
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
