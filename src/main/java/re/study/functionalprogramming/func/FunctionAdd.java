package re.study.functionalprogramming.func;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 합산 함수 예제
 */
@Slf4j
public class FunctionAdd {

    /**
     * 람다 표기법을 통한 addOne 함수의 정의
     */
    Function<Integer, Integer> addOne = i -> i + 1;

    /**
     * 람다 함수의 사용 예
     */
    @Test
    public void ut1001_AddOne() {
        log.info("result: {}", addOne.apply(100));
    }

    /**
     * 람다 함수의 합성을 통한 처리 예제
     */
    @Test
    public void ut1001_AddOneAndOne() {
        log.info("result: {}", addOne.compose(addOne).apply(100));
    }

    /**
     * Function 인터페이스 구현을 통한 addOne 함수의 정의
     */
    Function<Integer, Integer> addOne() {
        return new Function<Integer, Integer>() {
            public Integer apply(Integer i) {
                return i + 1;
            }
        };
    }

    /**
     * Function 함수의 사용 예
     */
    @Test
    public void ut1002_AddOne() {
        log.info("result: {}", addOne().apply(100));
    }

    /**
     * Function 함수의 합성을 통한 처리 예제
     */
    @Test
    public void ut1002_AddOneAndOne() {
        log.info("result: {}", addOne().compose(addOne()).apply(100));
    }

    /**
     * 넘겨받는 인자가 Integer 타입 2개 이고, 결과는 Integer 타입으로 리턴
     */
    BiFunction<Integer, Integer, Integer> addInt = (x, y) -> x + y;

    /**
     * 두개의 인자를 받는 람다 함수 x = f(i1, i2)
     */
    @Test
    public void ut1003_AddXY() {
        log.info("result: {}", addInt.apply(10, 10));
    }

    /**
     * 넘겨받은 Integer 를 더한 값을 리턴
     * @param value
     * @return
     */
    Function<Integer, Integer> addInt(Integer value) {
        return i -> i + value;
    }

    /**
     * 두개의 인자를 받는  Function 함수 x = f(i1).apply(i2)
     */
    @Test
    public void ut1004_AddXY() {
        log.info("result: {}", addInt(10).apply(10));
    }

    static Integer add5(Integer i) {
        return i + 5;
    }

    /**
     * java 8 람다 및 메서드레퍼런스를 통한 함수 합성 예제
     */
    @Test
    public void ut1005_AddXYAndAddOne() {
        log.info("result: {}", addInt.andThen(v -> v + 5).apply(10, 10));
        log.info("result: {}", addInt.andThen(FunctionAdd::add5).apply(10, 10));
    }

    @Test
    public void ut1006_AddInt() {
        log.info("result: {}", addInt(10).andThen(addInt(5)).apply(10));
    }

    /**
     * 입력 아규먼트 값을 더한 값을 리턴
     * 
     * @param <T> 입력 아규먼트 타입
     * @param <R> 리턴 아규먼트 타입
     */
    @FunctionalInterface
    static interface AddFunction<T, R> {

        R apply(T t);

        default <V> Function<T, V> and(Function<? super R, ? extends V> other) {
            Objects.requireNonNull(other);
            return (T t) -> other.apply(apply(t));
        }
    }

    /**
     * 2개의 함수 합성을 통한 덧셈
     */
    Function<Integer, Function<Integer, Integer>> addFunc = f1 -> f2 -> f1 + f2;

    @Test
    public void testAddFunc() {
        log.info("result: {}", addFunc.apply(100).apply(2));
    }

    /**
     * 3개의 함수 합성을 통한 덧셈
     */
    Function<Integer, Function<Integer, Function<Integer, Integer>>> addTripleFunc = f1 -> f2 -> f3 -> f1 + f2 + f3;

    @Test
    public void testAddTripleFunc() {
        log.info("result: {}", addTripleFunc.apply(100).apply(2).apply(2));
    }

}
