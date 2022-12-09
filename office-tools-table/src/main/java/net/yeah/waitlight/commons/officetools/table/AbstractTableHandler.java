package net.yeah.waitlight.commons.officetools.table;

import net.yeah.waitlight.commons.officetools.common.reflection.ReflectionUtils;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public abstract class AbstractTableHandler {
    protected RowDescriptor resolveTitleRow(Collection<Object> data) {
        return resolveTitleRow(getAnyDatum(data));
    }

    protected RowDescriptor resolveTitleRow(Object obj) {
        Objects.requireNonNull(obj);
        return resolveTitleRow(obj.getClass());
    }

    protected RowDescriptor resolveTitleRow(Class<?> klass) {
        List<CellDescriptor> cellDescriptors = ReflectionUtils.getAnnotations(klass, TableColumn.class)
                .stream()
                .map(TableColumn.class::cast)
                .sorted(Comparator.comparingInt(TableColumn::order))
                .map(TableColumn::title)
                .map(CellDescriptor::new)
                .toList();
        return new RowDescriptor(cellDescriptors);
    }

    protected RowDescriptor resolveDataRow(Object datum) {
        Objects.requireNonNull(datum);

        List<CellDescriptor> cellDescriptors = ReflectionUtils.getFieldDescriptors4Excel(datum.getClass(), TableColumn.class,
                        field -> field.getAnnotation(TableColumn.class).order())
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
        return data.stream().findAny().orElseThrow(() -> new RuntimeException("Data is empty"));
    }
}
