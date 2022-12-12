package net.yeah.waitlight.commons.officetools.pdf;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class PdfServiceTest {
    private final PdfService pdfService = new PdfService();
    String path = Objects.requireNonNull(this.getClass().getResource("/")).getPath();

    @Test
    public void template() throws IOException {
        pdfService.template(
                new FileInputStream(path + "test.pdf"),
                new FileOutputStream(path + "test.pdf"),
                Map.of("name", "tom", "age", 1)
        );
    }
}