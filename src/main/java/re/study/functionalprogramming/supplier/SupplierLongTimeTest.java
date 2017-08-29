package re.study.functionalprogramming.supplier;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import java.time.LocalDateTime;

import org.junit.Test;

import re.study.functionalprogramming.advanced.DateMaps;

public class SupplierLongTimeTest {

    /**
     * 시간이 걸리는 작업 예로 여기선 3초간 걸리는 작업이라고 가정
     * @param val
     * @return
     */
    private static String task(Integer val) {
        try {
            TimeUnit.SECONDS.sleep(3);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Big Value " + String.valueOf(val);
    }

    /**
     * 하나의 작업이 3초 이므로 총 9초가 걸리는 작업이다.
     * @throws Exception
     */
    @Test
    public void ut1001_longTimeTask() throws Exception {
        DateMaps<LocalDateTime> dm = DateMaps.ofNow();
        task(1);
        task(2);
        task(3);
        long duration = DateMaps.of(dm.get()).duration().getSeconds();
        System.out.println("Elapsed SECS: " + duration);
    }

    /**
     * 특정 조건에 부합 하는 경우에만 작업을 하는 로직
     * @param num
     * @param supplier
     * @return
     */
    private static String taskSupplier(int num, Supplier<String> supplier) {
        if (num >= 3) {
            return supplier.get();
        }
        else {
            return null;
        }
    }

    /**
     * 특정 조건에 부합 하는 경우에만 작업을 하는 Supplier 의 샘플
     * @throws Exception
     */
    @Test
    public void ut1002_longTimeTaskWithSupplier() throws Exception {
        DateMaps<LocalDateTime> from = DateMaps.ofNow();
        taskSupplier(1, () -> task(1));
        taskSupplier(2, () -> task(2));
        taskSupplier(3, () -> task(3));
        long duration = DateMaps.of(from.get()).duration().getSeconds();
        System.out.println("Elapsed SECS: " + duration);
    }
}
