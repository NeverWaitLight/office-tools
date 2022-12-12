package net.yeah.waitlight.commons.officetools.doc;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@Slf4j
public class DocService {

    public void template(InputStream inputStream, OutputStream outputStream, Map<String, Object> data) throws IOException {
        try (XWPFDocument document = new XWPFDocument(inputStream)) {
            readHeader(document);
            readParagraph(document);
            readTable(document);
            readFooter(document);
            document.write(outputStream);
        }
    }

    public void read(InputStream inputStream, Class<?> dataType) throws IOException {
        try (XWPFDocument document = new XWPFDocument(inputStream)) {
            readHeader(document);
            readParagraph(document);
            readTable(document);
            readFooter(document);
        }
    }

    private void readHeader(XWPFDocument document) {
        List<XWPFHeader> headers = document.getHeaderList();
        for (int i = 0; i < headers.size(); i++) {
            XWPFHeader header = headers.get(i);
            log.info("Header {} --> {}", i, header.getText());
        }
    }

    private void readFooter(XWPFDocument document) {
        List<XWPFFooter> footers = document.getFooterList();
        for (int i = 0; i < footers.size(); i++) {
            XWPFFooter footer = footers.get(i);
            log.info("Footer {} --> {}", i, footer.getText());
        }
    }

    private void readTable(XWPFDocument document) {
        List<XWPFTable> tables = document.getTables();
        for (int i = 0; i < tables.size(); i++) {
            log.info("Table {} --> {}", i, tables.get(i).getText());
        }
    }

    private void readParagraph(XWPFDocument document) {
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        for (int i = 0; i < paragraphs.size(); i++) {
            XWPFParagraph paragraph = paragraphs.get(i);
            log.info("Paragraph {} --> {}", i, paragraph.getText());
            List<XWPFRun> runs = paragraph.getRuns();
            for (int j = 0; j < runs.size(); j++) {
                XWPFRun run = runs.get(j);
                String text = run.text().trim();
                log.info("Run {} --> {}", j, text);
                if (text.contains("{{")) {
                    run.setText("12312321",0);
                }
            }
        }
    }

}
