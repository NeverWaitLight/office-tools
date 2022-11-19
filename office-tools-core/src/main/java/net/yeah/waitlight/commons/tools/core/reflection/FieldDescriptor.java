package net.yeah.waitlight.commons.tools.core.reflection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class FieldDescriptor {
    private Field field;
    private Method getter;
    private Method setter;

    public <T extends Annotation> T getAnnotation(Class<T> type) {
        return field.getAnnotation(type);
    }
}