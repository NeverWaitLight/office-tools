package net.yeah.waitlight.commons.tools.core.excel.impl;

import com.github.javafaker.Faker;
import net.yeah.waitlight.commons.tools.core.excel.ExcelHelper;
import net.yeah.waitlight.commons.tools.core.excel.model.Student;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PoiExcelXSSFHelperTest {
    public static final int TOTAL = 65536;
    public static final String EXCEL_PATH = "xssf-test.xlsx";

    private final ExcelHelper<XSSFWorkbook> helper = new PoiExcelXSSFHelper();

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
    public void write() throws FileNotFoundException {
        if (Objects.isNull(workbook)) return;
        FileOutputStream fileOut = new FileOutputStream(EXCEL_PATH);
        try {
            workbook.write(fileOut);
            fileOut.flush();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            try {
                fileOut.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}