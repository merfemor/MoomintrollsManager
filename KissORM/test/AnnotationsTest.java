import org.junit.Test;
import ru.ifmo.cs.korm.Session;
import ru.ifmo.cs.korm.annotations.Attribute;
import ru.ifmo.cs.korm.annotations.Table;

import java.sql.SQLException;

public class AnnotationsTest {

    @Test
    public void testHasEntityAnnotation() throws SQLException {
        @Table(name = "test")
        class Test {
            @Attribute(name = "test")
            int attribute;

            public int getAttribute() {
                return attribute;
            }

            public void setAttribute(int attribute) {
                this.attribute = attribute;
            }
        }
        Session session = Utils.getNewSession();
        session.addClass(Test.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoEntityAnnotation() throws SQLException {
        class BadTest {
        }
        Utils.getNewSession().addClass(BadTest.class);
    }

    @Test
    public void testOneAttribute() throws SQLException {
        @Table(name = "test")
        class NoAnnotatedFieldClass {
            @Attribute(name = "field")
            int field1;
            double field2;

            public int getField1() {
                return field1;
            }

            public void setField1(int field1) {
                this.field1 = field1;
            }

            public double getField2() {
                return field2;
            }

            public void setField2(double field2) {
                this.field2 = field2;
            }

        }
        Session session = Utils.getNewSession().addClass(NoAnnotatedFieldClass.class);
        session.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoAttributes() throws SQLException {
        @Table(name = "test")
        class NoAnnotatedFieldClass {
            int field1;
            double field2;
        }
        Utils.getNewSession().addClass(NoAnnotatedFieldClass.class);
    }


}
