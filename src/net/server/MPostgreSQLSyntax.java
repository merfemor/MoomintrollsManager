package net.server;

import ru.ifmo.cs.korm.SQLSyntax;
import trolls.Kindness;

import java.awt.*;

public class MPostgreSQLSyntax extends SQLSyntax {
    private static MPostgreSQLSyntax ourInstance = new MPostgreSQLSyntax();

    private MPostgreSQLSyntax() {
        super();
    }

    public static MPostgreSQLSyntax get() {
        return ourInstance;
    }

    @Override
    public Object toJdbcObject(Object o) {
        if (o instanceof Kindness) {
            return ((Kindness) o).value();
        } else if (o instanceof Color) {
            return ((Color) o).getRGB();
        }
        return super.toJdbcObject(o);
    }

    @Override
    public <T> T fromJdbcObject(Object o, Class<T> objClass) {
        if (objClass == Kindness.class) {
            return (T) new Kindness((Integer) o);
        } else if (objClass == Color.class) {
            return (T) new Color((Integer) o);
        }
        return super.fromJdbcObject(o, objClass);
    }

    @Override
    public Class toSQLtype(Class type) {
        if (type == Kindness.class) {
            return Integer.class;
        } else if (type == Color.class) {
            return Integer.class;
        }
        return super.toSQLtype(type);
    }
}
