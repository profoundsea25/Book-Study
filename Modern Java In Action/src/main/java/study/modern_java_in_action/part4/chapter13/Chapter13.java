package study.modern_java_in_action.part4.chapter13;

/**
 * Chapter 13. 디폴트 메서드
 */

/* 기존 인터페이스의 문제점
    - 인터페이스에 새로운 메서드를 추가하는 등 인터페이스를 바꾸고 싶을 때 문제가 발생
        - 이전에 해당 인터페이스를 구현했던 모든 클래스의 구현도 고쳐야 하기 때문
    - 이 문제를 해결하는 새로운 기능
        - 정적 메서드 (static method)
        - 디폴트 메서드 (default method)
    - 즉, 자바 8에서는 메서드 구현을 포함하는 인터페이스를 정의할 수 있다.
        - 결과적으로 기존 인터페이스를 구현하는 클래스는 자동으로 인터페이스에 추가된 새로운 메서드의 디폴트 메서드를 상속받게 된다.
        - 기존의 코드 구현을 바꾸도록 강요하지 않으면서 인터페이스 변경 가능
    - `default` 키워드
    - 디폴트 메서드는 주로 라이브러리 설계자들이 사용한다.
        - 자바 API의 호환성을 유지하면서 라이브러리를 바꿀 수 있다.
    - 디폴트 메서드를 통해 다중 상속을 달성할 수도 있다.
    - 정적 메서드
        - 인터페이스에 직접 정적 메서드를 선언할 수 있으므로 유틸리티 클래스를 없앨 수 있다.
 */

public class Chapter13 {

    /**
     * 13.1 변화하는 API
     */

    /* 13.1.1 API 버전 1

     */

    /* 13.1.1 API 버전 2
        - 인터페이스를 고치면 발생하는 문제점
            - 인터페이스에 메서드를 추가하면 해당 인터페이스를 구현하는 모든 클래스는 추가된 메서드를 구현해야 한다.
                - 인터페이스에 새로운 메서드를 추가하면 바이너리 호환성은 유지된다.
                    - 바이너리 호환성이란 새로 추가된 메서드를 호출하지 않으면 새로운 메서드 구현이 없이도
                      기존 클래스 파일 구현이 잘 작동한다는 의미다.
            - 인터페이스를 고치면 전체 애플리케이션을 재빌드할 때 컴파일 에러가 발생한다.
                - 공개된 API를 고치면 기존 버전과의 호환성 문제가 발생
        - 이 모든 문제를 디폴트 메서드로 해결할 수 있다.
            - 새롭게 바뀐 인터페이스에서 자동으로 기본 구현을 제공하므로 기존 코드를 고치지 않아도 된다.
     */

    /* 호환성
        - 바이너리 호환성
            - 뭔가를 바꾼 이후에도 에러 없이 기존 바이너리가 실행될 수 있는 상황
        - 소스 호환성
            - 코드를 고쳐도 기존 프로그램을 성공적으로 재컴파일할 수 있음을 의미
        - 동작 호환성
            - 코드를 바꾼 다음에도 같은 입력값이 주어지면 프로그램이 같은 동작을 실행한다는 의미
     */


    /**
     * 13.2 디폴트 메서드란 무엇인가?
     */

    /* 디폴트 메서드
        - 호환성을 유지하면서 API를 변경할 수 있다.
        - 인터페이스를 자신을 구현하는 클래스에서 메서드를 구현하지 않을 수 있는 새로운 메서드 시그지처를 제공
        - 인터페이스 자체에서 기본으로 구현을 제공
        - `default` 키워드를 포함하며, 메서드 바디를 포함한다.
     */

    /* 추상 클래스와 인터페이스
        - 공통점 : 추상 메서드와 바디를 포함하는 메서드를 정의할 수 있다.
        - 차이점
            - 클래스는 하나의 추상 클래스만 상속받을 수 있지만 인터페이스는 여러 개 구현할 수 있다.
            - 추상 클래스는 인스턴스 변수(필드)로 공통 상태를 가질 수 있다. 인터페이스는 인스턴스 변수를 가질 수 없다.
     */


    /**
     * 13.3 디폴트 메서드 활용 패턴
     */

    /* 13.3.1 선택형 메서드
        - 구현 클래스에서 필요없는 메서드의 빈 구현을 할 필요가 없다. 불필요한 코드가 줄어든다.
     */

    /* 13.3.2 동작 다중 상속
        - 자바는 인터페이스를 여러 개 상속 받을 수 있다.
        - 따라서 디폴트 메서드를 통해 여러 인터페이스에서 동작(구현 코드)을 상속받을 수 있다.
            - 중복되지 않는 초소한의 인터페이스를 유지한다면 동작을 쉽게 재사용하고 조합할 수 있다.
        - 디폴트 메서드를 수정하면 상속받는 모든 클래스를 한번에 고칠 수 있다.
     */

    /* 옳지 못한 상속
        - 다른 클래스/인터페이스의 메서드 일부가 필요하다고 해서 상속 받을 필요는 없다.
            - 이럴 때는 델리게이션(delegation)을 활용할 수 있다.
            - 해당 클래스를 멤버 변수로 받아 필요한 메서드를 직접 호출하는 메서드를 작성하는 것이 좋다.
        - 클래스에 final 이 붙은 경우는, 상속을 통해 재정의를 막기 위함이다.
     */


    /**
     * 13.4 해석 규칙
     */

    /* 다중 상속 문제
        - 디폴트 메서드가 추가되어 같은 시그니철르 갖는 디폴트 메서드를 상속받는 상황이 생길 수 있다.
     */

    /* 13.4.1 알아야 할 세 가지 해결 규칙
        - 다른 클래스나 인터페이스로부터 같은 시그니처를 갖는 메서드를 상속받을 때는 세 가지 규칙을 따라야 한다.
        - 1. 클래스가 항상 이긴다. 클래스나 슈퍼클래스에서 정의한 메서드가 디폴트 메서드보다 우선권을 갖는다.
        - 2. 1번 규칙 이외의 상황에서는 서브 인터페이스가 이긴다.
            - 상속관계를 갖는 인터페이스에서 같은 시그니처를 갖는 메서드를 정의할 때는 서브인터페이스가 이긴다.
            - 즉, B가 A를 상속받는다면 B가 A를 이긴다.
        - 3. 여전히 디폴트 메서드의 우선순위가 결정되지 않았다면 여러 인터페이스를 상속받는 클래스가 명시적으로
              디폴트 메서드를 오버라이드하고 호출해야 한다.
     */

    /* 13.4.3 충돌 그리고 명시적인 문제 해결
        - 클래스와 메서드 관계로 디폴트 메서드를 선택할 수 없는 상황에서는 선택할 수 있는 방법이 없다.
            - 개발자가 직접 메서드를 명시적으로 선택해야 한다.
     */

    interface A {
        default void hello() {
            System.out.println("Hello from A");
        }
    }

    interface B {
        default void hello() {
            System.out.println("Hello from B");
        }
    }

    public class C implements B, A {
        public void call() {
            new C().hello();
        }
        @Override
        public void hello() {
            // 둘 중 하나를 선택한다.
            A.super.hello();
//            B.super.hello();
        }
    }

}
