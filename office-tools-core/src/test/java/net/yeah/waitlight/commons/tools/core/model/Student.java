package net.yeah.waitlight.commons.tools.core.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.yeah.waitlight.commons.tools.core.converter.Boolean2StringService;
import net.yeah.waitlight.commons.tools.core.excel.ExcelColumn;

@Setter
@Getter
@Accessors(chain = true)
public class Student {
    @ExcelColumn(title = "编号")
    private Integer no;

    @ExcelColumn(title = "名字")
    private String name;

    @ExcelColumn(title = "年龄")
    private Integer age;

    @ExcelColumn(title = "居住地址")
    private String address;

    @ExcelColumn(title = "费用")
    private Double fee;

    @ExcelColumn(title = "性别")
    private Gender gender;

    @ExcelColumn(title = "是否入学", conversionService = Boolean2StringService.class)
    private Boolean checkin;
}