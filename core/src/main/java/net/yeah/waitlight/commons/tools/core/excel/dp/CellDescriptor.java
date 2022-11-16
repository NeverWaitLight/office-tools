package net.yeah.waitlight.commons.tools.core.excel.dp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class CellDescriptor {
    private int order;
    private String title;

    private Field field;
    private Method getter;
    private Method setter;
}