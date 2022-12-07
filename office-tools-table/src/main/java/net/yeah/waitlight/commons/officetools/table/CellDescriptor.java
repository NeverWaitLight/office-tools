package net.yeah.waitlight.commons.officetools.table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.poi.ss.usermodel.Cell;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CellDescriptor {
    private Cell cell;
    private Field field;
    private Method getter;
    private Method setter;
    private Object value;

    public CellDescriptor(Cell cell, Object value) {
        this.cell = cell;
        this.value = value;
    }
}
