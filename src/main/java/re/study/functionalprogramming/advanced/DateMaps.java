package re.study.functionalprogramming.advanced;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateMaps<T> {

    /**
     * Example: 2014-11-21'T'16:25:32
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

    // ~ BASIC STRUCTURE : BEGIN --------------------------------
    private static final DateMaps<?> EMPTY = new DateMaps<>();

    private final T value;

    private DateMaps() {
        this.value = null;
    }

    private DateMaps(T val) {
        this.value = val;
    }

    public static <T> DateMaps<T> of(T value) {
        return new DateMaps<>(value);
    }

    @SuppressWarnings("unchecked")
    public static <T> DateMaps<T> of(final long epochMilli) {
        final LocalDateTime localDtm = LocalDateTime.ofInstant(new Date(epochMilli).toInstant(),
                ZoneId.systemDefault());
        return (DateMaps<T>) of(localDtm);
    }

    @SuppressWarnings("unchecked")
    public static <T> DateMaps<T> of(final String dateValue, final String format) {
        if (dateValue.length() <= 8) {
            final LocalDate date = LocalDate.parse(dateValue, DateTimeFormatter.ofPattern(format));
            return new DateMaps<>((T) LocalDateTime.of(date, LocalTime.MIN));
        }
        final LocalDateTime localDtm = LocalDateTime.parse(dateValue, DateTimeFormatter.ofPattern(format));
        return new DateMaps<>((T) localDtm);
    }

    public static <T> DateMaps<T> ofNow(final String dateValue) {
        return of(dateValue, DEFAULT_TIMESTAMP_FORMAT);
    }

    public static <T> DateMaps<T> ofNow() {
        final LocalDateTime now = LocalDateTime.now();
        @SuppressWarnings("unchecked")
        final DateMaps<T> dateMaps = new DateMaps<>((T) now);
        return dateMaps;
    }

    public static <T> DateMaps<T> ofNullable(T value) {
        return value == null ? empty() : of(value);
    }

    public boolean isPresent() {
        return value != null;
    }

    public static <T> DateMaps<T> empty() {
        @SuppressWarnings("unchecked")
        DateMaps<T> t = (DateMaps<T>) EMPTY;
        return t;
    }

    private LocalDateTime localDtm() {
        Objects.requireNonNull(value);
        if (value instanceof LocalDateTime) {
            return ((LocalDateTime) value);
        }
        throw new RuntimeException("value T must be LocalDateTime type.");
    }

    public T get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    // ~ BASIC STRUCTURE : E N D --------------------------------

    public <U> DateMaps<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent())
            return empty();
        else {
            return DateMaps.ofNullable(mapper.apply(value));
        }
    }

    Function<LocalDate, Date> localDateToDate = v -> Date
            .from(v.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    Function<LocalDateTime, Date> localDateTimeToDate = v -> Date.from(v.atZone(ZoneId.systemDefault()).toInstant());

    @SuppressWarnings("unchecked")
    public <U> DateMaps<U> toDate() {
        if (value instanceof LocalDate) {
            return (DateMaps<U>) map((Function<? super T, ? extends U>) localDateToDate);
        }
        return (DateMaps<U>) map((Function<? super T, ? extends U>) localDateTimeToDate);
    }

    Function<Date, Instant> dateToInstant = v -> v.toInstant();
    Function<Date, LocalDateTime> dateToLocalDtm = v -> LocalDateTime.ofInstant(v.toInstant(), ZoneId.systemDefault());
    Function<LocalDateTime, Instant> localDtmToInstant = v -> v.atZone(ZoneId.systemDefault()).toInstant();

    @SuppressWarnings("unchecked")
    public <U> DateMaps<U> toInstant() {
        if (value instanceof Date) {
            return (DateMaps<U>) map((Function<? super T, ? extends U>) dateToInstant);
        }
        return (DateMaps<U>) map((Function<? super T, ? extends U>) localDtmToInstant);
    }

    @SuppressWarnings("unchecked")
    public <U> DateMaps<U> plusMins(final long minutes) {
        Function<LocalDateTime, LocalDateTime> f = v -> v.plusMinutes(minutes);
        return (DateMaps<U>) map((Function<? super T, ? extends U>) f);
    }

    @SuppressWarnings("unchecked")
    public <U> DateMaps<U> minusMins(final long minutes) {
        Function<LocalDateTime, LocalDateTime> f = v -> v.minusMinutes(minutes);
        return (DateMaps<U>) map((Function<? super T, ? extends U>) f);
    }

    @SuppressWarnings("unchecked")
    public <U> DateMaps<U> plusHours(final long hours) {
        Function<LocalDateTime, LocalDateTime> f = v -> v.plusHours(hours);
        return (DateMaps<U>) map((Function<? super T, ? extends U>) f);
    }

    @SuppressWarnings("unchecked")
    public <U> DateMaps<U> minusHours(final long hours) {
        Function<LocalDateTime, LocalDateTime> f = v -> v.minusHours(hours);
        return (DateMaps<U>) map((Function<? super T, ? extends U>) f);
    }

    @SuppressWarnings("unchecked")
    public <U> DateMaps<U> plusDays(final long days) {
        Function<LocalDateTime, LocalDateTime> f = v -> v.plusDays(days);
        return (DateMaps<U>) map((Function<? super T, ? extends U>) f);
    }

    @SuppressWarnings("unchecked")
    public <U> DateMaps<U> minusDays(final long days) {
        Function<LocalDateTime, LocalDateTime> f = v -> v.minusDays(days);
        return (DateMaps<U>) map((Function<? super T, ? extends U>) f);
    }

    public Duration duration() {
        return duration(LocalDateTime.now());
    }

    Function<LocalDateTime, Duration> duration = v -> Duration.between(localDtm(), v);

    public Duration duration(final LocalDateTime endDtm) {
        return duration.apply(endDtm);
    }

    public long durationMinutes(Date end) {
        final Duration duration = duration(dateToLocalDtm.apply(end));
        return duration.toMinutes();
    }

    public long durationMinutes(LocalDateTime end) {
        final Duration duration = duration(end);
        return duration.toMinutes();
    }

    public long durationSeconds(LocalDateTime end) {
        final Duration duration = duration(end);
        return duration.getSeconds();
    }

    // public long durationMinutes(LocalDateTime end) {
    // final Duration duration = duration(end);
    // return duration.getSeconds() / 60;
    // }
    //
    // ChronoUnit.DAYS.between(birthday, today);

    public long durationHours(LocalDateTime end) {
        final Duration duration = duration(end);
        return duration.getSeconds() / 3600;
    }

    // public static long durationMinutes(Date start, Date end) {
    // final Duration duration = DateUtils.duration(start, end);
    // return duration.getSeconds() / 60;
    // }
    //
    public long toEpochMilli() {
        return ((Instant) toInstant().get()).toEpochMilli();
    }

    public boolean isExpired() {
        return isExpired(LocalDateTime.now());
    }

    public boolean isExpired(final Date date) {
        Objects.requireNonNull(date);
        final LocalDateTime dtm = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return isExpired(dtm);
    }

    public boolean isExpired(final LocalDateTime checkDtm) {
        LocalDateTime localDtm = localDtm();
        Objects.requireNonNull(checkDtm);
        return localDtm.isAfter(checkDtm);
    }

    public <U> DateMaps<U> format() {
        return format(DEFAULT_TIMESTAMP_FORMAT);
    }

    @SuppressWarnings("unchecked")
    public <U> DateMaps<U> format(final String format) {
        Function<T, String> f = v -> ((LocalDateTime) v).format(DateTimeFormatter.ofPattern(format));
        return (DateMaps<U>) map(f);
    }
}
