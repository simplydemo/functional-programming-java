package re.study.functionalprogramming.func;

import java.util.Objects;
import java.util.function.Function;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 합산 함수 예제
 */
@Slf4j
public class FunctionAdd {
    /**
     * 1 을 더한 값을 리턴
     */
    Function<Integer, Integer> addOne = i -> i + 1;

    @Test
    public void testAddOne() {
        log.info("result: {}", addOne.apply(100));
    }

    /**
     * 넘겨받은 Integer 를 더한 값을 리턴
     * @param value
     * @return
     */
    Function<Integer, Integer> addInt(Integer value) {
        return i -> i + value;
    }

    @Test
    public void testAddInt() {
        log.info("result: {}", addInt(11).apply(100));
    }

    /**
     * 함수 컴포지션 'andThen' 을 통한 합산
     */
    @Test
    public void testAddIntAndThen() {
        log.info("result: {}", addInt(10).andThen(addOne).andThen(addOne).andThen(addOne).apply(100));
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
