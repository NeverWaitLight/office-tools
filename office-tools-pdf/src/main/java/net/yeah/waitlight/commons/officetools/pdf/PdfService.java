package net.yeah.waitlight.commons.officetools.pdf;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class PdfService {
    public void template(InputStream inputStream, OutputStream outputStream, Map<String, Object> data) throws IOException {
        PdfReader reader = new PdfReader(inputStream);
        PdfDocument pdf = new PdfDocument(reader);
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, true);
        form.getFormFields().forEach((key, value) -> {
            Object o = data.get(key);
            if (Objects.nonNull(o)) {
                value.setValue(String.valueOf(o));
            }
        });
        form.flattenFields();

        pdf.close();
        reader.close();
    }
}
