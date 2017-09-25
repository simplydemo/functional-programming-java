# functional-programming-java
**`함수형 프로그래밍의 장점`**
- 상태가 없다; Side effect가 없다.
- 동시성 - 멀티코어 기술과 궁합이 잘 맞다.
- 프로그램이 짧고 읽기 쉽다.
- 생산성이 올라간다.


**`함수형 프로그래밍의 단점`**
- 상태가 없다; Side effect가 없다.
현실의 프로그램은 모두 side effect와 mutation으로 동작한다. 사용자와의 interaction은 대부분 상태의 변화로 모델링 된다. 
(총으로 외계인을 쐈는데 체력이 떨어진 새로운 외계인을 생성하는 것으로 모델링할 사람은 거의 없다. 
대개는 기존 외계인의 체력을 감소시킨다.)
- 동시성 - 멀티코어 기술과 궁합이 잘 맞다.
Immutable data structure는 "쓰레드 안전"을 쉽게 확보하는 대신 '오래된' 데이터를 사용하게 될 수도 있다.
Mutable data structure는 항상 최신 데이터를 다룬다는 장점이 있지만 Data Consistency를 보장하기 위한 복잡함을 수반한다. 
어느 것이 낫다고 할 수는 없다.
- 프로그램이 짧고 읽기 쉽다. 
더 길고 읽기 어려운 경우도 있다. 
- 생산성이 올라간다.
함수형 스타일로 프로그램을 작성할 수 있는 프로그래머를 채용하는데 드는 비용을 상쇄시킬만큼 생산성이 올라야만 한다.

`세상엔 함수형 스타일이 딱 맞아떨어지지 않는 문제도 많다`

## 함수형 프로그래밍의 핵심
### 1. 순수 함수로 작성 되어야 한다. 수학의 f(x) 와 동일 하다.  
  (* 함수는 side-effect 를 가지지 않는다.)
~~~
function addOne(x){
	return x + 1;
}

function substractOne(x){
	return x - 1;
}
~~~

### 2. Immutability (불변성)을 보장 해야 한다.
  - 변수를 사용 하지 않는다.
  - 상태를 보존 하지 않는다.  
    ( 1 ~ 10 까지 더한 값 55 를 구한다고 했을 때, 최종 결과값 55만 가지고 있으며, 1 부터 9 까지의 연산에 대한 상태 변경 등등에 대해선 가지고 있지 않는다. )
~~~
int var = 1;
function add(x){
	var = var + 1;
	return var;
}
~~~

### 3. Statement 를 사용 하지 않는다.
OOP 또는 명령형 프로그래밍은 주어진 문장(Statement)을 실행 한다. 반면,   
함수형 프로그래밍은 주어진 표현식(expression)을 계산(Evaluation) 하는 방식 으로 구성 된다.

### 4. Higher-Order Functions (HOFs) 
함수를  자신의 파라미터로 취하는 함수 이다.  
  OOP 에서 함수는 값이나 객체를 인자로 주고, 값이나 객체를 결과로 받는다.  
  FP 에서는, 함수를 인자로 주고, 함수를 인자로 받을 수 있을 뿐만 아니라, 함수를 배열이나 컬렉션에 담을수도 있다.
   즉, 함수가 값과 같이 취급 된다.
~~~
function add
function divide
function composition
function farrays = [add, devide];
function somefunction = composition(add, divide)
function somefunction2 = composition(farrays)
~~~

### 5. Recursion
함수형 프로그래밍에선 재귀 호출이 빈번하게 사용된다. 때론 재귀 호출이 반복문을 대체할 수 있는 유일한 방법인 경우도 있으며, 
주어진 함수를 사용해 원하는 값을 계산할 때 까지의 프로그램 흐름을 만들어가는 필수 장치다. 재귀 호출의 빈번한 사용에 따른 비용을 최소화하기 위해, 
함수형 언어는 Tail Call(Tail Recursion) 기능을 통해 콜-스택의 불필요한 생성을 방지하며 메모리 낭비를 제거하는 등의 최적화 방안을 제공한다.

- 일반적인 합산 프로그램.
~~~
var arr = [1, 2, 3, 4, 5];
var tot = 0;
arr.forEach(v) {
  tot+=v;
}
~~~

- FP 를 통한 합산 Case 1:
~~~
function sum(tot, count){
	tot+=count;
	if(count > 0){
	   return sum(tot, count - 1)); 
	} else {
	   return tot;
	}	
} 
sum(0, 5)
~~~

- FP 를 통한 합산 Case 2:
~~~
var arr = [1, 2, 3, 4, 5];
arr.reduce((a,b) => {return a+b;}, 0);
~~~

Quiz. 다음의 evenMatcher 은 함수형 프로그램에서 추구 하는 순수 함수 인가?
~~~
var evenMatcher = function(x) { return x % 2 === 0; }
var evenFunc = function(arr) { return arr.filter( evenMatcher ); };

var arr = [1, 2, 3, 4, 5];
evenFunc(arr);
~~~

*** 너무나도 중요하고 흔들리지 말아야 할 사항으로 함수 f 는 인자 x 를 입력 하여 결과값 o 를 반환 하며, 함수 f 는 pure 함수여야만 한다 라는 점이다.
~~~
o = f(x)
~~~
어떤 값 xxx = a(x).b(x).c(x).d(x).e(x).f(x).g(x).h(x) 의 결과 일 수 있다.


## 살펴 보기

### Function 에 대한 이해
- Java 8 의 함수(Function)
Java 8 에서 함수는 Function<T,R> `R = f(T)` 이다.  
타입 T 는 입력값을 의미 하고, 타입 R 은 반환 값을 의미 한다.  

#### re.study.functionalprogramming.func.FunctionAdd
일반적인 함수를 정의 하고 사용 하는 예제 이다.

- 람다 표기법을 통한 addOne 함수의 정의
~~~
Function<Integer, Integer> addOne = i -> i + 1;

System.out.println( addOne.apply(100) );
~~~

- addOne 람다 함수의 합성 예제
~~~
Function<Integer, Integer> addOne = i -> i + 1;

System.out.println( addOne.compose(addOne).apply(100) );
~~~

- 사용자 커스텀 함수 정의
~~~
Function<Integer, Integer> addOne() {
    return new Function<Integer, Integer>() {
        public Integer apply(Integer i) {
            return i + 1;
        }
    };
}

System.out.println( addOne().apply(100) );
~~~

- 파라미터 인자를 두개 취하는 함수 예제
아래는 2개의 Integer 타입 인자를 처리 하여 Integer 타입 결과를 반환 하는 함수 `o = f(x, y)` 정의 이다.
~~~
BiFunction<Integer, Integer, Integer> addInt = (x, y) -> x + y;

System.out.println( addInt.apply(10, 10) );
~~~

- java 8 의 메서드 레퍼런스(Lambda 와는 다른) 예제
add5(Integer i) 함수는 일반적인 java 의 static 메서드 이다.  
addOne 의 람다 함수에서 합성을 위한 함수 인자로 add5 메서드를 사용 하였다. 
~~~
class FunctionAdd {

    static Integer add5(Integer i) {
        return i + 5;
    }

}

System.out.println( addOne.andThen(FunctionAdd::add5).apply(0) );
~~~

- _@FunctionalInterface_ 어노테이션을 이용한 사용자 함수 예제
아래 ConstantFunction 함수는 인자가 없고 반환 값만 정의하는 함수 이다.
표기법에서 인자가 없는 `f()` 함수에 대한 람다식은 `f = () -> x` 으로 한다. 
~~~
@FunctionalInterface
interface ConstantFunction<R> {
    R apply();
}

ConstantFunction<Integer> one = () -> 1;

System.out.println(one.apply());
~~~

- 함수와 함수를 합성 하는 예제
addFunc 의 경우 2개의 함수 파라미터 인자 f1과 f2를 넘겨 받아 더해진 결과를 반환 하는 커스텀 함수 `f1( f2(x) )` 이다.
~~~
/**
 * 2개의 함수 합성을 통한 덧셈
 */
Function<Integer, Function<Integer, Integer>> addFunc = f1 -> f2 -> f1 + f2;

System.out.println(addFunc.apply(100).apply(2));
~~~

#### re.study.functionalprogramming.func.FunctionCollectionAdd
컬렉션 처리 관련 함수를 정의 하고 사용 하는 예제 이다.

- 컬렉션의 합산 값을 처리 하는 함수 sum 
~~~
List<Integer> values = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

/**
 * 컬렉션 요소들을 합산한 값을 반환
 * @return
 */
Function<Collection<Integer>, Integer> sum() {
    return i -> i.stream().reduce(0, (a, b) -> a + b);
}

System.out.println( sum().apply(values) );
~~~

- 컬렉션의 평균 값을 구하는 함수 avg
~~~
    /**
     * 평균 값을 구하는 함수 인터페이스 정의
     * @param <T> 값
     * @param <D> 나눌 값
     * @param <R> 몫
     */
    BiFunction<Integer, Integer, Integer> avg = (v, d) -> v / d;
    
    System.out.println( avg.apply(sum().apply(values), values.size()) );
~~~

- 커링을 통한 평균 값을 구하는 함수 avgCurry
~~~
    Function<Collection<Integer>, Function<Integer, Double>> avgCurry = list -> count -> {
        final double sum = list.stream().reduce(0, (a, b) -> a + b);
        return new Double(sum / count);
    };
~~~

  re.study.functionalprogramming.func.FunctionCollectionAdd
  re.study.functionalprogramming.func.CalculatorBiFunction

### Predicate 에 대한 이해
  re.study.functionalprogramming.predicate.PredicateInteger
  re.study.functionalprogramming.predicate.PredicateCollection
  re.study.functionalprogramming.predicate.PredicateExpireDate

3. Consumer 에 대한 간단한 이해
  re.study.functionalprogramming.consumer.ConsumerTest
  
4. Supplier 에 대한 간단한 이해
  re.study.functionalprogramming.supplier.SupplierTest
  re.study.functionalprogramming.supplier.SupplierLongTimeTest
  re.study.functionalprogramming.supplier.SupplierFactoryTest

5. CompletableFuture 의 이해
  re.study.functionalprogramming.jmonads.CompletableFutureTest
  re.study.functionalprogramming.jmonads.TaskAllCompletableFuture
  re.study.functionalprogramming.jmonads.TaskAnyCompletableFuture

6. Functor 에 대한 이해
어떤 값을 캡슐화하는 타입 인자를 가지는 자료구조이다.
컨텍스트에서 관리하고 있는 데이터를 타입(T)은 유지한 채 사용 목적에 따라 값을 변경 할 수 있는 함수(기능)를 제공 한다. 

~~~
import java.util.function.Function;

interface Functor<T> {
    <R> Functor<R> map(Function<T,R> f);
}

/* 구현 샘플 */
class Identity<T> 
    implements Functor<T, Identity<?>> {

    private final T value;

    Identity(T value) { 
        this.value = value; 
    }

    public T get() {
    	return this.value;
    }

    public <R> Identity<R> map(Function<T,R> f) {
        final R result = f.apply(value);
        return new Identity<>(result);
    }    
    
}

/* 샘플 */

Identity<String> val = new Identity<>("abc");
Identity<Integer> len = val.map(String::length);
~~~


6. Monad 에 대한 이해
flatMap 이 모나드 이다. 컨텍스트에서 관리하고 있는 타입과 그 타입을 다루는 펑션이, 사용 목적에 따라 변경이 가능한 기법이다.
 


https://github.com/jooyunghan
https://github.com/jasongoodwin/better-java-monads


