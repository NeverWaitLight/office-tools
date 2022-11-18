package net.yeah.waitlight.commons.tools.core.excel.poi;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import net.yeah.waitlight.commons.tools.core.excel.ExcelHelper;
import net.yeah.waitlight.commons.tools.core.model.Gender;
import net.yeah.waitlight.commons.tools.core.model.Student;
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

@Slf4j
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
            Gender gender = Gender.values()[faker.random().nextInt(0, 1)];
            Boolean checkin = 1 == faker.random().nextInt(0, 1);

            Student student = new Student()
                    .setNo(no)
                    .setName(name)
                    .setAge(age)
                    .setAddress(address)
                    .setFee(fee)
                    .setGender(gender)
                    .setCheckin(checkin);
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