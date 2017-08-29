package re.study.functionalprogramming.jmonads;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * java.util.concurrent.CompletableFuture 는 동기식 처리 프로세스를 비동기식 처리를 해주는 API로, 비동기 타스크간의 연결,
 * 중간에 발생하는 예외 처리가 어려웠는데 이것을 해결하기 위해 등장.
 * 
 * <pre>
 * 1. 명시적 쓰레드 선언없이 쓰레드를 사용할수 있다.
 * 2. 함수형 프로그래밍 방식으로 비동기적으로 동시성/병렬 프로그래밍을 가능하게 함으로서 의도를 명확하게 드러내는 함축적인 프로그래밍을 가능하게 한다.
 * 3. 각 타스크마다 순서적 연결을 할수도 있고, 타스크의 예외 처리도 가능하다.
 * </pre>
 */
@Slf4j
public class CompletableFutureTest {

    @Test
    public void ut1001_whenComplete() throws Exception {
        CompletableFuture<Integer> future = CompletableFuture.completedFuture(47);
        CompletableFuture<Integer> future1 = future.whenComplete((r, e) -> {
            System.out.println(r);
            Optional.ofNullable(e).ifPresent(Throwable::printStackTrace);
        });
        System.out.println(future1.get());
    }

    /**
     * CompletableFuture.supplyAsync 는 지정된 Supplier를 호출하여 얻은 값으로 실행중인 작업에서 비동기 적으로 완료 되는 새
     * CompletableFuture를 반환 합니다.
     * @throws Exception
     */
    @Test
    public void ut1002_exceptionally_success() throws Exception {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 10 / 2);
        CompletableFuture<Integer> future1 = future.exceptionally(e -> -1);
        log.info("result: " + future1.get());
    }

    /**
     * "java.lang.ArithmeticException: / by zero" 예외를 발생 시킨 결과 이다.
     * @throws Exception
     */
    @Test
    public void ut1002_exceptionally_failure() throws Exception {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 10 / 0);
        CompletableFuture<Integer> future1 = future.exceptionally(e -> -1);
        log.info("result: " + future1.get());
    }

    /**
     * thenApply 결과에 함수를 적용 한다. 두개의 task를 순서를 보장하여 pipe로 연결 한다.
     * @throws Exception
     */
    @Test
    public void ut1003_thenApply() throws Exception {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getId());
            return 10 / 2;
        }, Executors.newScheduledThreadPool(5));
        CompletableFuture<Integer> future1 = future.thenApply(i -> {
            System.out.println(Thread.currentThread().getId());
            return i * 4;
        });
        log.info("result: " + future1.get());
    }

    /**
     * 타입이 서로 다른 두개의 task를 순서를 보장하여 pipe로 연결 한다.
     * @throws Exception
     */
    @Test
    public void ut1004_thenApply() throws Exception {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 10 / 2);
        CompletableFuture<String> future1 = future.thenApply(i -> i + "-a");
        log.info("result: " + future1.get());
    }

    /**
     * thenApplyAsync 결과에 함수를 비동기적으로 적용 한다(서로 다른 Thread 일 수 있다). 두개의 task를 순서를 보장하여 pipe로
     * 연결 한다.
     * @throws Exception
     */
    @Test
    public void ut1005_thenApplyAsync() throws Exception {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getId());
            return 10 / 2;
        }, Executors.newScheduledThreadPool(5));
        CompletableFuture<Integer> future1 = future.thenApplyAsync(i -> {
            System.out.println(Thread.currentThread().getId());
            return i * 4;
        });
        log.info("result: " + future1.get());
    }

    /**
     * thenApplyAsync 처리 실패의 경우
     * @throws Exception
     */
    @Test
    public void ut1005_thenApplyAsync_failure() throws Exception {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 10 / 0);
        CompletableFuture<Integer> future1 = future.thenApplyAsync(i -> i * 4);
        log.info("result: " + future1.get());
    }

    /**
     * Void 타입 이다.
     * @throws Exception
     */
    @Test
    public void ut1006_thenAcceptAsync() throws Exception {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 10 / 2);
        CompletableFuture<Void> future1 = future.thenAcceptAsync(System.out::println);

        System.out.println(future1.get());
    }

    /**
     * 두개의 task를 처리 하는 Void 타입의 CompletableFuture
     * @throws Exception
     */
    @Test
    public void ut1007_thenAcceptBoth() throws Exception {
        // @formatter:off
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 10 / 2);
        CompletableFuture<Void> future1 = future.thenAcceptBoth(
                CompletableFuture.supplyAsync(() -> "other"), 
                (i, s) -> System.out.println(i + s));
        // @formatter:on
        System.out.println(future1.get());
    }

    /**
     * 두 개의 Future 를 하나의 Future 로 합성
     * @throws Exception
     */
    @Test
    public void ut1007_thenCompose() throws Exception {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> 10 / 2);
        CompletableFuture<Integer> future1 = future.thenCompose(i -> {
            System.out.println(i);
            return CompletableFuture.supplyAsync(() -> i * 7);
        });

        System.out.println(future1.get());
    }

    /**
     * 두 개의 Future 를 새로운 Future 로 조합 하여 생성
     * @throws Exception
     */
    @Test
    public void ut1008_thenCombine() throws Exception {
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> 10 / 2);
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "a");
        CompletableFuture<String> future = future1.thenCombine(future2, (i, s) -> i + s);
        System.out.println(future.get());
    }

    /**
     * CompletableFuture.join은 CompletableFuture가 감싸고 있는 값을 드러 낸다.
     * @throws Exception
     */
    @Test
    public void ut1008_join() throws Exception {
        List<Integer> list = Arrays.asList(10, 20, 30, 40);
        // @formatter:off
        list.stream()
                .map(v -> CompletableFuture.supplyAsync(() -> v * v))
                .map(f -> f.thenApply(n -> n * n))
                .map(f -> f.join())
                .forEach(s -> log.info("{}", s));
        // @formatter:on
    }

    /**
     * CompletableFuture.getNow 현재 작업 처리내역을 반환 합니다. 작업이 완료 했을 경우는 결과의 값을 돌려 주고, 그렇지 않은 경우는
     * valueIfAbsent(주어진 값)을 반환 합니다. 만약, 예외가 발생 하면 예외를 Throw합니다.
     * @throws Exception
     */
    @Test
    public void ut1009_getNow() throws Exception {
        List<String> list = Arrays.asList("A", "B", "C", "D");
        list.stream().map(s -> CompletableFuture.supplyAsync(() -> s + s)).map(f -> f.getNow("Not Done"))
                .forEach(s -> System.out.println(s));
    }

    /**
     * 실행 결과는 아래와 같다. 여기엔 두개의 task 가 존재 한다. 하나는 "Hello!" 를 출력 하는 task 이고, 또다른 하나는 "World"
     * 를 출력 하는 task 이다. 이 둘의 task 가 자연스럽게 연결 되었고, 순서도 보장 하고 있다.
     * 
     * <pre>
    RESULT)
    async request is ready.
    -- 3초 대기(sleep)
    Hello! 
    -- 3초 대기(sleep)
    World
     * </pre>
     * 
     * 프로그램 명세를 보면 1. Executor를 통해 비동기 타스크가 수행될 쓰레드를 생성 하고, 2. CompletableFuture.runAsync를
     * 통해 다른 쓰레드에서 비동기 식으로 동작할 로직를 선언 하고, 3. CompletableFuture.thenRun 를 통해 첫번째 타스크가 완료된
     * 이후에 연속적으로 동작할 로직을 선언 했다.
     * 
     * @throws Exception
     */
    @Test
    public void ut1001_basic() throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        CompletableFuture<Void> f = CompletableFuture.runAsync(() -> {
            // STEP 1
            try {
                Thread.sleep(3000);
            }
            catch (Exception e) {
            }
            // STEP 2
            log.info("Hello!");
            // STEP 3
            try {
                Thread.sleep(3000);
            }
            catch (Exception e) {
            }
        }, executor).thenRun(() -> log.info("World") /* STEP 4 */ );
        log.info("async request is ready.");
        f.get();
    }

}
