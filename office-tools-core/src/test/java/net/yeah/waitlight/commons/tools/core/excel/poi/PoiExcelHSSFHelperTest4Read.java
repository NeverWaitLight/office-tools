package net.yeah.waitlight.commons.tools.core.excel.poi;

import com.github.javafaker.Faker;
import net.yeah.waitlight.commons.tools.core.excel.ExcelHelper;
import net.yeah.waitlight.commons.tools.core.model.Student;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PoiExcelHSSFHelperTest4Read {
    public static final int TOTAL = 65536;

    private final ExcelHelper<HSSFWorkbook> helper = new PoiExcelHSSFHelper();

    HSSFWorkbook workbook = null;

    @Test
    public void read() {
        Assert.assertNotNull(workbook);
        helper.readExcel(workbook, Student.class);
    }

    @Before
    public void prepareData() {
        final List<Student> students = new ArrayList<>(TOTAL);
        final Faker faker = new Faker();

        for (int i = 0; i < TOTAL; i++) {
            int no = faker.number().numberBetween(100000, 200000);
            String name = faker.name().fullName();
            int age = faker.number().numberBetween(10, 15);
            String address = faker.address().fullAddress();
            double fee = faker.number().randomDouble(3, 100, 200);

            Student student = new Student()
                    .setNo(no)
                    .setName(name)
                    .setAge(age)
                    .setAddress(address)
                    .setFee(fee);
            students.add(student);
        }

        workbook = helper.buildExcel(students);
    }


}