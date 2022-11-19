package net.yeah.waitlight.commons.tools.core.excel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RowDescriptor {
    private Object obj;
    private List<CellDescriptor> cellDescriptors;
}