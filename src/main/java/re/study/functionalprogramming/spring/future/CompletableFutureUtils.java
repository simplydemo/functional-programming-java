package re.study.functionalprogramming.spring.future;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * ListenableFuture 를 CompletableFuture로 변환 하는 유틸리티
 */
public class CompletableFutureUtils {

    public static <T> CompletableFuture<T> toCompletableFuture(ListenableFuture<T> listenableFuture) {

        CompletableFuture<T> completableFuture = new CompletableFuture<>();

        listenableFuture.addCallback(
                // onSuccess
                completableFuture::complete,
                // onFailure
                completableFuture::completeExceptionally);

        return completableFuture;
    }

    public static <T> CompletableFuture<T> buildCompletableFuture(final ListenableFuture<T> listenableFuture) {
        CompletableFuture<T> completableFuture = new CompletableFuture<T>() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                boolean result = listenableFuture.cancel(mayInterruptIfRunning);
                super.cancel(mayInterruptIfRunning);
                return result;
            }
        };
        listenableFuture.addCallback(new ListenableFutureCallback<T>() {
            @Override
            public void onFailure(Throwable ex) {
                completableFuture.completeExceptionally(ex);
            }

            @Override
            public void onSuccess(T result) {
                completableFuture.complete(result);
            }
        });
        return completableFuture;
    }

    public static <T> CompletableFuture<T> doNothingCompletableFuture() {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        completableFuture.complete(null);
        return completableFuture;
    }

    /**
     * <code>
    String url1 = "http://localhost:8081/service?req={req}";
    String url2 = "http://localhost:8081/service2?req={req}";
    return buildHttpCompletableFuture(rt.getForEntity(url1, String.class, idx))
            .thenCompose(r -> buildCompletableFuture(rt.getForEntity(url2, String.class, r)))
            .exceptionally(ex -> ex.getMessage());</code>
     * 
     * @param listenableFuture
     * @return
     */
    public static <T> CompletableFuture<T> buildHttpCompletableFuture(
            final ListenableFuture<ResponseEntity<T>> listenableFuture) {
        CompletableFuture<T> completableFuture = new CompletableFuture<T>() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                boolean result = listenableFuture.cancel(mayInterruptIfRunning);
                super.cancel(mayInterruptIfRunning);
                return result;
            }
        };

        // add callback
        listenableFuture.addCallback(new ListenableFutureCallback<ResponseEntity<T>>() {
            @Override
            public void onSuccess(ResponseEntity<T> result) {
                completableFuture.complete(result.getBody());
            }

            @Override
            public void onFailure(Throwable ex) {
                completableFuture.completeExceptionally(ex);
            }
        });

        return completableFuture;
    }
}
