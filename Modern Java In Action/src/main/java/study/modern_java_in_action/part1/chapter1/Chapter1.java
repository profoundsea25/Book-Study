package study.modern_java_in_action.part1.chapter1;

import org.junit.jupiter.api.Test;
import study.modern_java_in_action.model.Apple;
import study.modern_java_in_action.model.Color;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static study.modern_java_in_action.model.Color.*;

/**
 * Chapter 1. 자바 8, 9, 10, 11 : 무슨 일이 일어나고 있는가?
 */

public class Chapter1 {

    /**
     * 1.1 역사의 흐름은 무엇인가?
     */

    /*
        - 가장 큰 변화는 자바 8
        - 프로그램을 더 쉽게 구현할 수 있게 됨.
            - 자연어에 더 가깝게 간단한 방식으로 코드를 구현할 수 있다.
        - 멀티 코어 활용 : 병렬 실행 환경을 쉽게 관리하고 에러가 덜 발생하는 방향으로 진화
            - 자바 1.0 - 스레드와 락, 메모리 모델
            - 자바 5 - 스레드 풀, 병렬 실행 컬렉션
            - 자바 7 - 병렬 실행에 도움을 줄 수 있는 포크/조인 프레임워크
            - 자바 8 - 병렬 실행을 새롭고 단순한 방식으로 접근할 수 있는 방법 제공
            - 자바 9 - 리액티브 프로그래밍
        - 자바 8 : 간결한 코드, 멀티코어 프로세서의 쉬운 활용
            - 스트림 API
                - 고수준 언어로 원하는 동작을 표현하면, 구현에서 최적의 저수준 실행 방법을 선택하는 방식
                - 에러가 자주 일어나고 멀티코어 CPU 사용보다 비용이 훨씬 비싼 `synchronized`를 사용하지 않아도 됨.
            - 메서드에 코드를 전달하는 기법
                - 메서드 참조와 람다
                - 새롭고 간결한 방식으로 동작 파라미터화를 구현할 수 있다.
                - 함수형 프로그래밍에서 위력을 발휘
            - 인터페이스의 디폴트 메서드
     */


    /**
     * 1.2 왜 아직도 자바는 변화하는가?
     */

    /*
        - 진화하지 않는 기존 언어는 사장된다.
        - 특정 분야에서 장점을 가진 언어는 다른 경쟁 언어를 도태시킨다.
     */

    /* 1.2.1 프로그래밍 언어 생태계에서 자바의 위치
        - 출시이래 객체지향 언어로 성공적으로 자리 잡음.
        - 빅데이터라는 도전에 직면하면서 멀티코어 컴퓨터나 컴퓨틸 클러스터를 이용해서 빅데이터를 효과적으로 처리할 필요성이 커짐.
        - 자바 8은 더 다양한 프로그래밍 도구 그리고 다양한 프로그래밍 문제를 더 빠르고 정확하며 쉽게 유지보수할 수 있다는 장점 제공
            - 자바 8에 추가된 기능은 자바에 없던 완전히 새로운 개념이지만 현재 시장에서 요구하는 기능을 효과적으로 제공
        - 자바 8 설계의 밑바탕을 이루는 세 가지 프로그래밍 개념
            - 스트림 처리
            - 동작 파라미터화로 메서드에 코드 전달하기
            - 병렬성과 공유 가변 데이터
     */

    /* 1.2.2 스트림 처리 (stream processing)
        - 스트림이란 한 번에 한 개씩 만들어지는 연속적인 데이터 항목들의 모임이다.
            - 이론적으로 프로그램은 입력 스트림에서 데이터를 한 개씩 읽어 들이며 마찬가지로 출력 스트림으로 데이터를 한 개씩 기록한다.
            - 즉 어떤 프로그램의 출력 스트림은 다른 프로그램의 입력 스트림이 될 수 있다.
        - `java.util.stream` 패키지
            - `Stream<T>`는 T 형식으로 구성된 일련의 항목을 의미한다.
            - 쉽게 생각해서 조립 라인처럼 어떤 항목을 연속으로 제공하는 어떤 기능이라고 단순하게 생각하자.
            - 파이프라인을 만드는 데 필요한 많은 메서드를 제공한다.
        - 스트림 API의 핵심은 기존에는 한 번에 한 항목을 처리했지만
          이제 자바 8에서는 우리가 하려는 작업을 고수준으로 추상화해서 일련의 스트림으로 만들어 처리 할 수 있다는 것이다.
            - 스트림 파이프라인을 이용해서 입력 부분을 여러 CPU 코어에 쉽게 할당할 수 있다는 부가적인 이득도 얻을 수 있다.
            - 스레드라는 복잡한 작업을 사용하지 않으면서 공짜로 병렬성을 얻을 수 있다.
     */

    /* 1.2.3 동작 파라미터화(behavior parameterization)로 메서드에 코드 전달하기
        - 코드 일부를 API로 전달하는 기능
            - 우리 코드(ex 메서드)를 다른 메서드의 인수로 넘겨주는 기능
     */

    /* 1.2.4 병렬성과 공유 가변 데이터
        - 병렬성을 쉽게 얻을 수 있다.
            - 대신 스트림 메서드로 전달하는 코드의 동작 방식을 조금 바꿔야 한다.
        - 다른 코드와 동시에 실행하더라도 안전하게 실행할 수 있는 코드를 만들려면 공유된 가변 데이터에 접근하지 않아야 한다.
            - 이를 순수(pure) 함수, 부작용 없는(side-effect-free) 함수, 상태 없는(stateless) 함수라 한다.
        - 스트림을 이용하면 기존 자바 스레드 API보다 쉽게 병렬성을 활용할 수 있다.
            - 다중 프로세싱 코어에서 `synchronized`를 사용하면 생각보다 훨씬 더 비싼 대가를 치러야 할 수 있다.
     */

    /* 1.2.5 자바가 진화해야 하는 이유
        - 기존 값을 변화시키는 데 집중했던 고전적인 객체지향에서 벗어나 함수형 프로그래밍으로 다가섰다는 것이 자바 8의 가장 큰 변화다.
        - 언어는 하드웨어나 프로그래머의 기대의 변화에 부응하는 방향으로 변화해야 한다.
     */


    /**
     * 1.3 자바 함수
     */

    /*
        - 프로그래밍 언어에서 함수(function)이라는 용어는 메서드(method) 특히 정적 메서드와 같은 의미로 사용된다.
            - 자바의 함수는 이에 더해 '수학적인 함수'처럼 사용되며 부작용을 일으키지 않는 함수를 의미한다.
        - 자바 8에서는 함수를 새로운 값의 형식으로 추가했다.
            - 스트림과 연계될 수 있도록 함수를 만들었기 때문이다.
        - 함수를 값처럼 취급할 때 장점
            - 프로그래밍 언어의 핵심은 값을 바꾸는 것이다. 이를 일급(first-class)값이라고 한다.
            - 자바 프로그래밍 언어의 다양한 구조체(메서드, 클래스 같은)가 값의 구조를 표현하는 데 도움이 될 수 있지만,
              프로그램을 실행하는 동안 이러한 모든 구조체를 자유롭게 전달할 수는 없다. (= 이급 시민)
            - 예를 들어 런타임에 메서드를 전달할 수 있다면, 즉 메서드를 일급 시민으로 만들면 프로그래밍에 유용하게 활용할 수 있다.
     */

    /* 1.3.1 메서드와 람다를 일급 시민으로
        - 메서드를 값으로 취급할 수 있도록 설계
        - 메서드 참조(method reference)
            - 코드에서 `::`로 표현 (이 메서드를 값으로 사용하라 라는 의미)
        - 람다 : 익명 함수
            - 직접 메서드를 정의할 수도 있지만,
              이용할 수 있는 편리한 클래스나 메서드가 없을 때 새로운 람다 문법을 이용하면 더 간결하게 코드 구현이 가능하다.
     */
    @Test
    void methodReferenceExample() {
        // 익명 클래스 사용
        File[] byAnonymousClass = new File(".").listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isHidden();
            }
        });

        // 메서드 참조 사용
        File[] byMethodReference = new File(".").listFiles(File::isHidden);
    }

    /* 1.3.2 코드 넘겨주기 : 예제

     */

    // 자바 8 이전
    public static List<Apple> filterGreenApplesBeforeJava8(List<Apple> inventory) {
        List<Apple> result = new ArrayList<>();

        for (Apple apple : inventory) {
            if (GREEN.equals(apple.getColor())) {
                result.add(apple);
            }
        }
        return result;
    }

    public static List<Apple> filterHeavyApplesBeforeJava8(List<Apple> inventory) {
        List<Apple> result = new ArrayList<>();

        for (Apple apple : inventory) {
            if (apple.getWeight() > 150) {
                result.add(apple);
            }
        }
        return result;
    }

    // 자바 8 이후
    public static boolean isGreenAppleAfterJava8(Apple apple) {
        return GREEN.equals(apple.getColor());
    }

    public static boolean isHeavyAppleAfterJava8(Apple apple) {
        return apple.getWeight() > 150;
    }

    public static List<Apple> filterApplesAfterJava8(List<Apple> inventory, Predicate<Apple> p) {
        List<Apple> result = new ArrayList<>();

        for (Apple apple : inventory) {
            if (p.test(apple)) {
                result.add(apple);
            }
        }
        return result;
    }

    @Test
    void callMethodAfterJava8Example() {
        List<Apple> inventory = Apple.exampleAppleList();
        filterApplesAfterJava8(inventory, Chapter1::isGreenAppleAfterJava8);
        filterApplesAfterJava8(inventory, Chapter1::isHeavyAppleAfterJava8);
    }

    /* 1.3.3 메서드 전달에서 람다로
        - 한두 번만 사용할 메서드를 매번 정의하는 것은 귀찮은 일이다.
            - 이를 해결하기 위해 익명 함수 또는 람다라는 새로운 개념을 이용해서 코드를 구현할 수 있다.
            - 한 번만 사용할 메서드는 따로 정의를 구현할 필요가 없다.
            - 하지만 람다가 몇 줄 이상으로 길어진다면(즉, 조금 복잡한 동작을 수행하는 상황)
              익명 람다보다는 코드가 수행하는 일을 잘 설명하는 이름을 가진 메서드를 정의하고 메서드 참조를 활용하는 것이 좋다.
                - 코드의 명확성이 우선시되어야 한다.
     */

    @Test
    void useLambdaExample() {
        List<Apple> inventory = Apple.exampleAppleList();
        filterApplesAfterJava8(inventory, (Apple a) -> GREEN.equals(a.getColor()));
        filterApplesAfterJava8(inventory, (Apple a) -> a.getWeight() > 150);
        filterApplesAfterJava8(inventory, (Apple a) -> a.getWeight() < 80 || Color.RED.equals(a.getColor()));
    }

    /**
     * 1.4 스트림
     */

    /*
        - 거의 모든 자바 애플리케이션은 컬렉션을 만들고 활용한다.
            - 하지만 컬렉션으로 모든 문제가 해결되는 것은 아니다.
        - 스트림 API를 이용하면 컬렉션 API와는 상당히 다른 방식으로 데이터를 처리할 수 있다.
            - 컬렉션에서는 반복 과정을 직접 처리해야 했다.
                - 즉, `for-each` 루프를 이용해서 각 요소를 반복하면서 작업을 수행했다.
                - 이런 방식의 반복을 외부 반복(external iteration)이라 한다.
            - 반면 스트림 API를 이용하면 루프를 신경 쓸 필요가 없다.
                - 스트림 API에서는 라이브러리 내부에서 모든 데이터가 처리된다.
                - 이와 같은 반복을 내부 반복이라 한다.
     */

    /* 1.4.1 멀티스레딩은 어렵다
        - 멀티스레딩 환경에서 각각의 스레드는 동시에 공유된 데이터에 접근하고, 데이터를 갱신할 수 있다.
            - 스레드를 잘 제어하지 못하면 원치 않는 방식으로 데이터가 바뀔 수 있다.
            - 보통 멀티스레딩 모델은 순차적인 모델보다 다루기 어렵다.
        - 자바 8 스트림 API로 컬렉션을 처리하면서 발생하는 모호함과 반복적인 코드 문제, 그리고 멀티코어 활용 어려움이라는 두 가지 문제를 모두 해결했다.
        - 스트림은 스트림 내의 요소를 쉽게 병렬로 처리할 수 있는 환경을 제공한다.
            - 컬렉션을 필터리할 수 있는 가장 빠른 방법은 컬렉션을 스트림으로 바꾸고, 병렬로 처리한 다음에, 리스트로 다시 복원하는 것이다.
    */

    /**
     * 1.5 디폴트 메서드와 자바 모듈
     */

    /*
        - 자바 9의 모듈 시스템
            - 모듈을 정의하는 문법을 제공해 패키지 모음을 포함하는 모듈을 정의할 수 있다.
        - 자바 8의 디폴트 메서드
            - 인터페이스를 쉽게 바꿀 수 있도록 디폴트 메서드를 지원한다.
            - 인터페이스에 새로운 메서드를 추가한다면 인터페이스를 구현하는 모든 클래스는 새로 추가된 메서드를 구현해야 하는 어려움을 해소
                - 기존의 구현을 고치지 않고도 이미 공개된 인터페이스를 변경한다.
            - 구현하지 않아도 되는 메서드를 인터페이스에 추가할 수 있는 기능
                - 메서드 본문은 클래스 구현이 아니라 인터페이스의 일부로 포함된다.
            - 여러 인터페이스를 구현하는 클래스에서 다중 디폴트 메서드가 존재할 수 있다.
                - 다만 이것이 다중 상속은 아니다.
     */

    /**
     * 1.6 함수형 프로그래밍에서 가져온 다른 유용한 아이디어
     */

    /* `Optional<T>`
        - NullPointer 예외를 피할 수 있도록 도와주는 클래스
        - `Optional<T>`는 값을 갖거나 갖지 않을 수 있는 컨테이너 객체다.
        - 값이 없는 상황을 어떻게 처리할지 명시적으로 구현하는 메서드를 포함하고 있다.
     */

}
