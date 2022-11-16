package net.yeah.waitlight.commons.tools.core.excel.model;

import lombok.Data;
import lombok.experimental.Accessors;
import net.yeah.waitlight.commons.tools.core.excel.anno.ExcelColumn;

@Data
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
}