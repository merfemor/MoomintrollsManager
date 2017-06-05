package ru.ifmo.cs.korm.mapping;

import ru.ifmo.cs.korm.annotations.Attribute;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MappingAttribute {
    public final String oName, tName;
    public final boolean isAutoGenerated, isId;
    public final Method getter, setter;
    public final Class oClass;

    MappingAttribute(Field field) throws NoSuchMethodException, IllegalArgumentException {
        Attribute annotation = field.getAnnotation(Attribute.class);
        if (annotation == null)
            throw new IllegalArgumentException("No annotation " + Attribute.class + " on field " + field);
        this.tName = annotation.name();
        this.oName = field.getName();
        this.isId = annotation.id();
        this.isAutoGenerated = annotation.autoGenerated();
        this.oClass = field.getType();
        this.getter = field.getDeclaringClass().getDeclaredMethod(
                "get" + oName.substring(0, 1).toUpperCase() + oName.substring(1));
        this.setter = field.getDeclaringClass().getDeclaredMethod(
                "set" + oName.substring(0, 1).toUpperCase() + oName.substring(1),
                field.getType()
        );
    }
}
