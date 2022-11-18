package net.yeah.waitlight.commons.tools.core.excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SheetDescriptor<T> {
    private RowDescriptor<String> titleRow;
    private List<RowDescriptor<T>> dataRows;
}