package re.study.functionalprogramming.predicate;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 컬렉션 요소 테스트
 */
@Slf4j
public class PredicateCollection {

    private static final String constantValue = "c";
    /**
     * Predicate 로 evaluate 할 요소가 정의된 상수 값 constantValue 와 일치 하는지 체크
     */
    final Predicate<String> predicateConstantValue = p -> p.equals(constantValue);

    /**
     * 정해진 값 constantValue 에 대한 Predicate
     */
    @Test
    public void ut1001_predicateConstantValue() {
        final List<String> list = Arrays.asList("a", "b", "c", "d", "e");
        log.info("result: {}", matcher(list, predicateConstantValue));
    }

    /**
     * Predicate 로 evaluate 할 요소가, 비교 할 값 value 일치 하는지 체크, 특히 value 는 Type 을 정의 하지 않았으므로
     * 매처에 대한 대상을 직접 구현 할 수 있음.
     */
    private <T> Predicate<T> matcher(final T value) {
        return p -> value.equals(p);
    }

    private <T> Boolean matcher(List<T> list, Predicate<T> predicate) {
        for (T v : list) {
            if (predicate.test(v)) {
                return true;
            }
        }
        return false;
    }

    /**
     * String 타입 인자 "c" 에 대한 matcher 함수
     */
    @Test
    public void ut1002_predicateStringWithMatcher() {
        final List<String> list = Arrays.asList("a", "b", "c", "d", "e");
        log.info("result: {}", matcher(list, matcher("c")));
    }

    /**
     * Integer 타입 인자 0 에 대한 matcher 함수
     */
    @Test
    public void ut1003_predicateIntegerWithMatcher() {
        final List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        log.info("result: {}", matcher(list, matcher(0)));
    }

    private <T> Predicate<T> listMatcher(List<T> t) {
        return p -> {
            for (T v : t) {
                if (v.equals(p)) {
                    return true;
                }
            }
            return false;
        };
    }

    /**
     * String 타입 인자 "f" 에 대한 Predicate
     */
    @Test
    public void ut1004_listOfStringPredicateWithMatcher() {
        final List<String> strings = Arrays.asList("a", "b", "c", "d", "e");
        log.info("result: {}", listMatcher(strings).test("f"));
    }

    /**
     * Integer 타입 인자 11 에 대한 Predicate
     */
    @Test
    public void ut1005_listOfIntegerPredicateWithMatcher() {
        final List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5);
        log.info("result: {}", listMatcher(integers).test(11));
    }

    // public static <A,B> Function<A,B> Y(Function<Function<A,B>, Function<A,B>> f) {
    // return x -> f.apply(Y(f)).apply(x);
    // }

}
