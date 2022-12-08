package net.yeah.waitlight.commons.officetools.table;

import net.yeah.waitlight.commons.officetools.common.reflection.ReflectionUtils;
import net.yeah.waitlight.commons.officetools.table.excel.ExcelColumn;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public abstract class AbstractTableHelper {
    protected RowDescriptor resolveTitleRow(Class<?> klass) {
        List<CellDescriptor> cellDescriptors = ReflectionUtils.getAnnotations(klass, ExcelColumn.class)
                .stream()
                .map(ExcelColumn.class::cast)
                .sorted(Comparator.comparingInt(ExcelColumn::order))
                .map(ExcelColumn::title)
                .map(CellDescriptor::new)
                .toList();
        return new RowDescriptor(cellDescriptors);
    }

    protected RowDescriptor resolveDataRow(Object datum) {
        Objects.requireNonNull(datum);

        List<CellDescriptor> cellDescriptors = ReflectionUtils.getFieldDescriptors4Excel(datum.getClass(), ExcelColumn.class,
                        field -> field.getAnnotation(ExcelColumn.class).order())
                .stream()
                .map(fdp -> new CellDescriptor(datum,
                        fdp.getField(),
                        fdp.getGetter(),
                        fdp.getSetter(),
                        ReflectionUtils.invoke(datum, fdp.getGetter())
                ))
                .toList();
        return new RowDescriptor(cellDescriptors);
    }

    protected Object getAnyDatum(Collection<Object> data) {
        return data.stream().findAny().orElseThrow();
    }
}
