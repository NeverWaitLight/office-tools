package net.yeah.waitlight.commons.tools.core.excel.impl;

import com.github.javafaker.Faker;
import net.yeah.waitlight.commons.tools.core.excel.ExcelHelper;
import net.yeah.waitlight.commons.tools.core.excel.model.Student;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PoiExcelHSSFHelperTest {
    public static final int TOTAL = 65536;
    public static final String EXCEL_PATH = "hssf-test.xls";

    private final ExcelHelper<HSSFWorkbook> helper = new PoiExcelHSSFHelper();

    private List<Student> students = new ArrayList<>();

    private Workbook workbook = null;

    @Test
    public void buildExcel() {
        workbook = helper.buildExcel(students);
        Assert.assertNotNull(workbook);
    }

    @Before
    public void prepareData() {
        final List<Student> r = new ArrayList<>(TOTAL);
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
            r.add(student);
        }

        students = r;
    }

    @After
    public void write() throws Exception {
        if (Objects.isNull(workbook)) return;
        try (FileOutputStream fileOut = new FileOutputStream(EXCEL_PATH)) {
            workbook.write(fileOut);
            fileOut.flush();
        }
    }


}