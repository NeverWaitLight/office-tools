package net.yeah.waitlight.commons.tools.core.excel;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RowDescriptor<T> {
    private T obj;
    private List<CellDescriptor> cellDescriptors;
}