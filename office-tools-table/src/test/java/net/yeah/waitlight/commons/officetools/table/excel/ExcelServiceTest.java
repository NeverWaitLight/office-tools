package net.yeah.waitlight.commons.officetools.table.excel;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import net.yeah.waitlight.commons.officetools.common.convert.ConversionService;
import net.yeah.waitlight.commons.officetools.table.model.Gender;
import net.yeah.waitlight.commons.officetools.table.model.Student;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ExcelServiceTest {

    public static final int COUNT = XLS.MAX_ROW_NUM / 100;

    public static final String OUT_PUT_PATH = "target/hssf-test.xls";

    private final ExcelService excelService = new ExcelService(new ConversionService());

    @Test
    public void write() throws IOException {
        try (FileOutputStream out = new FileOutputStream(OUT_PUT_PATH)) {
            excelService.write(prepareData(), out);
        }
    }

    @Test
    public void reade() throws IOException {
        write();
        try (FileInputStream fileInputStream = new FileInputStream(OUT_PUT_PATH)) {
            List<Student> data = excelService.read(fileInputStream, Student.class);
            Assert.assertEquals(COUNT, data.size());
        }
    }

    public List<Object> prepareData() {
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

        return r;
    }

}