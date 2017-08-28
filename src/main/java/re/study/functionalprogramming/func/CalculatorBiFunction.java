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
     * BinaryOperator 인터페이스는 BiFunction 인터페이스를 상속한 2개의 인자를 받아 연산 하는 함수
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
     * HOF(고차 함수)를 통한 처리 예제로, 넘겨 받는 함수를 통해 데이터를 처리 하는 예제
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
