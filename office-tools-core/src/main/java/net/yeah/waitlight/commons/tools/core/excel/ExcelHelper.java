package net.yeah.waitlight.commons.tools.core.excel;

import net.yeah.waitlight.commons.tools.core.LanguageCountry;

import java.util.Collection;
import java.util.List;

public interface ExcelHelper<E> {
    default <D> E buildExcel(Collection<D> data) {
        return buildExcel(data, LanguageCountry.getSystemLanguageCountry());
    }

    <D> E buildExcel(Collection<D> data, LanguageCountry languageCountry);

    <D> List<D> readExcel(E e, Class<D> klass);
}
