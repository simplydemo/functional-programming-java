# functional-programming-java
함수형 프로그래밍의 핵심
1. 순수 함수로 작성 되어야 한다. 수학의 f(x) 와 동일 하다.
  (* 함수는 side-effect 를 가지지 않는다.)
function addOne(x){
	return x + 1;
}

function substractOne(x){
	return x - 1;
}

2. Immutability (불변성)을 보장 해야 한다.
  - 변수를 사용 하지 않는다.
  - 상태를 보존 하지 않는다.
    ( 1 ~ 10 까지 더한 값 55 를 구한다고 했을 때, 최종 결과값 55만 가지고 있으며, 1 부터 9 까지의 연산에 대한 상태 변경 등등에 대해선 가지고 있지 않는다. )
int var = 1;
function add(x){
	var = var + 1;
	return var;
}

3. Statement 를 사용 하지 않는다.
OOP 또는 명령형 프로그래밍은 주어진 문장(Statement)을 실행 한다. 반면, 
함수형 프로그래밍은 주어진 표현식(expression)을 계산(Evaluation) 하는 방식 으로 구성 된다.

4. Higher-Order Functions (HOFs) 
함수를  자신의 파라미터로 취하는 함수 이다.
  OOP 에서 함수는 값이나 객체를 인자로 주고, 값이나 객체를 결과로 받는다.
  FP 에서는, 함수를 인자로 주고, 함수를 인자로 받을 수 있을 뿐만 아니라, 함수를 배열이나 컬렉션에 담을수도 있다.
   즉, 함수가 값과 같이 취급 된다.
   
function add
function divide
function composition
function farrays = [add, devide];
function somefunction
composition(add, divide)
somefunction = composition(farrays)

5. Recursion
함수형 프로그래밍에선 재귀 호출이 빈번하게 사용된다. 때론 재귀 호출이 반복문을 대체할 수 있는 유일한 방법인 경우도 있으며, 주어진 함수를 사용해 원하는 값을 계산할 때 까지의 프로그램 흐름을 만들어가는 필수 장치다. 재귀 호출의 빈번한 사용에 따른 비용을 최소화하기 위해, 함수형 언어는 Tail Call(Tail Recursion) 기능을 통해 콜-스택의 불필요한 생성을 방지하며 메모리 낭비를 제거하는 등의 최적화 방안을 제공한다.

일반적인 합산 프로그램. 
var arr = [1, 2, 3, 4, 5];
var tot = 0;
arr.forEach(v) {
  tot+=v;
}

FP 를 통한 합산 Case 1:
function sum(tot, count){
	tot+=count;
	if(count > 0){
	   return sum(tot, count - 1)); 
	} else {
	   return tot;
	}	
} 
sum(0, 5)

FP 를 통한 합산 Case 2:
var arr = [1, 2, 3, 4, 5];
arr.reduce((a,b) => {return a+b;}, 0);


Quiz. 다음의 evenMatcher 은 함수형 프로그램에서 추구 하는 순수 함수 인가?

var evenMatcher = function(x) { return x % 2 === 0; }
var evenFunc = function(arr) { return arr.filter( evenMatcher ); };

var arr = [1, 2, 3, 4, 5];
evenFunc(arr);



살펴 보기 순서
1. Function 에 대한 간단한 이해
  re.study.functionalprogramming.func.FunctionAdd
  re.study.functionalprogramming.func.FunctionCollectionAdd
  re.study.functionalprogramming.func.CalculatorBiFunction

2. Predicate 에 대한 간단한 이해




https://github.com/jooyunghan
https://github.com/jasongoodwin/better-java-monads


