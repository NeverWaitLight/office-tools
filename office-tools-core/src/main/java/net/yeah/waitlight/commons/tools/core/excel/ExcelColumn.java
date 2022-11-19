package net.yeah.waitlight.commons.tools.core.excel;

import net.yeah.waitlight.commons.tools.core.converter.ConversionService;
import net.yeah.waitlight.commons.tools.core.converter.DefaultConvertService;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ExcelColumn {
    String title() default "";

    int order() default -1;

    boolean canImport() default true;

    boolean canExport() default true;

    Class<? extends ConversionService> conversionService() default DefaultConvertService.class;

    /**
     * 转换工具
     */
    Convert[] converts() default {};
}