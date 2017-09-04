package re.study.functionalprogramming.consumer;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.junit.Test;

/**
 * Consumer 는 반환값이 없고 데이터(메시지)를 소비하는 역할만 한다. 메시지 소비(Consumer) 주체가 어떤 형태 인지는 정의 하는 시점에선 관심이
 * 없고, 정의된 소비(Consumer) 주체가 함수가 실행 되는 시점에 동작할 뿐이다.
 */
public class ConsumerTest {

    private void printNames(String name) {
        System.out.println(name);
    }

    @Test
    public void ut1001_basic() throws Exception {
        Consumer<String> consumer = this::printNames;
        consumer.accept("Jeremy");
        consumer.accept("Paul");
        consumer.accept("Richard");
    }

    private void printList(List<Integer> listOfIntegers, Consumer<Integer> consumer) {
        for (Integer integer : listOfIntegers) {
            consumer.accept(integer);
        }
    }

    @Test
    public void ut1002_list() {
        Consumer<Integer> consumer = i -> System.out.println("value: " + i);
        List<Integer> values = Arrays.asList(1, 10, 200, 101, -10, 0);
        printList(values, consumer);
    }

    @Test
    public void ut1003_listComposition() {
        Consumer<Integer> consumer = i -> System.out.print(i);
        Consumer<Integer> consumerWithAndThen = consumer.andThen(i -> System.out.println("(printed " + i + ")"));
        List<Integer> values = Arrays.asList(1, 10, 200, 101, -10, 0);
        printList(values, consumerWithAndThen);
    }
}
