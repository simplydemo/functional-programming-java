package re.study.functionalprogramming.functor;

import java.util.function.Function;

/**
 * 펑터는 자료 구조 이다.
 * 펑터는 어떤 값을 캡슐화하는 타입 인자를 가지는 자료구조이다.
 * 
 * Functor<T>는 항상 불변형의 컨테이너이다. 따라서 map은 원래의 객체를 절대 변경하지 않는다. 대신 인자 함수로 변형한 결과값을 완전히 새로운 펑터
 * 객체에 감싸서 반환한다.(결과 타입 R은 다른 타입일 수도 있다.) 추가로 펑터는 identity 함수가 전달되었을 때, 즉 map(x -> x)가
 * 호출되었을 때 어떤 다른 동작도 취해서는 안된다. (역주: side-effect가 없음을 말한다) 이런 패턴의 경우 항상 같은 펑터나 같은 인스턴스를
 * 반환해야 한다.
 *
 * @param <T> 인자 T
 */
public interface Fnctor<T> {

    
    /**
     * 펑터는 함수 아규먼트를 인자로 받고, 그 함수의 처리 결과를 펑터로 감싸서 반환 한다.
     * 결국 펑터는 함수의 결과 값을 가지는 컨테이너 역할을 하게 된다.
     * @param f 함수
     * @return 함수의 결과를 감싸고 있는 Functor
     */
    <R> Fnctor<R> map(Function<T, R> f);

}
