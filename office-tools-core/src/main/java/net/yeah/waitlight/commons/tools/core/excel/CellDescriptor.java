package net.yeah.waitlight.commons.tools.core.excel;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.yeah.waitlight.commons.tools.core.reflection.FieldDescriptor;

import java.util.Objects;
import java.util.function.Supplier;

@Getter
@Setter
@Accessors(chain = true)
public class CellDescriptor {
    private int cellnum;
    private FieldDescriptor fdp;
    private Object obj;
    private Supplier<?> valueSupplier;
}
