package re.study.functionalprogramming.functor;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

import lombok.extern.slf4j.Slf4j;
import re.study.functionalprogramming.functor.model.Address;
import re.study.functionalprogramming.functor.model.Customer;

@Slf4j
public class FunctorExample {
    /**
     * 펑터는 자료 구조 이다.
     * 
     * 펑터는 어떤 값을 캡슐화하는 타입 인자를 가지는 자료구조이다.
     * 
     * 펑터는 값 자체를 변경 하지 않고, 새로운 값을 가지는 펑터를 반환 한다. No side-effect
     * 
     * ABC 펑터를 구현한 Identity 펑터의 예제 이다.
     * @param <T>
     */
    class Identity<T> implements Functor<T, Identity<?>> {
        private final T value;

        Identity(T value) {
            this.value = value;
        }

        public <R> Identity<R> map(Function<T, R> f) {
            final R result = f.apply(value);
            return new Identity<>(result);
        }
    }

    @Test
    public void testFunctor() throws Exception {
        Identity<String> idString = new Identity<>("abc");
        log.info("idString.value: {}", idString.value);
        Identity<Integer> idInt = idString.map(String::length);
        log.info("idInt.value: {}", idInt.value);

        Function<String, Integer> sumChar = v -> {
            char array[] = v.toCharArray();
            int sum = 0;
            for (char c : array) {
                sum += c;
            }
            return sum;
        };

        Identity<Integer> sumFunctor = idString.map(sumChar);
        log.info("idInt.value: {}", sumFunctor.value);
    }

    /**
     * Customer 클래스 인스턴스를 기준으로 아래와 같이 펑터를 활용 할 수 있다.
     * 
     * <code>
        Identity<byte[]> idBytes = new Identity<>(customer)
            .map(Customer::getAddress)
            .map(Address::street)
            .map((String s) -> s.substring(0,3))
            .map(String::toLowerCase)
            .map(String::getBytes);</code>
     * 
     * 가만히 보면 펑터를 이용하여 값을 매핑해 나가는 것이 단순히 메서드 체이닝하는 것과 다를 바 없어 보인다.
     * 
     * <code>
         byte[] bytes = customer
            .getAddress()
            .street()
            .substring(0,3)
            .toLowerCase()
            .getBytes();</code>
     * 
     * 별로 얻는 것도 없이, 심지어 값을 빼낼 수도 없는데 군더더기 같은 펑터를 신경써야 하는 이유가 뭘까?
     */
    @Test
    public void testCustomerFunctor() throws Exception {
        String addr = "hollywood";
        Customer customer = new Customer(addr);
        // @formatter:off
        byte[] bytes = new Identity<>(customer)
            .map(Customer::getAddress)
            .map(Address::street)
            .map((String s) -> s.substring(0,3))
            .map(String::toLowerCase)
            .map(String::getBytes).value
            ;
        log.info("Case 1: {}", bytes);
        
        byte[] cbytes = customer
                .getAddress()
                .street()
                .substring(0,3)
                .toLowerCase()
                .getBytes();
        log.info("Case 2: {}", cbytes);
        // @formatter:on

        // customer 객체가 null 이거나 street 값이 null 인 경우는 어떻게 될까?
    }

    /**
     * FOptional<T> 펑터는 값을 가지고 있을 수도 있고 비어 있을 수도 있다. 타입 안전성을 유지하면서 null을 인코딩하는 방법인 셈이다.
     * FOptional 객체를 만들 수 있는 방법은 두 가지다. 값을 가지고 생성하는 방법과 empty()로 생성하는 것이다. 어떤 방법으로 만들든
     * Identity와 마찬가지로 일단 생성되고 나면 FOptional 객체는 변경되지 않으며 다만 그 내부의 값을 이용할 수 있을 뿐이다.
     * FOptional은 비어있는 상태에서는 함수 f를 사용하지 않는다는 점이 다르다.
     * @param <T>
     */
    static class FOptional<T> implements Functor<T, FOptional<?>> {

        private final T valueOrNull;

        private FOptional(T valueOrNull) {
            this.valueOrNull = valueOrNull;
        }

        /**
         * valueOrNull 값이 null 이 아닌 경우에만 함수가 동작 한다.
         */
        public <R> FOptional<R> map(Function<T, R> f) {
            if (valueOrNull == null)
                return empty();
            else
                return of(f.apply(valueOrNull));
        }

        public static <T> FOptional<T> of(T a) {
            return new FOptional<T>(a);
        }

        @SuppressWarnings("hiding")
        private <T> FOptional<T> empty() {
            return new FOptional<T>(null);
        }

    }

    /**
     * FOptional 펑터의 경우 Identity 펑터와는 다르게 null 이 아닌경우에만 함수를 실행 하도록 되어 있으며, null 인경우 empty
     * 펑터를 리턴 하도록 되어 있다.
     * @throws Exception
     */
    @Test
    public void testCustomerFunctor2() throws Exception {
        String addr = "hollywood";
        addr = null;
        Customer customer = new Customer(addr);
        // @formatter:off
        try {
            byte[] bytes = FOptional.of(customer)
                .map(Customer::getAddress)
                .map(Address::street)
                .map((String s) -> s.substring(0,3))
                .map(String::toLowerCase)
                .map(String::getBytes).valueOrNull;
                ;
            log.info("Case 1: {}", bytes);
        }
        catch (Exception e) {
            log.error(e.getMessage());
        }
        
        try {
            byte[] cbytes = customer
                    .getAddress()
                    .street()
                    .substring(0,3)
                    .toLowerCase()
                    .getBytes();
            log.info("Case 2: {}", cbytes);
        }
        catch (Exception e) {
            log.error(e.getMessage());
        }
        // @formatter:on
    }

    /**
     * 펑터가 꼭 하나의 T 값을 캡슐화하는 것은 아니라는 것을 알 수 있다. 펑터는 여러 값을 포장할 수도 있다. FList 펑터가 여러 값을 포장 하고
     * 있는 좋은 예이다.
     * 
     * FList 펑터는 입력 T 에 대한 어떤 유형 ? 값을 가지는 FList 펑터를 리턴 한다.
     * 
     * FList 펑터의 문제점은 map 메서드를 통해 리턴된 FList 펑터에 캡슐화된 값을 "?" 알 수 없다는 것이다.
     * @param <T>
     */
    class FList<T> implements Functor<T, FList<?>> {

        private final ImmutableList<T> list;

        FList(Iterable<T> value) {
            this.list = ImmutableList.copyOf(value);
        }

        @Override
        public <R> FList<?> map(Function<T, R> f) {
            List<R> result = new ArrayList<>(list.size());
            for (T t : list) {
                result.add(f.apply(t));
            }
            return new FList<>(result);
        }
    }

    /**
     * 펑터가 꼭 하나의 T 값을 캡슐화하는 것은 아니라는 것을 알 수 있다. 펑터는 여러 값을 포장할 수도 있다. List 펑터처럼.
     * @throws Exception
     */
    @Test
    public void testCustomerFunctor3() throws Exception {
        FList<Customer> custfunctor = new FList<>(asList(new Customer("Seoul ..."), new Customer("Pucheon ...")));

        // @formatter:off
        FList<?> streets = 
                custfunctor
        .map(Customer::getAddress)
        // custfunctor 펑터의 타입이  Customer 로 정해져 있으므로, 아래의 map은 사용할 수 없다.
        // .map(Address::getStreet)
        // map 함수를 통해 타입을  변환 해서도 아래의 타입을 사용할 수 없다.
        .map(a -> (Address)a)
        // map 내부에서 형을 변환 했지만, FList 펑터의 타입은 여전히 Customer 로 인지 하기 때문이다.
        .map(addr -> ((Address)addr).street() );
        log.info("{}", streets.list);
        
        // @formatter:on

    }

    /**
     * 펑터가 꼭 하나의 T 값을 캡슐화하는 것은 아니라는 것을 알 수 있다. 펑터는 여러 값을 포장할 수도 있다. FRList 펑터가 여러 값을 포장 하고
     * 있는 좋은 예이다.
     * 
     * FRList 펑터는 입력 T 에 대한 R 타입의 값을 가지는 FRList 펑터를 리턴 한다. * FList 펑터의 문제점을 보완한 펑터 이다.
     * @param <T>
     */
    class FRList<T> implements Functor<T, FRList<?>> {

        private final ImmutableList<T> list;

        FRList(Iterable<T> value) {
            this.list = ImmutableList.copyOf(value);
        }

        @Override
        public <R> FRList<R> map(Function<T, R> f) {
            List<R> result = new ArrayList<>(list.size());
            for (T t : list) {
                result.add(f.apply(t));
            }
            return new FRList<>(result);
        }
    }

    /**
     * testCustomerFunctor3() 테스트 케이스를 보면 map 을 통해 변환된 T 에 대해 정확하게 알고 있어야 했지만, FRList<R> 의
     * 경우는 그냥 사용 할 수 있다.
     * @throws Exception
     */
    @Test
    public void testCustomerFunctor4() throws Exception {
        FRList<Customer> custfunctor = new FRList<>(asList(new Customer("Seoul ..."), new Customer("Pucheon ...")));

        // @formatter:off
        FRList<?> streets = 
                custfunctor
                    .map(Customer::getAddress)
                    .map(Address::street);        
        // @formatter:on
        log.info("{}", streets.list);

    }
}
