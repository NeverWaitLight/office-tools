package net.yeah.waitlight.commons.tools.core.excel;

import java.util.Collection;
import java.util.List;

public interface ExcelHelper<E> {
    <D> E buildExcel(Collection<D> data);

    <D> List<D> readExcel(E e, Class<D> klass);
}
