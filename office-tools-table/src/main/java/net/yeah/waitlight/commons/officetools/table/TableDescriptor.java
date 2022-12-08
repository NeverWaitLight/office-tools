package net.yeah.waitlight.commons.officetools.table;

import java.util.List;

public record TableDescriptor(
        List<RowDescriptor> rowDescriptors
) {
}