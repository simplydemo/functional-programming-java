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

    private <T> Boolean matcher(List<T> list, Predicate<T> predicate) {
        for (T v : list) {
            if (predicate.test(v)) {
                return true;
            }
        }
        return false;
    }

    private static final String word = "da";
    /**
     * Predicate 로 evaluate 할 요소가 정의된 값 word 와 일치 하는지 체크
     */
    final Predicate<String> findWord = p -> p.equals(word);

    /**
     * Predicate 로 evaluate 할 요소가, 비교 할 값 value 일치 하는지 체크, 특히 value 는 Type 을 정의 하지 않았으므로
     * 매처에 대한 대상을 직접 구현 할 수 있음.
     */
    public <T> Predicate<T> matcher(final T value) {
        return p -> value.equals(p);
    }

    @Test
    public void testPredicateCollection() {
        final List<String> list = Arrays.asList("a", "b", "c", "d", "e");
        log.info("findWord 1: {}", matcher(list, findWord));
        log.info("findWord 2: {}", matcher(list, matcher("da")));
        log.info("findWord 3: {}", list.stream().anyMatch(findWord));

        final List<Integer> listInt = Arrays.asList(1, 2, 3, 4, 5);
        log.info("findWord 4: {}", matcher(listInt, matcher(10)));
    }

}
