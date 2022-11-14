package study.modern_java_in_action.part4.chapter11;

import org.junit.jupiter.api.Test;
import study.modern_java_in_action.part4.chapter11.ex_11_1.Car1;
import study.modern_java_in_action.part4.chapter11.ex_11_1.Insurance1;
import study.modern_java_in_action.part4.chapter11.ex_11_1.Person1;
import study.modern_java_in_action.part4.chapter11.ex_11_2.Car2;
import study.modern_java_in_action.part4.chapter11.ex_11_2.Insurance2;
import study.modern_java_in_action.part4.chapter11.ex_11_2.Person2;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Chapter 11. null 대신 Optional 클래스
 */

public class Chapter11 {

    /**
     * 11.1 값이 없는 상황을 어떻게 처리할까?
     */

    // NPE 의 향연. person, getCar, getInsurance, getName 에서 NPE 발생 가능성
    public String getCarInsuranceName1(Person1 person1) {
        return person1.getCar().getInsurance().getName();
    }

    /* 11.1.1 보수적인 자세로 NullPointerException 줄이기
        - 필요한 곳에 다양한 null 확인 코드를 추가
        - 방법 1. 깊은 의심
            - 변수를 접근할 때마다 중첩된 if 가 추가되며 코드 들여쓰기 수준이 증가한다.
            - 이와 같은 반복 패턴 코드를 `깊은 의심(deep doubt)`라고 한다.
            - 들여쓰기가 많아질수록 코드 구조가 엉망이 되고 가독성도 떨어진다.
        - 방법 2. 너무 많은 출구
            - if 문에 return 사용
            - 출구가 너무 많아져 유지보수가 어려워진다.
     */
    // 깊은 의심
    public String getCarInsuranceName2(Person1 person1) {
        if (person1 != null) {
            Car1 car1 = person1.getCar();
            if (car1 != null) {
                Insurance1 insurance1 = car1.getInsurance();
                if (insurance1 != null) {
                    return insurance1.getName();
                }
            }
        }
        return "Unknown";
    }

    // 너무 많은 출구
    public String getCarInsuranceName3(Person1 person1) {
        if (person1 == null) {
            return "Unknown";
        }
        Car1 car1 = person1.getCar();
        if (car1 == null) {
            return "Unknown";
        }
        Insurance1 insurance1 = car1.getInsurance();
        if (insurance1 == null) {
            return "Unknown";
        }
        return insurance1.getName();
    }

    /* 11.1.2 null 때문에 발생하는 문제
        - 에러의 근원
        - 코드를 어지럽힌다.
        - 아무 의미를 표현하지 않는다.
        - 자바 철학에 위배된다. (포인터를 숨겼는데, 예외가 바로 Null Pointer)
        - 형식 시스템에 구멍을 만든다. (모든 참조 형식에 null을 할당할 수 있다. 따라서 어떤 의미로 사용되는지 정확히 알 수 없다.)
     */

    /* 11.1.3 다른 언어는 null 대신 무얼 사용하나?
        - 그루비의 안전 연산자 (`?.`), 하스켈의 `MayBe`, 스칼라의 `Option` 등
     */


    /**
     * 11.2 Optional 클래스 소개
     */

    /* `Optional<T>`
        - `Optional`은 선택형값을 캡슐화하는 클래스다.
            - 값이 있으면 `Optional` 클래스는 값을 감싼다.
            - 값이 없으면 `Optional.empty` 메서드로 `Optional`을 반환한다.
        - 값이 없을 수 있음을 명시적으로 보여준다.
            - null은 올바른 값인지 아니면 잘못된 값인지 판단할 아무 정보도 없다.
        - `Optional`을 이용하면 값이 없는 상황이 우리 데이터에 문제가 있는 것인지 아니면
          알고리즘의 버그인지 명확하게 구분할 수 있다.
        - 모든 null 참조를 `Optional`로 대치하는 것은 바람직하지 않다.
        - `Optional`의 역할은 더 이해하기 쉬운 API를 설계하도록 돕는 것이다.
            - 즉, 메서드의 시그니처만 보고도 선택형 값인지 여부를 구별할 수 있다.
            - `Optional`이 등장하면 이를 언랩해서 값이 없을 수 있는 상황에 적절하게 대응하도록 강제하는 효과가 있다.
     */


    /**
     * 11.3 Optional 적용 패턴
     */

    /* 11.3.1 Optional 객체 만들기
        - 빈 Optional
            - `Optional.empty()`
        - null이 아닌 값으로 Optional 만들기
            - `Optional.of()`
            - of에 null이 들어가면 NPE 발생
        - null값으로 Optional 만들기
            - `Optional.ofNullable()`
            - ofNullable에 null이 들어가면 빈 Optional 객체가 반환된다.
     */
    @Test
    void createOptionalObjectExample() {
        Car2 car = new Car2();

        // 빈 Optional
        Optional<Car2> optCar1 = Optional.empty();

        // null이 아닌 값으로 Optional 만들기
        Optional<Car2> optCar2 = Optional.of(car);

        // null값으로 Optional 만들기
        Optional<Car2> optCar3 = Optional.ofNullable(car);
    }

    /* 11.3.2 맵으로 Optional의 값을 추출하고 변환하기
        - `Optional.map()`
            - `Optional`의 정보를 추출할 때 사용
            - `Optional`이 값을 포함하면 map의 인수로 제공된 함수가 값을 바꾼다.
            - `Optional`이 비어있으면 아무 일도 일어나지 않는다.
     */
    @Test
    void optionalMapExample() {
        // optional 이 없으면
        String name = null;
        Insurance1 insurance1 = new Insurance1();
        if (insurance1 != null) {
            name = insurance1.getName();
        }

        // optional 이 있으면
        Insurance2 insurance2 = new Insurance2();
        Optional<Insurance2> optInsurance = Optional.ofNullable(insurance2);
        Optional<String> optName = optInsurance.map(Insurance2::getName);
    }

    /* 11.3.3 `flatMap`으로 `Optional` 객체 연결
        - `flatMap`은 인수로 받은 `Optional`의 콘텐츠만 남겨 하나의 `Optional`만 남긴다.
            - null을 확인하느라 조건 분기문을 추가해서 코드를 복잡하게 만들지 않으면서도 쉽게 이해하는 코드를 완성시킬 수 있다.
        - `Optional`인수로 받거나 `Optional`을 반환하는 메서드를 정의한다면 결과적으로 이 메서드를 사용하는 모든 사람에게
          이 메서드가 빈 값을 받거나 빈 결과를 반환할 수 있음을 잘 문서화해서 제공하는 것과 같다.
        - `flatMap`을 빈 `Optional`에 호출하면 아무 일도 일어나지 않고 그대로 반환된다.
            - 호출 체인 중 어떤 메서드가 빈 `Optional`을 반환하다면 전체 결과로 빈 `Optional`을 반환하고
              아니면 관련 보험회사의 이름을 포함하는 `Optional`를 반환한다.
        - `orElse`는 `Optional`이 비어있을 때 기본값을 제공한다.
            - `Optional`에 값이 있다면 그 값을 반환한다.
     */
    public String getCarInsuanceName(Optional<Person2> person) {
        return person
                .flatMap(Person2::getCar)
                .flatMap(Car2::getInsurance)
                .map(Insurance2::getName)
                .orElse("Unknown");
    }

    /* 도메인 모델에 `Optional`을 사용했을 때 데이터를 직렬화할 수 없는 이유
        - `Optional`은 선택형 반환값을 지원하는 것이다.
        - `Optional` 클래스는 필드 형식으로 사용할 것을 가정하지 않았으므로 `Serializable` 인터페이스를 구현하지 않는다.
            - 따라서 직렬화를 사용한다면 문제가 생길 수 있다.
        - 그럼에도 불구하고 여전히 `Optional`를 사용하는 것이 바람직하다.
            - 특히 객체 그래프에서 일부 또는 전체 객체가 null일 수 있는 상황이라면 더욱 그렇다.
     */

    /* 11.3.4 Optional 스트림 조작
        - `Optional`을 포함하는 스트림을 쉽게 처리할 수 있도록 `Optional`에 `stream()` 메서드를 추가했다.
            - `Optional` 스트림을 값을 가진 스트림으로 변환할 때 이 기능을 유용하게 활용할 수 있다.
        - `Optional`이 비어있는지 아닌지에 따라 `Optional`을 0개 이상의 항목을 포함하는 스트림으로 변환한다.
            - 한 단계의 연산으로 값을 포함하는 `Optional`을 언랩하고 비어있는 `Optional`은 건너뛸 수 있다.
     */
    public Set<String> getCarInsuranceNames(List<Person2> persons) {
        return persons.stream()
                .map(Person2::getCar)
                .map(optCar -> optCar.flatMap(Car2::getInsurance))
                .map(optIns -> optIns.map(Insurance2::getName))

//                // Stream에 결과가 비어있을수도 있다.
//                .filter(Optional::isPresent)
//                .map(Optional::get)
//                .collect(Collectors.toSet());

                // 위의 주석된 코드를 아래와 같이 줄일 수 있다.
                .flatMap(Optional::stream)
                .collect(Collectors.toSet());
    }

    /* 디폴트 액션과 `Optional` 언랩
        - `orElse()`
            - 빈 `Optional`인 상황에서 기본값을 반환
        - `get()`
            - 값을 읽는 가장 간단한 메서드면서 동시에 가장 안전하지 않은 메서드
            - 래핑된 값이 있으면 해당 값을 반환하고 값이 없으면 `NoSuchElementException` 발생
            - 따라서 `Optional`에 값이 반드시 있다고 가정할 수 있는 상황이 아니면 get 메서드를 사용하지 않는 것이 바람직
        - `orElseGet(Supplier<? extends T> other)`
            - `orElse`에 대응하는 게으른 버전
            - `Optional`이 값이 없을 때만 `Supplier` 실행
        - `orElseThrow(Supplier<? extends X> ExceptionSupplier`
            - `Optional`이 비어있을 때 예외를 발생시킴. 발생시킬 예외의 종류 선택 가능
        - `ifPresent(Consumer<? super T> consumer)`
            - 값이 존재할 때 인수로 넘겨준 동작을 실행할 수 있음. 값이 없으면 아무 일도 일어나지 않음.
        - `ifPresentOrElse(Consumer<? super T> action, Runnable emptyAction`
            - `Optional`이 비었을 때 실행할 수 있는 `Runnable`을 인수로 받는다는 점만 `ifPresent`와 다름
     */

    /* 11.3.6 두 Optional 합치기
        - `Optional` 메서드를 이용해 중첩하여 한 줄로 만들기
     */
    public Insurance2 findCheapestInsurance(Person2 person, Car2 car) {
        // 다양한 보험회사가 제공하는 서비스 조회
        // 모든 결과 데이터 비교
        Insurance2 cheaptestCompany = new Insurance2();
        return cheaptestCompany;
    }

    public Optional<Insurance2> nullSafeFindCheapestInsurace(Optional<Person2> person, Optional<Car2> car) {
        if (person.isPresent() && car.isPresent()) { // null 체크 코드와 다를 바 없다.
            return Optional.of(findCheapestInsurance(person.get(), car.get()));
        } else {
            return Optional.empty();
        }
    }

    public Optional<Insurance2> nullSafeFindCheapestInsuraceRefactoring(Optional<Person2> person, Optional<Car2> car) {
        return person.flatMap(p -> car.map(c -> findCheapestInsurance(p, c)));
    }

    /* 11.3.7 필터로 특정값 거르기
        - `filter`
            - 프레디케이드를 인수로 받는다.
            - `Optional` 객체가 값을 가지며 프레디케이트와 일치하면 `filter` 메서드는 그 값을 반환하고,
              그렇지 않으면 빈 `Optional` 객체를 반환한다.
            - `Optional`이 비어있다면 아무런 연산을 하지 않는다.
            - 프레디케이트 적용 결과가 true면 `Optional`에 아무 변화가 없으며,
              결과가 false면 값은 사라져버리고 `Optional`은 빈 상태가 된다.
     */
    @Test
    void optionalFilterExample() {
        // 보험회사 이름이 CambridgeInsurance 인지 확인하는 코드
        Insurance2 insurance = new Insurance2();
        if (insurance != null && "CambridgeInsurance".equals(insurance.getName())) {
            System.out.println("ok");
        }

        // Optional.filter 활용
        Optional<Insurance2> optInsurance = Optional.of(insurance);
        optInsurance.filter(ins ->
                "CambridgeInsurance".equals(ins.getName()))
                .ifPresent(x -> System.out.println("ok"));
    }

    public String getCarInsuranceName(Optional<Person2> person, int minAge) {
        return person
                .filter(p -> p.getAge() >= minAge)
                .flatMap(Person2::getCar)
                .flatMap(Car2::getInsurance)
                .map(Insurance2::getName)
                .orElse("Unknown");
    }


    /**
     * 11.4 `Optional`을 사용한 실용 예제
     */

    /* 11.4.1 잠재적으로 null이 될 수 있는 대상을 Optional로 감싸기

     */
    @Test
    void optionalExample_11_4_1() {
        Map<String, Object> map = new HashMap<>();

        Object value = map.get("key");
        // "key" 가 없으면 null 반환

        // 아래와 같이 바꿔 쓸 수 있다.
        Optional<Object> optValue = Optional.ofNullable(map.get("key"));
    }

    /* 11.4.2 예외와 Optional 클래스
        - ex. `Integer.parseInt()`를 Optional을 반환하도록 만들기
            - try/catch 블록을 없애고 사용할 수 있다.
     */
    public static Optional<Integer> stringToInt(String s) {
        try {
            return Optional.of(Integer.parseInt(s));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /* 11.4.3 기본형 Optional을 사용하지 말아야 하는 이유
        - `OptionalInt`, `OptionalLog`, `OptionalDouble` 등이 있다.
            - 스트림과 다르게 `Optional`의 최대 요소는 하나이므로 기본형 특화 클래스로 성능을 개선할 수 없다.
        - 기본형 특화 `Optional`은 map, flatMap, filter 등을 지원하지 않는다.
        - 스트림과 마찬가지로 기본형 특화 Optional로 생성한 결과는 다른 일반 Optional과 혼용할 수 없다.
     */

    /* 11.4.4 응용

     */
    @Test
    void optionalExample_11_4_4() {
        Properties props = new Properties();
        props.setProperty("a", "5");
        props.setProperty("b", "true");
        props.setProperty("c", "-3");

        assertEquals(5, readDuration(props, "a"));
        assertEquals(0, readDuration(props, "b"));
        assertEquals(0, readDuration(props, "c"));
        assertEquals(0, readDuration(props, "d"));
    }

    public int readDuration(Properties props, String name) {
        // 기본 구현
//        String value = props.getProperty(name);
//        if (value != null) {
//            try {
//                int i = Integer.parseInt(value);
//                if (i > 0) {
//                    return i;
//                }
//            } catch (NumberFormatException numberFormatException) {
//
//            }
//        }
//        return 0;

        // Optional 활용
        return Optional.ofNullable(props.getProperty(name))
                .flatMap(Chapter11::stringToInt)
                .filter(i -> i > 0)
                .orElse(0);
    }

}
