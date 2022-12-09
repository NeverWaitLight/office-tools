package net.yeah.waitlight.commons.officetools.table.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.yeah.waitlight.commons.officetools.table.TableColumn;

@Setter
@Getter
@Accessors(chain = true)
public class Student {
    @TableColumn(title = "编号")
    private Integer no;

    @TableColumn(title = "名字")
    private String name;

    @TableColumn(title = "费用")
    private Double fee;

    @TableColumn(title = "性别")
    private Gender gender;

    @TableColumn(title = "是否入学")
    private Boolean checkin;
}