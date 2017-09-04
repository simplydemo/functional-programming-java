package re.study.functionalprogramming.spring.expression;

import java.util.Optional;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import org.junit.Test;

public class ElvisOperatorTest {
    private static final ExpressionParser parser = new SpelExpressionParser();

    String value(String expression) {
        return value(expression, String.class);
    }

    <T> T value(String expression, Class<T> clazz) {
        return parser.parseExpression(expression).getValue(clazz);
    }

    String val(String expression, Object obj) {
        return val(expression, obj, String.class);
    }

    <T> T val(String expression, Object obj, Class<T> clazz) {
        StandardEvaluationContext context = new StandardEvaluationContext(obj);
        return parser.parseExpression(expression).getValue(context, clazz);
    }

    String input = "aasa";
    /**
     * 
     * @throws Exception
     */
    @Test
    public void ut1001_basic() throws Exception {
        ExpressionParser parser = new SpelExpressionParser();
        String result = parser.parseExpression("null?:'Unknown'").getValue(String.class);
        System.out.println(result); // 'Unknown'

        String result2 = value("'Apple'?:'Unknown'");
        System.out.println(result2); // 'Apple'
        
        
        String result3 = value("#input?:'Unknown'");
        System.out.println(result3); // 'Apple'
    }

    static class Person {
        public Integer id;
        public String name;

        public Person(Integer id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    @Test
    public void ut1002_basic() throws Exception {
        Person p1 = new Person(null, null);
        String result = val("id?:'Null'", p1);
        System.out.println(result);

        Person p2 = new Person(1001, null);
        String result2 = val("id?:'Null'", p2);
        System.out.println(result2);
    }

    @Test
    public void ut1003_basic() throws Exception {
        Person p1 = null;
        Optional<Person> op = Optional.ofNullable(p1);
        String result = op.map(v -> v.name).orElse("Unknown");
        System.out.println(result);

        Person p2 = new Person(null, null);
        System.out.println(Optional.ofNullable(p2).map(v -> v.name).orElse("Unknown"));

        Person p3 = new Person(1004, "Banana");
        System.out.println(Optional.ofNullable(p3).map(v -> v.name).orElse("Unknown"));
    }
}
