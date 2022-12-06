package net.yeah.waitlight.commons.tools.core.excel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Collection;

@Setter
@Getter
@NoArgsConstructor
public class SheetDescriptor {
    private Sheet sheet;
    private Collection<Object> data;

    public SheetDescriptor(Sheet sheet, Collection<Object> data) {
        this.sheet = sheet;
        this.data = data;
    }
}