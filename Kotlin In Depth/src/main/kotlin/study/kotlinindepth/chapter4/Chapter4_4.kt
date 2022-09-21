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
            - 내포된 object도 인스턴스가 생기면 자신을 둘러싼 클래스의 비공개 멤버에 접근 가능하다.
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

    /* 동반 객체 (companion object)
        - `companion` 키워드를 덧붙인 내포된 객체
        - 다른 내포된 객체와 같이 동작하지만 한 가지 예외가 있음.
            - 동반 객체의 멤버에 접근할 때는 동반 객체의 이름을 사용하지 않고 동반 객체가 들어있는 외부 클래스의 이름을 사용할 수 있다.
        - 이름을 사용하지 않아도 됨. (권장 사항)
            - 동반 객체 이름을 생략한 경우 컴파일러는 동반 객체의 디폴트 일므을 `Companion`으로 가정한다.
            - 동반 객체의 멤버를 임포트하고 싶을 때는 객체 이름을 명시해야 한다. (접근할 때는 명시할 필요 없다.)
        - 하나의 클래스에 동반 객체가 둘 이상 있을 수는 없음.
        - 사용 예시 : Factory 디자인 패턴
            - 생성자를 직접 사용하고 싶지 않을 때 companion object 사용
            - 생성자를 비공개로 지정하고 클래스 외부에서 사용할 수 없게 한 다음,
            - 내포된 객체에 팩토리 메서드 역할을 하는 함수를 정의하고,
            - 그 함수 안에서 필요에 따라 객체의 생성자를 호출
        - `companion` 변경자는 최상위 객체 앞이나 다른 객체에 내포된 객체 앞에 붙이는 것은 금지된다.
        - Java의 static과 비교
            - 외부 클래스와 똑같은 전역 상태를 공유
            - 외부 클래스의 모든 멤버에 멤버 가시성과 무관하게 접근 가능
            - 차이점 : 코틀린 동반 객체의 문맥은 객체 인스턴스이다. 따라서 자바 정적 멤버/정적 멤버 클래스보다 더 유연하다.
                - 코틀린 동반 객체는 다른 상위 타입을 상속할수도 있고 일반 객체처럼 여기저기에 전달될 수 있기 때문
        - java의 static 초기화 블록처럼 동반 객체 안에서도 init 블록을 사용할 수 있다.
     */

    // object를 사용한 팩토리 메서드 기법
    class ApplicationNestedObject private constructor(val name: String) {
        object Factory {
            fun create(args: Array<String>): ApplicationNestedObject? {
                val name = args.firstOrNull() ?: return null
                return ApplicationNestedObject(name)
            }
        }
    }

    @Test
    fun nestedObjectTest() {
        val args = arrayOf("name")
//        val app = ApplicationNestedObject("name") // private 생성자이기 때문에 객체 생성 불가
        val app = ApplicationNestedObject.Factory.create(args) ?: return
        println("ApplicationNestedObject started: ${app.name}")
    }

    // companion object를 사용한 팩토리 메서드 기법
    class ApplicationCompanionObject private constructor(val name: String) {
        companion object Factory {
            fun create(args: Array<String>): ApplicationCompanionObject? {
                val name = args.firstOrNull() ?: return null
                return ApplicationCompanionObject(name)
            }
        }
    }

    @Test
    fun companionObjectTest() {
        val args = arrayOf("name")
        val app = ApplicationCompanionObject.create(args) ?: return // Factory 호출이 필요가 없다.
        println("ApplicationCompanionObject started: ${app.name}")
    }

    // companion object는 이름을 아예 생략할 수도 있다.
    class ApplicationCompanionObjectNoName private constructor(val name: String) {
        companion object {
            fun create(args: Array<String>): ApplicationCompanionObjectNoName? {
                val name = args.firstOrNull() ?: return null
                return ApplicationCompanionObjectNoName(name)
            }
        }
    }


    /**
     * 4.4.3 객체 식
     */

    /* 객체 식
        - 명시적 선언 없이 객체를 바로 생성할 수 있는 특별한 식
        - 자바 익명 클래스와 아주 비슷
        - 객체 식도 식이므로 객체식이 만들어내는 값을 변수에 대입할 수 있다.
            - 다만 클래스나 객체 식과 달리 object를 함수 안에 정의할 수는 없다.
        - 객체 식의 반환타입은 객체 식 안에 정의된 모든 멤버가 들어있는 클래스를 표현하는 익명 객체 타입이다.
            - 이런 타입은 단 하나만 존재한다.
            - 멤버가 모두 완전히 똑같은 두 객체 식이 있다고 해도, 둘의 타입은 서로 다르다.
        - 객체 식이 만들어내는 객체도 다른 클래스 인스턴스와 마찬가지로 사용할 수 있다.
        - 익명 객체 타입은 지역 선언이나 비공개 선언에만 전달될 수 있다.
            - 예를 들어 익명 객체 타입을 최상위 함수로 정의하면 객체 멤버에 접근할 때 컴파일 오류가 발생한다.
                - 이 때 타입은 객체 식에 지정된 상위 타입이 된다. 타입을 지정하지 않으면 Any를 상위 타입으로 가정한다.
        - 지역 함수, 클래스와 마찬가지로 객체 식도 자신을 둘러싼 코드 영역의 변수를 포획할 수 있다.
            - 포획한 가변 변수를 객체 본무에서 변경 가능하다. (컴파일 시 데이터 공유를 위해 필요한 래퍼가 생성된다.)
        - 지연 초기화되는 객체 선언과 달리 객체 식이 만들어내는 객체는 객체 인스턴스가 생성된 직후 바로 초기화된다.
        - 객체 식은 클래스 상속과 조합했을 때 더 강력해진다.
            - 객체 식은 기존 클래스의 하위 클래스를 선언하지 않고도 기존 클래스를 약간만 변경해 기술하는 간결한 방법을 제공한다.
     */

    @Test
    fun objectExpressionTest() {
        fun midPoint(xRange: IntRange, yRange: IntRange) = object {
            val x = (xRange.first + xRange.last)/2
            val y = (yRange.first + yRange.last)/2
        }

        // 아래와 같은 메서드 내의 object 선언은 불가능
        // object 선언이 싱글턴을 표현하지만 지역 object들은 자신을 둘러싼 바깥 함수가 호출될 때마다 매번 다시 생성돼야 하기 때문이다.
//        object MidPoint {
//            val x = (xRange.first + xRange.last)/2
//            val y = (yRange.first + yRange.last)/2
//        }

        val midPoint = midPoint(1..5, 2..6)

        println("${midPoint.x}, ${midPoint.y}")
    }

    @Test
    fun anonymousObjectTest() {
        val o = object {
            val x = 1
            val y = 2
        }
        println(o.x + o.y)
    }

    @Test
    fun objectExpressionCaptureTest() {
        var x = 1

        val o = object {
            fun change() {
                x = 2
            }
        }

        o.change()
        println(x) // x = 2
    }

    @Test
    fun objectExpressionInitTest() {
        var x = 1

        val o = object {
            val a = x++
        }

        println("o.a = ${o.a}") // o.a = 1
        println("x = $x") // x = 2
    }

}