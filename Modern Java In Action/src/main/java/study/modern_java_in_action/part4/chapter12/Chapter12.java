package study.modern_java_in_action.part4.chapter12;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.*;
import java.util.Locale;

/**
 * Chapter.12 새로운 날짜와 시간 API
 */

/* 기존 자바의 날짜와 시간 API
    - 자바는 `java.util.Date` 클래스 하나로 날자와 시간 관련 기능을 제공했다.
        - 날짜를 의미하는 `Date`라는 클래스는 특정 시점을 날짜가 아닌 밀리초 단위로 표현한다.
        - 1900년을 기준으로 하는 오프셋, 0에서 시작하는 달 인덱스 등 모호한 설계
        - `toString`의 활용이 어려움.
    - `java.util.Calendar`
        - 안타깝게도 `Calendar` 클래스 역시 쉽게 에러를 일으키는 설계 문제를 갖고 있었다.
        - 여전히 달의 인덱스 시작은 0
        - `Date` 클래스와의 혼란
    - `DateFormat`
        - `Date`에서만 작동하는 몇몇 기능
        - 스레드 안전하지 않음.
    - `Date`, `Calendar` 모두 가변 클래스
 */
@Slf4j
public class Chapter12 {

    /**
     * 12.1 LocalDate, LocalTime, Instant, Duration, Period 클래스
     */

    /* 12.1.1 `LocalDate`와 `LocalTime` 사용
        - `LocalDate`는 시간을 제외한 날짜를 표현하는 불변 객체
            - 어떠한 시간대 정보도 포함하지 않음.
            - 정적 팩토리 메서드 `of`로 인스턴스 생성 가능
            - `TemporalField`를 전달해서 정보를 얻을 수도 있다.
                - 시간 관련 객체에서 어떤 필드의 값에 접근할지 정의하는 인터페이스
                - `ChronoField`는 `TemporalField` 인터페이스를 정의
        - `LocalTime`은 시간을 표현할 수 있다.
            - 정적 팩토리 메서드 `of`
                - 시간, 분을 받는 of
                - 시간, 분, 초를 받는 of
        - `DateTimeFormatter`는 날짜, 시간 객체의 형식을 지정한다.
     */
    @Test
    void localDateExample() {
        // LocalDate
        LocalDate date = LocalDate.of(2022, 11, 9);
        int year1 = date.getYear();
        Month month1 = date.getMonth();
        int day1 = date.getDayOfMonth();
        DayOfWeek dow = date.getDayOfWeek();
        int len = date.lengthOfMonth();
        boolean leap = date.isLeapYear();

        LocalDate today = LocalDate.now();

        // `TemporalField`를 전달해서 정보를 얻을 수도 있다.
        int year2 = date.get(ChronoField.YEAR);
        int month2 = date.get(ChronoField.MONTH_OF_YEAR);
        int day2 = date.get(ChronoField.DAY_OF_MONTH);

        // 내장 메서드를 통해 위의 코드의 가독성을 높일 수 있다.
        int year3 = date.getYear();
        int month3 = date.getMonthValue();
        int day3 = date.getDayOfMonth();


        // LocalTime
        LocalTime time = LocalTime.of(13, 45, 20);
        int hour = time.getHour();
        int minute = time.getMinute();
        int second = time.getSecond();


        // parse 정적 메서드
        // 파싱 불가일 경우 `DateTimeParseException`을 일으킴
        LocalDate date2 = LocalDate.parse("2022-11-09");
        LocalTime time2 = LocalTime.parse("21:14:20");
    }

    /* 12.1.2 날짜와 시간 조합
        - `LocalDateTime`은 `LocalDate`와 `LocalTime`을 쌍으로 갖는 복합 클래스이다.
            - 날짜와 시간을 모두 표현할 수 있다.
            - `LocalDate`와 `LocalTime`으로 `LocalDateTime` 생성 가능
            - 반대로 `LocalDateTime`에서 `LocalDate`와 `LocalTime` 생성 가능
     */
    @Test
    void localDateTimeExample() {
        LocalDate date = LocalDate.of(2022, 11, 9);
        LocalTime time = LocalTime.of(13, 45, 20);

        LocalDateTime dt1 = LocalDateTime.of(2017, Month.SEPTEMBER, 21, 13, 45, 20);
        LocalDateTime dt2 = LocalDateTime.of(date, time);
        LocalDateTime dt3 = date.atTime(13, 45, 20);
        LocalDateTime dt4 = date.atTime(time);
        LocalDateTime dt5 = time.atDate(date);

        LocalDate date1 = dt1.toLocalDate();
        LocalTime time1 = dt1.toLocalTime();
    }

    /* 12.1.3 Instant 클래스 : 기계의 날짜와 시간
        - 기계적인 관점에서 시간 표현
        - 유닉스 에포크 시간(Unix Epoch time, 1970년 1월 1일 0시 0분 0초 UTC)을 기준으로 특정 지점까지의 시간을 초로 표현
        - `ofEpochSecond`에 초를 줘서 `Instant`클래스 인스턴스 생성 가능
            - 나노초(10억분의 1초) 정밀도 제공
            - 두 번째 인수를 이용해 나노초 단위로 시간 보정 가능
        - `Instant`는 사람이 읽을 수 있는 시간 정보를 제공하지 않음.
     */
    @Test
    void instantExample() {
        Instant.ofEpochSecond(3);
        Instant.ofEpochSecond(3, 0);
        Instant.ofEpochSecond(2, 1_000_000_000);
        Instant.ofEpochSecond(4, -1_000_000_000);
    }

    /* 12.1.4 `Duration`과 `Period` 정의
        - `Temporal` 인터페이스
            - 특정 시간을 모델링하는 객체의 값을 어떻게 읽고 조작할지 정의
        - `Duration`
            - 두 시간 객체 사이의 지속시간 표시
            - `between`으로 두 시간 객체 사이의 지속시간을 만들 수 있음.
                - `LocalTime`, `LocalDateTime, `Instant` 사이의 시간
            - 초와 나노초 시간 단위를 표현
                - `LocalDate`는 사용할 수 없다.
        - `Period`
            - 년, 월, 일로 시간을 표현할 때는 `Period` 클래스 사용
        -
     */
    @Test
    void durationExample() {
        LocalDateTime dateTime1 = LocalDateTime.of(2017, 9, 21, 13, 45, 20);
        LocalDateTime dateTime2 = LocalDateTime.of(2018, 9, 21, 13, 45, 20);
        LocalTime time1 = LocalTime.of(13, 45, 20);
        LocalTime time2 = LocalTime.of(14, 45, 20);
        Instant instant1 = Instant.ofEpochSecond(3);
        Instant instant2 = Instant.ofEpochSecond(4);

        Duration d1 = Duration.between(time1, time2);
        Duration d2 = Duration.between(dateTime1, dateTime2);
        Duration d3 = Duration.between(instant1, instant2);

        Duration threeMinutes1 = Duration.ofMinutes(3);
        Duration threeMinutes2 = Duration.of(3, ChronoUnit.MINUTES);
    }

    @Test
    void periodExample() {
        Period tenDays1 = Period.between(LocalDate.of(2017, 9, 11),
                LocalDate.of(2017, 9, 21));

        Period tenDays2 = Period.ofDays(10);
        Period threeWeeks = Period.ofWeeks(3);
        Period twoYearsSixMonthsOneDay = Period.of(2, 6, 1);
    }

    /*
        - LocalDate, LocalTime, Instant, Duration, Period 클래스는 모두 불변이다.
            - 스레드 안전성과 도메인 모델의 일관성을 유지하기 좋다.
     */


    /**
     * 12.2 날짜 조정, 파싱, 포매팅
     */

    @Test
    void withAttributeExample() {
        // with 메서드는 기존 객체를 바꾸지 않는다.
        // with 메서드는 기존 `Temporal` 객체를 바꾸는 것이 아니라 필드를 갱신한 복사본을 만든다. (= 함수형 갱신)
        // get, with 메서드로 `Temporal` 객체의 필드값을 읽거나 고칠 수 있다.
        // 새 변수에 할당하지 않으면 아무 일도 안 일어난다.
        LocalDate date1 = LocalDate.of(2017, 9, 21); // 2017-09-21
        LocalDate date2 = date1.withYear(2011); // 2011-09-21
        LocalDate date3 = date2.withDayOfMonth(25); // 2011-09-25
        LocalDate date4 = date3.with(ChronoField.MONTH_OF_YEAR, 2); // 2011-02-25
    }

    @Test
    void changeAttributeExample() {
        // 새 변수에 할당하지 않으면 아무 일도 안 일어난다.
        LocalDate date1 = LocalDate.of(2017, 9, 21); // 2017-09-21
        LocalDate date2 = date1.plusWeeks(1); // 2017-09-28
        LocalDate date3 = date2.minusYears(6); // 2011-09-28
        LocalDate date4 = date3.plus(6, ChronoUnit.MONTHS); // 2012-03-28
    }

    /* 12.2.1 TemporalAdjusters 사용하기
        - 복잡한 날짜 조정 기능 지원
        - 오버로드된 버전의 with 메서드에 좀 더 다양한 동작을 수행할 수 있도록 하는 기능 제공
        - 커스텀 하고 싶다면 `TemporalAdjuster` 인터페이스를 구현 (함수형 인터페이스)
            - `Temporal` 객체를 어떻게 다른 `Temporal` 객체로 변환할지 정의
     */
    @Test
    void temporalAdjusterExample() {
        LocalDate date1 = LocalDate.of(2014, 3, 18); // 2014-03-18
        LocalDate date2 = date1.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)); // 2014-03-23
        LocalDate date3 = date2.with(TemporalAdjusters.lastDayOfMonth()); // 2014-03-31
    }

    public class NextWorkingDay implements TemporalAdjuster {
        @Override
        public Temporal adjustInto(Temporal temporal) {
            DayOfWeek dow = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));
            int dayToAdd = 1;
            if (dow == DayOfWeek.FRIDAY) {
                dayToAdd = 3;
            } else if (dow == DayOfWeek.SATURDAY) {
                dayToAdd = 2;
            }
            return temporal.plus(dayToAdd, ChronoUnit.DAYS);
        }
    }

    @Test
    void nextWorkingDay() {
        // 위의 클래스를 람다로 표현
        LocalDate localDate = LocalDate.now();
        localDate = localDate.with(temporal -> {
            DayOfWeek dow = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));
            int dayToAdd = 1;
            if (dow == DayOfWeek.FRIDAY) {
                dayToAdd = 3;
            } else if (dow == DayOfWeek.SATURDAY) {
                dayToAdd = 2;
            }
            return temporal.plus(dayToAdd, ChronoUnit.DAYS);
        });

        // `TemporalAjuster`를 람다 표현식으로 정의하고 싶다면
        TemporalAdjuster nextWorkingDay = TemporalAdjusters.ofDateAdjuster(
            temporal -> {
                DayOfWeek dow = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));
                int dayToAdd = 1;
                if (dow == DayOfWeek.FRIDAY) {
                    dayToAdd = 3;
                } else if (dow == DayOfWeek.SATURDAY) {
                    dayToAdd = 2;
                }
                return temporal.plus(dayToAdd, ChronoUnit.DAYS);
            }
        );
        localDate = localDate.with(nextWorkingDay);
    }

    /* 12.2.2 날짜와 시간 객체 출력과 파싱
        - 날짜와 시간에서는 포매팅과 파싱은 필수이다.
        - 포매팅 전용 패키지 java.time.format
            - `DateTimeFormatter` 클래스가 가장 중요
                - 정적 팩토리 메서드와 상수를 이용해 손쉽게 포매터를 만들 수 있다.
                - 날짜나 시간을 특정 형식의 문자열로 만들 수 있다.
                - 스레드 안전하게 사용할 수 있다.
        - 날짜나 시간을 표현하는 문자열을 파싱해서 날짜 객체를 만들 수 있다.
            - 날짜와 시간 API에서 특정 시점이나 간격을 표현하는 모든 클래스의 팩토리 메서드 parse를 이용해서 문자열을 날짜 객체로 만들 수 있다.
        - `LocalDate`의 `format`메서드는 요청 형식의 패턴에 해당하는 문자열을 생성한다.
        - `DateTimeFormatterBuilder` 클래스로 복합적인 포매터를 정의해서 좀 더 세부적으로 포매터를 제어할 수 있다.
            - 대소문자를 구분하는 파싱, 관대한 규칙을 적용하는 파싱, 패딩, 포매터의 선택사항 등을 활용할 수 있다.
     */
    @Test
    void dateTimeFormatterAndParseExample() {
        LocalDate date = LocalDate.of(2014, 3, 18);
        String s1 = date.format(DateTimeFormatter.BASIC_ISO_DATE); // 20140318
        String s2 = date.format(DateTimeFormatter.ISO_LOCAL_DATE); // 20140318

        LocalDate date1 = LocalDate.parse("20140318", DateTimeFormatter.BASIC_ISO_DATE);
        LocalDate date2 = LocalDate.parse("2014-03-18", DateTimeFormatter.ISO_LOCAL_DATE);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate1 = date.format(formatter); // 18/03/2014
        log.info("formattedDate1 : {}", formattedDate1);
        LocalDate date3 = LocalDate.parse(formattedDate1, formatter);

        DateTimeFormatter italianFormatter = DateTimeFormatter.ofPattern("d. MMMM yyyy", Locale.ITALIAN);
        String formattedDate2 = date.format(italianFormatter); // 18. marzo 2014
        log.info("formattedDate2 : {}", formattedDate2);
        LocalDate date4 = LocalDate.parse(formattedDate2, italianFormatter);

        DateTimeFormatter customItalianFormatter = new DateTimeFormatterBuilder()
                .appendText(ChronoField.DAY_OF_MONTH)
                .appendLiteral(". ")
                .appendText(ChronoField.MONTH_OF_YEAR)
                .appendLiteral(" ")
                .appendText(ChronoField.YEAR)
                .parseCaseInsensitive()
                .toFormatter(Locale.ITALIAN);
    }

    /**
     * 12.3 다양한 시간대와 캘린더 활용 방법
     */

    /* java.time.ZoneId
        - 새로운 날짜와 시간 API를 통해 시간대를 간단하게 처리할 수 있다.
        - 불변 클래스
     */

    /* 12.3.1 시간대 이용하기
        - `ZoneRule` 클래스에서 시간대 규칙 집합을 정의한다. (약 40개)
        - `ZoneId`의 `getRule` 메서드를 해당 시간대의 규정을 획득할 수 있다.
        - 지역 ID는 `{지역}/{도시}` 형식이다.
        - 기존에 쓰이던 `TimeZone` 객체를 `ZoneId` 객체로 바꿀 수 있다.
        - `ZoneId`를 얻은 후에는 LocalDate, LocalDateTime, Instant 등을 이용해서 ZonedDateTime 인스턴스로 변환할 수 있다.
            - `ZonedDateTime`은 지정한 시간대에 상대적인 시점을 표현한다.
     */
    @Test
    void zoneIdExample() {
        ZoneId zoneId = ZoneId.of("Europe/Rome");

        LocalDate date = LocalDate.of(2014, Month.MARCH, 18);
        ZonedDateTime zdt1 = date.atStartOfDay(zoneId);
        LocalDateTime dateTime = LocalDateTime.of(2014, Month.MARCH, 18, 13, 45);
        ZonedDateTime zdt2 = dateTime.atZone(zoneId);
        Instant instant = Instant.now();
        ZonedDateTime zdt3 = instant.atZone(zoneId);

        LocalDateTime timeFromInstant = LocalDateTime.ofInstant(instant, zoneId);
    }

    /* 12.3.2 UTC/Greenwich 기준의 고정 오프셋
        - `ZoneId`의 서브클래스인 `ZoneOffset` 클래스로 런던의 그리니치 0도 자오선과 시간값의 차이를 표현할 수 있다.
     */
    @Test
    void zoneOffsetExample() {
        // `ZoneOffset`은 서머타임을 제대로 처리할 수 없어 권장하지 않음
        ZoneOffset newYorkOffset = ZoneOffset.of("-05:00");

        LocalDateTime dateTime = LocalDateTime.of(2014, Month.MARCH, 18, 13, 45);
        OffsetDateTime dateTimeInNewYork = OffsetDateTime.of(dateTime, newYorkOffset);
    }

    /* 12.3.3 대안 캘린더 시스템 사용하기
        - 추가로 4개의 캘린더 시스템을 제공
            - ThaiBuddhistDate, MinguoDate, JapaneseDate, HijrahDate 등
        - `ChronoLocalDate` 인터페이스를 구현
            - `LocalDate`도 이 인터페이스를 구현함
        - 그러나 날짜와 시간 API의 설계자는 `LocalDate`를 사용하라고 권장
     */

}
