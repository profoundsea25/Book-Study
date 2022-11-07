package study.modern_java_in_action.part4.chapter11;

import org.junit.jupiter.api.Test;
import study.modern_java_in_action.part4.chapter11.ex_11_1.Car1;
import study.modern_java_in_action.part4.chapter11.ex_11_1.Insurance1;
import study.modern_java_in_action.part4.chapter11.ex_11_1.Person1;
import study.modern_java_in_action.part4.chapter11.ex_11_2.Car2;

import java.util.Optional;

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
        - 보통 객체의 정보를 추출할 때는 `Optional`을 사용할 때가 많다.
     */

}
