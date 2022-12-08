package net.yeah.waitlight.commons.officetools.table.csv;

import com.github.javafaker.Faker;
import net.yeah.waitlight.commons.officetools.table.excel.XLS;
import net.yeah.waitlight.commons.officetools.table.model.Gender;
import net.yeah.waitlight.commons.officetools.table.model.Student;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvServiceTest {

    public static final int COUNT = XLS.MAX_ROW_NUM;

    public static final String OUT_PUT_PATH = "target/test.csv";

    private final CsvService csvService = new CsvService();
    private List<Object> students = new ArrayList<>();

    @Test
    public void build() {
        try (FileOutputStream fileOutputStream = new FileOutputStream(OUT_PUT_PATH)) {
            csvService.build(students, fileOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
//        if (Objects.isNull(workbook)) return;
//        try (FileOutputStream fileOut = new FileOutputStream(OUT_PUT_PATH)) {
//            workbook.write(fileOut);
//            fileOut.flush();
//        }
    }

}