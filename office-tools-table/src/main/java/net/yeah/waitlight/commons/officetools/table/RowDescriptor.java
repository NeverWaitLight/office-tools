package net.yeah.waitlight.commons.officetools.table;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public record RowDescriptor(
        List<CellDescriptor> cellDescriptors
) {
    public <R> List<R> getValues(Function<Object, R> func) {
        if (Objects.isNull(cellDescriptors) || cellDescriptors.isEmpty()) {
            return Collections.emptyList();
        }
        Objects.requireNonNull(func, "Value function must not be null");

        return cellDescriptors()
                .stream()
                .map(cellDescriptor -> func.apply(cellDescriptor.value()))
                .toList();
    }
}