package net.yeah.waitlight.commons.tools.core.excel.anno;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ExcelColumn {
    /**
     * Corresponding title
     */
    String title() default "";

    /**
     * Column index
     * <p>
     * Takes precedence over the {@link #title()}
     */
    int order() default -1;

    boolean canImport() default true;

    boolean canExport() default true;
}