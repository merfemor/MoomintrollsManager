package ru.ifmo.cs.korm;

import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class SQLSyntax {
    private static SQLSyntax instance;

    protected SQLSyntax() {
    }

    public static SQLSyntax get() {
        if (instance == null) {
            instance = new SQLSyntax();
        }
        return instance;
    }

    protected Object toJdbcObject(Object o) {
        if (o instanceof ZonedDateTime) {
            return Timestamp.from(((ZonedDateTime) o).toInstant());
        }
        return o;
    }

    protected <T> T fromJdbcObject(Object o, Class<T> objClass) {
        if (o instanceof Timestamp) {
            if (objClass == ZonedDateTime.class) {
                return (T) ZonedDateTime.ofInstant(((Timestamp) o).toInstant(), ZoneOffset.UTC);
            }
        }
        return (T) o;
    }

    protected Class toSQLtype(Class type) {
        if (type == long.class) {
            return Long.class;
        } else if (type == int.class) {
            return Integer.class;
        } else if (type == boolean.class) {
            return Boolean.class;
        } else if (type == ZonedDateTime.class) {
            return Timestamp.class;
        }
        return type;
    }
}
