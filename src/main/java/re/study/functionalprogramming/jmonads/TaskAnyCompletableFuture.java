package re.study.functionalprogramming.jmonads;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.junit.Test;

/**
 * 동시에 n개의 처리를 진행 하고 n개의 작업중 하나라도 처리가 완료 되면 다음을 진행 하는 예제
 */
public class TaskAnyCompletableFuture {
    /**
     * 5 초 작업
     * @return
     */
    private String buildMessage(long interval) {
        try {
            Thread.sleep(interval * 1000L);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Completed!! " + interval;
    }

    /**
     * 5, 15, 3초가 걸리는 3개의 작업이 있다. 이 중 가장 짧은 작업 시간은 3초 이므로 "STEP 2"는 3초 뒤에 로그가 찍힐 것이다.
     * 
     * @throws Exception
     */
    @Test
    public void anyOfTest() throws Exception {
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> buildMessage(5));
        CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(() -> buildMessage(15));
        CompletableFuture<String> cf3 = CompletableFuture.supplyAsync(() -> buildMessage(3));
        List<CompletableFuture<String>> tasks = Arrays.asList(cf1, cf2, cf3);

        // @formatter:off
        CompletableFuture<Void> cf = 
                CompletableFuture.anyOf(tasks.toArray(new CompletableFuture[3]))
                .thenApplyAsync(s -> tasks.stream()
                         .filter( v -> v.isDone() )
                         .map(future -> future.join())
                         .collect(Collectors.toList())
                        )
                .thenAcceptAsync(
                        // s -> System.out.println(s) 
                        s -> s.forEach(m -> System.out.println(m))
                        );
        // @formatter:on
        System.out.println("----- STEP 1");
        cf.get();
        System.out.println("----- STEP 2");
    }
}
