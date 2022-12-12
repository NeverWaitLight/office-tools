package net.yeah.waitlight.commons.officetools.pdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

@Slf4j
public class PdfService {
    public void template(InputStream inputStream, OutputStream outputStream, Map<String, Object> data) throws IOException {
        try {
            PdfReader pdfReader = new PdfReader(inputStream);
            PdfStamper pdfStamper = new PdfStamper(pdfReader, outputStream);
            AcroFields acroFields = pdfStamper.getAcroFields();
            for (String key : data.keySet()) {
                acroFields.setField(key, data.get(key).toString());
            }
            pdfStamper.setFormFlattening(true);
            pdfStamper.close();
            pdfReader.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
