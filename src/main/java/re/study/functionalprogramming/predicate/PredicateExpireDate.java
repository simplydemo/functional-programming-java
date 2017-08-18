package re.study.functionalprogramming.predicate;

import java.util.Date;
import java.util.Objects;
import java.util.function.Predicate;

import java.time.LocalDateTime;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;
import re.study.functionalprogramming.DateUtils;

/**
 * 만료일 날짜 테스트
 */
@Slf4j
public class PredicateExpireDate {

    /**
     * 현재 날짜를 기준으로 만료 되었는지를 판단 하는 Predicate
     */
    final Predicate<Date> isExpired = d -> d.getTime() < System.currentTimeMillis();

    /**
     * 특정일을 'chkDate' 기준으로 만료 되었는지를 판단 하는 Predicate
     * @param chkDate
     * @return
     */
    public Predicate<Date> isExpired(final Date chkDate) {
        return d -> d.getTime() < (chkDate == null ? System.currentTimeMillis() : chkDate.getTime());
    }

    /**
     * ExpiredChecker 함수는 파라미터 타입에 따라 사용자가 직접 만료 여부를 정의 할 수 있도록 지원 한다.
     * @param <T1> 체크 대상 날짜 타입
     * @param <T2> 체크 기준 날짜 타입
     */
    @FunctionalInterface
    static interface ExpiredChecker<T1, T2> {
        public Boolean apply(T1 d1, T2 d);

        default ExpiredChecker<T1, T2> and(ExpiredChecker<T1, T2> other) {
            Objects.requireNonNull(other);
            return (t, t2) -> apply(t, t2) && other.apply(t, t2);
        }

    }

    ExpiredChecker<Date, Date> expiredDate = (d, chkDate) -> d.getTime() < chkDate.getTime();
    ExpiredChecker<LocalDateTime, LocalDateTime> expiredLocalDtm = (d, chk) -> d.isBefore(chk);

    @Test
    public void testPredicateExpired() {
        final Date licensedDate = DateUtils.toDate(DateUtils.nowTime().minusMinutes(-1));
        final Date now = DateUtils.now();
        log.info("isExpired 1: {}", isExpired.test(licensedDate));
        log.info("isExpired 2: {}", isExpired(now).test(licensedDate));
        log.info("isExpired 3: {}", expiredDate.apply(licensedDate, now));

        final LocalDateTime licensedDtm = DateUtils.nowTime().minusMinutes(-1);
        final LocalDateTime nowTime = DateUtils.nowTime();
        log.info("isExpired 4: {}", expiredLocalDtm.apply(licensedDtm, nowTime));
    }

}
