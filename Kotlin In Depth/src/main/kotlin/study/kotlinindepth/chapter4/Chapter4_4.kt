package study.kotlinindepth.chapter4

import org.junit.jupiter.api.Test
import study.kotlinindepth.chapter4.Chapter4_4.Application.exit

/**
 * 4.4 객체
 */

class Chapter4_4 {
    /**
     * 4.4.1 객체 선언
     */

    /* object 키워드
        - 어떤 클래스에 인스턴스가 오직 하나만 존재하게 보장하게 함
        - 코틀린에서 내장하는 싱글턴 패턴
        - object를 선언하는 것은 클래스를 정의함과 동시에 클래스의 인스턴스를 정의하는 것이기도 함.
        - 일반적으로 object의 인스턴스는 단 하나뿐이므로 인스턴스만 가리켜도 어떤 타입을 쓰는지 충분히 알 수 있다.
            - 따라서 object를 타입으로 사용해도 무방
        - object 정의는 스레드 안전하다.
            - 컴파일러는 실행되는 여러 스레드에서
                - 싱글턴에 접근하더라도 오직 한 인스턴스만 공유되고,
                - 초기화 코드도 단 한 번만 실행되도록 보장한다.
        - 초기화는 싱글턴 클래스가 실제 로딩되는 시점까지 지연된다.
            - 보통은 프로그램이 object 인스턴스에 처음 접근할 때 초기화가 이뤄진다.
        - 클래스와 마찬가지로 object도 멤버 함수와 프로퍼티, 초기화 블록도 포함할 수 있다.
            - 하지만 주생성자나 부생성자는 없다.
            - object의 인스턴스는 항상 암시적으로 만들어지기 때문에 객체의 경우 생성자 호출이 아무런 의미가 없다.
        - object 본문에 들어있는 클래스에는 `inner`가 붙을 수 없다.
            - 내부 클래스의 인스턴스는 항상 바깥쪽 클래스의 인스턴스와 연관됨.
            - object 선언은 항상 인스턴스가 하나뿐이므로 inner 변경자가 불필요함.
        - object의 멤버를 임포트해서 간단한 이름만 사용해 참조할 수 있다.
            - 다만, `import Application.*` 와 같이 모든 멤버를 임포트 문으로 임포트 불가능
                - object 정의 안에는 toString()이나 equals()와 같은 공통 메서드 정의도 포함되기 때문이다.
        - object도 클래스와 마찬가지로 다른 클래스 안에 내포될 수 있다.
            - 다른 object에 내포될 수도 있다.
            - 내포된 object 선언도 싱글턴이며 전체 애플리케이션에서 인스턴스가 단 하나만 생긴다.
                - 객체를 둘러싸는 클래스마다 인스턴스를 별도로 만들어야 한다면 object가 아닌 내부 클래스를 사용해야 한다.
        - object를 함수 내부에 넣거나 지역 클래스 또는 내부 클래스 안에 넣을 수 없다.
            - 이런 정의들은 어떤 외부 문맥에 의존하므로 싱글턴이 될 수 있기 때문이다.
            - 객체 식(object expression)을 사용하면 지역 영역의 객체를 만들 수 있다.
        - 자바의 Util Class(인스턴스 없이 정적 메소드만 모아 놓은 클래스)는 코틀린에서 권장되지 않는다.
            - 코틀린 클래스는 정적 메서드를 정의할 수 없다.
            - 다만 코틀린은 최상위 선언을 패키지 안에 함께 모아둘 수 있으므로 Util Class를 선언할 필요가 없다.
     */
    object Application {
        val name = "My Application"

        override fun toString() = name

        fun exit() {}
    }

    fun describe(app: Application) = app.name

    @Test
    fun objectTest() {
        println(Application)
    }

    @Test
    fun importTest() {
        println(Application.name)
        exit() // 상단 import문 확인
    }


    /**
     * 4.4.2 동반 객체
     */
}