package re.study.functionalprogramming.predicate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * Integer 조건 테스트
 */
@Slf4j
public class PredicateInteger {

    private <T> List<T> map(List<T> list, Predicate<T> predicate) {
        final List<T> result = new ArrayList<>();
        for (T v : list) {
            if (predicate.test(v)) {
                result.add(v);
            }
        }
        return result;
    }

    final Predicate<Integer> GE2 = v -> v > 2;

    @Test
    public void ut1001_predicateGE2() {
        log.info("result : {}", GE2.test(2));
        log.info("result : {}", GE2.test(3));
    }

    @Test
    public void ut1002_predicateListGE2() {
        final List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        log.info("result : {}", list.stream().filter(GE2).collect(Collectors.toList()));
    }

    final Predicate<Integer> LE7 = n -> n < 7;

    @Test
    public void testPredicateInteger() {
        final List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        log.info("Original  : {}", map(list, v -> true));
        log.info("Result GE2: {}", map(list, GE2));
        log.info("Result LE7: {}", map(list, LE7));
        log.info("Result GE2.and(LE7): {}", map(list, GE2.and(LE7)));
    }

}
