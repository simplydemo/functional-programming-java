package re.study.functionalprogramming.advanced;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 날짜 함수 응용
 */
@Slf4j
public class DateMapsTest {

    @Test
    public void testDateMaps() throws Exception {
        log.info("DateMaps.of( object ): {}", DateMaps.of(LocalDateTime.now()).get());
        log.info("DateMaps.ofNow(): {}", DateMaps.ofNow().get());
        log.info("DateMaps.ofNow().format(): {}", DateMaps.ofNow().format().get());
        log.info("DateMaps.ofNow().format( pattern ): {}", DateMaps.ofNow().format("yyyy-MM-dd").get());
        log.info("DateMaps.ofNow( dateValue ): {}", DateMaps.ofNow("2015-08-18T15:31:12").format().get());

        log.info("DateMaps.of(1503043989344): {}", DateMaps.of(1503043989344L).get());
        log.info("DateMaps.of(dateValue, pattern): {}", DateMaps.of("20100101", "yyyyMMdd").get());
        log.info("DateMaps.of(dateValue, pattern).format( pattern ): {}",
                DateMaps.of("20100101", "yyyyMMdd").format("yyyy-MM-dd HH:mm:ss").get());

        log.info("DateMaps.ofNow().toInstant(): {}", DateMaps.ofNow().toInstant().get().getClass());
        log.info("DateMaps.ofNow().toDate(): {}", DateMaps.ofNow().toDate().get().getClass());
        log.info("DateMaps.ofNow().minusHours(100): {}", DateMaps.ofNow().minusHours(100).format().get());
        log.info("DateMaps.ofNow().plusHours(100): {}", DateMaps.ofNow().plusHours(100).format().get());
        log.info("DateMaps.ofNow().isExpired(): {}", DateMaps.ofNow().isExpired());
        log.info("DateMaps.ofNow().plusMins(1).isExpired(): {}", DateMaps.ofNow().plusMins(1).isExpired());
        log.info("DateMaps.ofNow().minusMins(1).isExpired(): {}", DateMaps.ofNow().minusMins(1).isExpired());
        log.info("DateMaps.ofNow().minusMins(1).toEpochMilli(): {}", DateMaps.ofNow().minusMins(1).toEpochMilli());

        log.info("DateMaps.ofNow().minusHours(10).durationMinutes(): {}",
                DateMaps.ofNow().minusHours(10).durationMinutes(LocalDateTime.now()));
        log.info("DateMaps.ofNow().minusDays(1).durationMinutes(): {}",
                DateMaps.ofNow().minusDays(1).durationMinutes(LocalDateTime.now()));
        log.info("DateMaps.ofNow().minusDays(10).duration.getSeconds(): {}",
                DateMaps.ofNow().minusDays(10).duration(LocalDateTime.now()).getSeconds());
        log.info("DateMaps.ofNow().minusMins(1).duration.getSeconds(): {}",
                DateMaps.ofNow().minusMins(1).duration(LocalDateTime.now()).getSeconds());

        log.info("DateMaps.ofNow().map -> format: {}",
                DateMaps.ofNow().map(v -> ((LocalDateTime) v).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).get());

    }
}
