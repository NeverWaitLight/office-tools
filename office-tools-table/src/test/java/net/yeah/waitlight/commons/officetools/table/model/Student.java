package net.yeah.waitlight.commons.officetools.table.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.yeah.waitlight.commons.officetools.table.excel.ExcelColumn;

@Setter
@Getter
@Accessors(chain = true)
public class Student {
    @ExcelColumn(title = "编号")
    private Integer no;

    @ExcelColumn(title = "名字")
    private String name;

    @ExcelColumn(title = "费用")
    private Double fee;

    @ExcelColumn(title = "性别")
    private Gender gender;

    @ExcelColumn(title = "是否入学")
    private Boolean checkin;
}