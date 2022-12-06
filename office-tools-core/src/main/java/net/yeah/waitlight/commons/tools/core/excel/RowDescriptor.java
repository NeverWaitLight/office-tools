package net.yeah.waitlight.commons.tools.core.excel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class RowDescriptor {
    private Object obj;
    private Row row;
    private List<CellDescriptor> cellDescriptors;
}