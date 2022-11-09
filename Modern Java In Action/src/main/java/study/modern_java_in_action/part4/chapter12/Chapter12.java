package study.modern_java_in_action.part4.chapter12;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoField;

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
     */
}
