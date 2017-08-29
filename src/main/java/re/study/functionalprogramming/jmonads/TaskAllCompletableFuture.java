package re.study.functionalprogramming.jmonads;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.junit.Test;

/**
 * 동시에 n개의 처리를 진행 하고 모든처리가 완료 되면 다음을 진행 하는 예제
 */
public class TaskAllCompletableFuture {

    /**
     * 5 초 작업
     * @return
     */
    private String buildMessage() {
        try {
            Thread.sleep(5 * 1000L);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Completed!!";
    }

    /**
     * 하나의 작업이 5초가 걸린다. 3개의 작업을 처리 하므로 순차적으로 하게 되면 15초가 걸리지만 병렬로 처리 하면 5초가 걸릴 것이다.
     * 
     * @throws Exception
     */
    @Test
    public void allOfTest() throws Exception {
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(this::buildMessage);
        CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(this::buildMessage);
        CompletableFuture<String> cf3 = CompletableFuture.supplyAsync(this::buildMessage);
        List<CompletableFuture<String>> tasks = Arrays.asList(cf1, cf2, cf3);

        // @formatter:off
        CompletableFuture<Void> cf = 
                CompletableFuture.allOf(tasks.toArray(new CompletableFuture[3]))
                .thenApplyAsync(v -> tasks.stream()
                        .map(future -> future.join())
                        .collect(Collectors.toList()))
                .thenAcceptAsync(v -> v.forEach(message -> System.out.println(message)));
        // @formatter:on
        System.out.println("----- STEP 1");
        cf.get();
        System.out.println("----- STEP 2");
    }
}
