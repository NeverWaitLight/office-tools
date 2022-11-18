package net.yeah.waitlight.commons.tools.core.reflection;

import net.yeah.waitlight.commons.tools.core.excel.ExcelColumn;
import net.yeah.waitlight.commons.tools.core.model.Student;
import org.junit.Test;

import java.util.List;

public class ReflectionUtilsTest {

    @Test
    public void getFieldDescriptors() {
        Student student = new Student();
        List<FieldDescriptor> fieldDescriptors = ReflectionUtils.getFieldDescriptors(student, ExcelColumn.class);
    }
}