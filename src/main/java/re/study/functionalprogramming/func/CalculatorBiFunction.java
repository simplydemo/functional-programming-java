package re.study.functionalprogramming.func;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 계산기 함수 예제
 */
@Slf4j
public class CalculatorBiFunction {

    BinaryOperator<Long> add = (x, y) -> x + y;
    BinaryOperator<Long> subtract = (x, y) -> x - y;
    BinaryOperator<Long> multiply = (x, y) -> x * y;
    BinaryOperator<Long> division = (x, y) -> x / y;

    /**
     * 정의된 함수를 사용
     * @throws Exception
     */
    @Test
    public void testCalculator() throws Exception {
        log.info("add: {}", add.apply(10L, 2L));
        log.info("subtract: {}", subtract.apply(10L, 2L));
        log.info("multiply: {}", multiply.apply(10L, 2L));
        log.info("division: {}", division.apply(10L, 2L));
    }

    /**
     * 함수형 프로그래밍은 HOF 를 지원 하므로 넘겨받은 함수를 적용
     * @param v1 값1
     * @param v2 값2
     * @param func 함수
     * @return 적용 값
     */
    private Long calc(Long v1, Long v2, BiFunction<Long, Long, Long> func) {
        return func.apply(v1, v2);
    }

    @Test
    public void testCalc() throws Exception {
        log.info("calc - add: {}", calc(10L, 2L, add));
        log.info("calc - subtract: {}", calc(10L, 2L, subtract));
        log.info("calc - multiply: {}", calc(10L, 2L, multiply));
        log.info("calc - division: {}", calc(10L, 2L, division));
    }
}
