package net.yeah.waitlight.commons.officetools.table.csv;

import net.yeah.waitlight.commons.officetools.table.TableService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

public class CsvService implements TableService<CSVPrinter> {

    public static void main(String[] args) {
        Map<String, String> AUTHOR_BOOK_MAP = Map.of(
                "Dan Simmons", "Hyperion",
                "Douglas Adams", "The Hitchhiker's Guide to the Galaxy"
        );
        String[] HEADERS = {"author", "title"};

        try (
                FileWriter out = new FileWriter("book_new.csv");
                CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(HEADERS))
        ) {
            AUTHOR_BOOK_MAP.forEach((author, title) -> {
                try {
                    printer.printRecord(author, title);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public CSVPrinter build(Collection<Object> data) {
        return null;
    }

    @Override
    public Future<CSVPrinter> buildAsync(Collection<Object> data) {
        return null;
    }

    @Override
    public <D> List<D> read(InputStream inputStream, Class<D> klass) {
        return null;
    }
}
