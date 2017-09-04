package re.study.functionalprogramming.spring.future;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.util.concurrent.ListenableFutureTask;

import org.junit.Test;

public class CompletableFutureUtilsTest {

    Callable<Object> SECS3 = () -> {
        System.out.println("TASK3 starting");
        try {
            Thread.sleep(3 * 1000L);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("TASK3 completed");
        return "TASK3 completed";
    };

    Callable<Object> SECS5 = () -> {
        try {
            Thread.sleep(5 * 1000L);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("TASK5 completed");
        return "TASK5 completed";
    };

    private static final String TASK_FAILED = "task failed";

    @Test
    public void completableFuture() throws Exception {
        CompletableFuture<?> cf = CompletableFutureUtils.toCompletableFuture(new ListenableFutureTask<>(SECS3))
                .thenCompose(x -> {
                    System.out.println(x);
                    // if (remoteServerAvailable(x)) {
                    // ListenableFuture<ResponseEntity<String>> payloadFuture2 =
                    // template.getForEntity(url2, String.class);
                    // ListenableFuture<ResponseEntity<String>> payloadFuture3 =
                    // template.getForEntity(url3, String.class);
                    // try {
                    // return CompletableFuturesUtil.toCompletableFuture(payloadFuture2)
                    // .thenCombine(CompletableFuturesUtil.toCompletableFuture(payloadFuture3),
                    // this::aggregatePayload);
                    // //aggregate the payloads are difficult, here only aggregate two
                    // payload. Imagine 10 payloads
                    // } catch (Exception e) {
                    // throw new RuntimeException(e);
                    // }
                    // } else {
                    // return CompletableFuture.completedFuture(TASK_FAILED);
                    // }

                    return CompletableFuture.completedFuture(TASK_FAILED);
                });

        System.out.println("----- BEGIN -----");
        cf.whenComplete((result, ex) -> {
            System.out.println("Result: " + result);
            System.out.println("Exception: " + ex);
        });
        cf.get();

        TimeUnit.SECONDS.sleep(6);

        System.out.println("----- E-N-D -----");
    }
}
