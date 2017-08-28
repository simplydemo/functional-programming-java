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

    AvgFunction<Integer, Integer, Integer> avgFunc = (v, d) -> v / d;

    /**
     * 커스텀 AvgFunction를 통한, avgFunc 함수 테스트  
     */
    @Test
    public void test_avgFunc() {
        log.info("result: {}", avgFunc.avg(sum().apply(values), values.size()));
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

}
