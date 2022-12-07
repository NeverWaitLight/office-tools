package net.yeah.waitlight.commons.officetools.table.excel;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ExcelColumn {
    String title() default "";

    int order() default -1;
}