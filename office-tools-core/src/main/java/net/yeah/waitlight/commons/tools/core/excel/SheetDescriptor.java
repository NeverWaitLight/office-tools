package net.yeah.waitlight.commons.tools.core.excel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.function.Function;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SheetDescriptor<T> {
    private Function<T, RowDescriptor> titleFunction;
    private Collection<T> data;
}