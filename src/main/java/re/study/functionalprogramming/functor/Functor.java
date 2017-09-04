package re.study.functionalprogramming.functor;

import java.util.function.Function;

/**
 * 펑터는 자료 구조 이다.
 * 
 * 펑터는 어떤 값을 캡슐화하는 타입 인자를 가지는 자료구조이다.
 *
 * @param <T> 인자 T
 * @param <F> 리턴 타입 펑터
 */
public interface Functor<T, F extends Functor<?, ?>> {

    /**
     * 펑터는 함수 아규먼트를 인자로 받고 그 함수의 처리 결과를 펑터로 리턴 한다.
     * 
     * 반환 되는 펑터는 여러 타입을 처리 할 수 있는 컨테이너 역할을 할 수 있다.
     * @param f 함수
     * @return 함수의 결과를 감싸고 있는 Functor
     */
    <R> F map(Function<T, R> f);
}
