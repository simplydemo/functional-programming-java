package re.study.functionalprogramming;

import java.util.Date;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <pre>
 * UTC: Coordinated Universal Time 
 * GMT: Greenwich Mean Time 
 * KST: Korea Standard Time
 * UTC와 GMT는 초의 소숫점 단위에서만 차이가 나기 때문에 일상에서는 혼용되어 사용되며 기술적인 표기에서는 UTC가 사용됩니다.
 * </pre>
 * 
 * @author aider
 */
public class DateUtils {

    public static final String LOCAL_TIMEZONE = "Asia/Seoul";

    public static final String UTC_TIMEZONE = "UTC"; // ZoneId.of("UTC")

    public static final int TOKEN_EXPIRES_DAYS = 30;

    /**
     * Example: 2014-11-21 16:25:32
     */
    public static final String DEFAULT_TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    /**
     * Example: 16-06-11 10:10:44
     */
    public static final String LIST_TIMESTAMP_FORMAT = "yy-MM-dd HH:mm:ss";

    /**
     * Example: 2014-11-21T16:25:32-05:00
     */
    public static final String DEFAULT_UTC_TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    /**
     * Example: 2016-04-06T16:04:10.626+0900
     */
    public static final String DEFAULT_UTC_NANOSECONDS_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    private static final DateTimeFormatter formatter(final String format) {
        return DateTimeFormatter.ofPattern(format);
    }

    public static Date now() {
        return toDate(LocalDateTime.now());
    }

    public static LocalDateTime nowTime() {
        return LocalDateTime.now();
    }

    public static Instant nowInstant() {
        return ZonedDateTime.now().toInstant();
    }

    public static LocalDateTime toLocalTime(final long timestamp) {
        return DateUtils.toLocalTime(new Date(timestamp));
    }

    public static LocalDateTime toLocalTime(final Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static LocalDateTime plusMins(int minutes) {
        final LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        return now.plusMinutes(minutes);
    }

    public static LocalDateTime minusMinutes(int minutes) {
        final LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        return now.minusMinutes(minutes);
    }

    public static Long timeMillis(final LocalDateTime localDtm) {
        return DateUtils.toDate(localDtm).getTime();
    }

    public static LocalDateTime plusDays(int days) {
        final LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        return now.plusDays(days);
    }

    public static LocalDateTime plusDays(final Date date, int days) {
        final LocalDateTime localTime = DateUtils.toLocalTime(date);
        return localTime.plusDays(days);
    }

    public static LocalDateTime minusDays(int days) {
        final LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        return now.minusDays(days);
    }

    public static LocalDateTime minusDays(final Date date, int days) {
        final LocalDateTime localTime = DateUtils.toLocalTime(date);
        return localTime.minusDays(days);
    }

    /**
     * Returns a string value corresponding to the date format
     * 
     * @return value of formatted
     */
    public static String getFormattedString() {
        return DateUtils.nowTime().format(DateUtils.formatter(DateUtils.DEFAULT_TIMESTAMP_FORMAT));
    }

    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date toDate(LocalDateTime localDtm) {
        return Date.from(localDtm.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate toLocalDate(String value, String pattern) {
        final LocalDate date = LocalDate.parse(value, DateTimeFormatter.ofPattern(pattern));
        return date;
    }

    public static LocalDateTime toLocalDtm(String value, String pattern) {
        final LocalDateTime date = LocalDateTime.parse(value, DateTimeFormatter.ofPattern(pattern));
        return date;
    }

    public static Date toDate(final String value, String pattern) {
        if (pattern.length() <= 8) {
            final LocalDate localDate = DateUtils.toLocalDate(value, pattern);
            return DateUtils.toDate(localDate);
        }
        final LocalDateTime localDtm = DateUtils.toLocalDtm(value, pattern);
        return DateUtils.toDate(localDtm);
    }

    private static Duration duration(Date start, Date end) {
        return DateUtils.duration(DateUtils.toLocalTime(start), DateUtils.toLocalTime(end));
    }

    private static Duration duration(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start.toLocalTime(), end.toLocalTime());
    }

    public static long durationMinutes(Date start, Date end) {
        final Duration duration = DateUtils.duration(start, end);
        return duration.getSeconds() / 60;
    }

    public static boolean isExpired(final Long timestamp) {
        if (timestamp == null) {
            return false;
        }
        final LocalDateTime baseTime = DateUtils.toLocalTime(timestamp);
        return baseTime.isBefore(DateUtils.nowTime());
    }

    public static boolean isExpired(final Date date) {
        final LocalDateTime baseTime = DateUtils.toLocalTime(date);
        return baseTime.isBefore(DateUtils.nowTime());
    }

    public static boolean isExpired(final LocalDateTime baseTime) {
        return baseTime.isBefore(DateUtils.nowTime());
    }

    /**
     * Returns a string value corresponding to the date format
     *
     * @param format date format
     * @return value of formatted
     */
    public static String getFormattedString(String format) {
        return DateUtils.nowTime().format(DateUtils.formatter(format));
    }

    public static String getFormattedString(Long timestamp) {
        return DateUtils.getFormattedString(DateUtils.toLocalTime(timestamp));
    }

    public static String getFormattedString(Date date) {
        return DateUtils.getFormattedString(DateUtils.toLocalTime(date));
    }

    public static String getFormattedString(final LocalDateTime localDtm) {
        return localDtm.format(DateUtils.formatter(DateUtils.DEFAULT_TIMESTAMP_FORMAT));
    }

    /**
     * Returns a string value corresponding to the date format
     *
     * @param date Reference date
     * @param format date format
     * @return value of formatted
     */
    public static String getFormattedString(final Date date, final String format) {
        return DateUtils.getFormattedString(toLocalTime(date), format);
    }

    public static String getFormattedString(final LocalDateTime localDtm, final String format) {
        return localDtm.format(DateUtils.formatter(format));
    }

}
