package net.yeah.waitlight.commons.officetools.doc;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class DocServiceTest {

    private final DocService docService = new DocService();

    @Test
    public void template() throws IOException {
        String path = Objects.requireNonNull(this.getClass().getResource("/")).getPath();
        docService.template(new FileInputStream(path + "test.docx"), new FileOutputStream(path + "out.docx"), null);
    }

}