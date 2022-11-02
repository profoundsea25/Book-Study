package study.modern_java_in_action.part3.chapter9;

import org.junit.jupiter.api.Test;

/**
 * Chapter 9. 리팩터링, 테스팅, 디버깅
 */

public class Chapter9 {

    /**
     * 9.1 가독성과 유연성을 개선하는 리팩터링
     */

    /* 복습
        - 람다 : 익명 클래스보다 코드를 좀 더 간결하게 만든다.
        - 메서드 참조 : 인수로 전달하려는 메서드가 이미 있을 때는 람다보다 더 간결한 코드를 구현할 수 있다.
     */

    /* 9.1.1 코드 가독성 개선
        - 코드 가독성이란?
            - "어떤 코드를 다른 사람도 쉽게 이해할 수 있음"
            - 코드 가독성을 개선한다는 것은 우리가 구현한 코드를 다른 사람이 쉽게 이해하고 유지보수할 수 있게 만드는 것을 의미
            - 코드의 문서화를 잘하고, 표준 코딩 규칙을 준수하는 등의 노력이 필요하다.
        - 코드 가독성을 개선하는 간단한 3가지 예제
            - 익명 클래스를 람다 표현식으로 리팩터링하기
            - 람다 표현식을 메서드 참조로 리팩터링하기
            - 명령형 데이터 처리를 스트림으로 리팩터링하기
     */

    /* 9.1.2 익명 클래스를 람다 표현식으로 리팩터링하기
        - 익명 클래스를 람다로 바꿀 때 주의할 점
            - 익명 클래스에서 사용한 `this`와 `super`는 람다 표현식에서 다른 의미를 갖는다.
                - 익명 클래스 `this` = 익명 클래스 자기 자신
                - 람다 `this` = 람다를 감싸는 클래스
            - 익명 클래스는 감싸고 있는 클래스의 변수를 가릴 수 있다. (섀도우 변수, shadow variable)
                - 람다 표현식으로는 변수를 가릴 수 없다.
            - 익명 클래스를 람다 표현식으로 바꾸면 콘텍스트 오버로딩에 따른 모호함이 초래될 수 있다.
                - 익명 클래스는 인스턴스화할 때 명시적으로 형식이 정해진다.
                - 람다 형식은 콘텍스트에 따라 달라진다.
     */
    @Test
    void anonymousClassToLambdaExample() {
        // 익명 함수
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello");
            }
        };

        // 람다
        Runnable r2 = () -> System.out.println("Hello");
    }

    @Test
    void shadowVariableExample() {
        int a = 10;
        Runnable r1 = () -> {
//            int a = 2; // 컴파일 할 수 없다.
            System.out.println(a);
        };

        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                int a = 2;
                System.out.println(a);
            }
        };
    }

    interface Task {
        public void execute();
    }

    public static void doSomething(Runnable r) {
        r.run();
    }

    public static void doSomething(Task a) {
        a.execute();
    }

    @Test
    void lambdaContextExample() {
        // 익명 클래스는 타입을 명시하여 모호함이 없다.
        doSomething(new Task() {
            @Override
            public void execute() {
                System.out.println("Danger danger!!");
            }
        });

        // 람다는 `Task`를 호출할지 `Runnable`을 호출할지 모호하다.
//        doSomething(() -> System.out.println("Danger danger!!"));
        // 따라서 명시적으로 형변환을 작성해 모호함을 제거할 수 있다.
        doSomething((Task) () -> System.out.println("Danger danger!!"));
    }

    /* 9.1.3 람다 표현식을 메서드 참조로 리팩터링하기
        - 람다 표현식 대신 메서드 참조를 이용하면 가독성을 높일 수 있다.
     */


}
