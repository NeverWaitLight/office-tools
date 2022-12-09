package net.yeah.waitlight.commons.officetools.table;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface TableColumn {
    String title() default "";

    int order() default -1;
}