package net.yeah.waitlight.commons.tools.core.excel.dp;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RowDescriptor<O> {
    private O obj;
    private List<CellDescriptor> cellDescriptors;
}