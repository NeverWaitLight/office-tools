package net.yeah.waitlight.commons.tools.core.excel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.yeah.waitlight.commons.tools.core.reflection.FieldDescriptor;

import java.util.function.Supplier;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CellDescriptor {
    private int cellnum;
    private FieldDescriptor fdp;
    private Object obj;
    private Supplier<?> valueSupplier;
}
