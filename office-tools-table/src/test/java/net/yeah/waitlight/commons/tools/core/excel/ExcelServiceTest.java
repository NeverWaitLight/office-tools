package net.yeah.waitlight.commons.tools.core.excel;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import net.yeah.waitlight.commons.officetools.table.ExcelService;
import net.yeah.waitlight.commons.officetools.table.XLS;
import net.yeah.waitlight.commons.tools.core.model.Gender;
import net.yeah.waitlight.commons.tools.core.model.Student;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class ExcelServiceTest {

    public static final int COUNT = XLS.MAX_ROW_NUM / 100;

    public static final String OUT_PUT_PATH = "target/hssf-test.xls";

    private final ExcelService excelService = new ExcelService();

    private List<Object> students = new ArrayList<>();

    private Workbook workbook = null;

    @Test
    public void build() {
        workbook = excelService.build(students);
        Assert.assertNotNull(workbook);
    }

    @Test
    public void reade() throws FileNotFoundException {
        List<Student> data = excelService.read(new FileInputStream(OUT_PUT_PATH), Student.class);
        Assert.assertEquals(COUNT, data.size());
    }

    @Before
    public void prepareData() {
        final List<Object> r = new ArrayList<>(COUNT);
        final Faker faker = new Faker();

        for (int i = 0; i < COUNT; i++) {
            int no = faker.number().numberBetween(100000, 200000);
            String name = faker.name().fullName();
            double fee = faker.number().randomDouble(3, 100, 200);
            Gender gender = Gender.values()[faker.random().nextInt(0, 1)];
            Boolean checkin = 1 == faker.random().nextInt(0, 1);

            Student student = new Student()
                    .setNo(no)
                    .setName(name)
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
        try (FileOutputStream fileOut = new FileOutputStream(OUT_PUT_PATH)) {
            workbook.write(fileOut);
            fileOut.flush();
        }
    }

}