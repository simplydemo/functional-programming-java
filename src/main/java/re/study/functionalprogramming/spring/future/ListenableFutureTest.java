package re.study.functionalprogramming.spring.future;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.util.concurrent.ListenableFutureTask;

import org.junit.Test;

/**
 * 내일 이어서 살펴 보자구... https://dzone.com/articles/converting-listenablefutures
 * 
 * ListenableFutureTask 의 특징은, onSuccess, onFailure 결과 이벤트에 대해 을 nested 구조로 중첩 해서 다른 작업들을
 * 연결 시켜 놓을 수 있다는 점이다.
 * 
 * 호출 단계가 많아질 수록 indentation 에 의해서 소스 코드가 우측으로 후퇴한다. 일반적인 Code 포메팅 표준에서 어긋 나는 사항 이며 모니터
 * 크기가 아주 크지 않다면 상당히 불편해 질 것이다. 예외 처리를 매 호출 단계 마다 하고 있는 점도 부담 스럽다.
 * 
 * callback hell 중첩된 tasks 들 중 하나에서 예외가 발생 하거나 특정 단계를 trace 하는 경우에서 HELL 을 경험 하게 될 것이다.
 * http://callbackhell.com/ <code>
asyncFunction1(function(input, result1) {
  asyncFunction2(function(result2) {
    asyncFunction3(function(result3) {
      asyncFunction4(function(result4) {
        asyncFunction5(function(output) {
            // finally, do something...
        });
      });
    });
  });
});</code>
 */
public class ListenableFutureTest {

    private Callable<Object> SECS3 = () -> {
        try {
            Thread.sleep(3 * 1000L);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("TASK3 completed");
        return "TASK3 completed";
    };

    private Callable<Object> SECS5 = () -> {
        try {
            Thread.sleep(5 * 1000L);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("TASK5 completed");
        return "TASK5 completed";
    };

    @Test
    public void listenableFuture() throws Exception {
        ListenableFutureTask<Object> listenableFutureTask = new ListenableFutureTask<>(SECS5);

        listenableFutureTask.addCallback(new ListenableFutureCallback<Object>() {
            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("exception occurred!!");
            }

            @Override
            public void onSuccess(Object o) {
                ListenableFutureTask<Object> listenableFuture = new ListenableFutureTask<>(SECS3);
                listenableFuture.addCallback(new ListenableFutureCallback<Object>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        System.out.println("exception occurred!!");
                    }

                    @Override
                    public void onSuccess(Object o) {
                        System.out.println("all tasks completed!!");
                    }
                });
                listenableFuture.run();
            }
        });
        System.out.println("start ----- listenableFuture");
        System.out.println("----- STEP 1");
        listenableFutureTask.run();
        System.out.println("----- STEP 2");
    }

    @Test
    public void completableFuture() throws Exception {
        CompletableFuture<Object> cf1 = CompletableFutureUtils
                .buildCompletableFuture(new ListenableFutureTask<>(SECS3));
        // cf1.runAsync(() -> System.out.println("abc"));
        // CompletableFuture<Object> cf2 = AsyncUtils.buildCompletableFuture(new
        // ListenableFutureTask<>(SECS5));

        System.out.println("start ----- completableFuture");
        System.out.println("----- STEP 1");
        cf1.get();
        System.out.println("----- STEP 2");

        // List<CompletableFuture<Object>> tasks = Arrays.asList(cf1, cf2);

        // @formatter:off
//        CompletableFuture<Void> cf = 
//                CompletableFuture.anyOf(tasks.toArray(new CompletableFuture[2]))
//                .thenApplyAsync(v -> tasks.stream()
//                        // .map(future -> future.join())
//                        // .collect(Collectors.toList())
//                        )
//                .thenAcceptAsync(v -> v.forEach(message -> System.out.println(message)));
        // @formatter:on
    }

}
