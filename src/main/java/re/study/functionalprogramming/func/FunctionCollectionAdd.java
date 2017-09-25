package re.study.functionalprogramming.func;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 컬렉션 함수 예제
 */
@Slf4j
public class FunctionCollectionAdd {

    List<Integer> values = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    /**
     * 컬렉션 요소들을 합산한 값을 반환
     * @return
     */
    Function<Collection<Integer>, Integer> sum() {
        return i -> i.stream().reduce(0, (a, b) -> a + b);
    }

    /**
     * 함수형 프로그래밍에서 합산 예제
     */
    @Test
    public void test_sum() {
        log.info("result: {}", sum().apply(values));
    }

    /**
     * 평균 값을 구하는 함수 인터페이스 정의
     * @param <T> 값
     * @param <D> 나눌 값
     * @param <R> 몫
     */
    static interface AvgFunction<T, D, R> {
        R avg(T t, D d);
    }

    /**
     * 두개의 인자를 취하는 함수는 이미 BiFunction 을 제공 하고 있음.
     */
    BiFunction<Integer, Integer, Integer> avg = (v, d) -> v / d;

    /**
     * BiFunction를 통한, avg 함수 테스트
     */
    @Test
    public void test_avg() {
        log.info("result: {}", avg.apply(sum().apply(values), values.size()));
    }

    AvgFunction<Integer, Integer, Integer> avgFunc = (v, d) -> v / d;

    /**
     * 커스텀 AvgFunction를 통한, avgFunc 함수 테스트
     */
    @Test
    public void test_avgFunc() {
        log.info("result: {}", avgFunc.avg(sum().apply(values), values.size()));
    }

    public static Function<String, Function<Integer, String>> getNumbers() {
        final List<String> ones = Arrays.asList("Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight",
                "Nine");
        final List<String> tens = Arrays.asList("Zero", "Ten", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy",
                "Eighty", "Ninety");

        return units -> number -> {
            return units == "Ones" ? ones.get(number % 10) : tens.get(number % 10);
        };
    }

    /**
     * Currying(커링): 커링은 여러 인자를 받는 함수를 하나의 인자만 받도록 여러함수를 합성 하는 기법 이다. Why? 인자를 여러개를 받는 함수는
     * 사용성에 있어서 유연하지 않기 때문 이다.
     * 
     * <code>
     * o = f(a, b) + c
     * f(a, b) 의 함수가 a,b를 합산(+) 하는 것 이라면 아래와 같이 변경 해도 된다.
     * o = f(a) + f(b) + f(c)
     * Lambda 의 선언부만 표현 하면
     * f = (a, b) -> c 를
     * f = a -> b -> c (으)로 변경 한 것과 같다.
     * </code>
     * 
     * 커링은 plus.(1).(2) 의 형태가 plus(1,2) 보다 사용이 유연 하기 때문이다.
     */
    @Test
    public void test_currying() {
        Function<String, Function<Integer, String>> currying = getNumbers();
        System.out.println(currying.apply("Tens").apply(1112));
        System.out.println(currying.apply("Tens").apply(8)); // 80
        System.out.println(currying.apply("Ones").apply(2)); // 2
    }

    Function<Collection<Integer>, Function<Integer, Double>> avgCurry = list -> count -> {
        final double sum = list.stream().reduce(0, (a, b) -> a + b);
        return new Double(sum / count);
    };

    /**
     * <pre>
     * 바로 위의 avg2 함수는 평균을 구하기 위해 2개의 인자를 받는다. 그 형태는 avg2(Integer, Integer) 이다.
     * 함수의 시그니쳐만으로는  첫번째 인자와 두번째 인자는 각각 어떤 인자(값)을 받아야 되는지 알 수가 없다.
     * </pre>
     *
     * <code>
    int a = sum().apply(values);
    int b = values.size();
    avg2.apply(a, b);</code>
     * 
     * 반면, avgCurry 함수는 인자의 타입 정보를 알려주게 된다.
     */
    @Test
    public void test_avgCurry() {
        log.info("result: {}", avgCurry.apply(values).apply(values.size()));
    }

    /**
     * 컬렉션에 대한 평균은 쉽게 avgCollections 함수를 이용 할 수도 있다.
     */
    Function<Collection<Integer>, Double> avgCollections = nums -> {
        final int size = nums.size();
        final double sum = nums.stream().reduce(0, (a, b) -> a + b);
        return (sum / size);
    };

    @Test
    public void test_avgCollections() {
        log.info("result: {}", avgCollections.apply(values));
    }
}
