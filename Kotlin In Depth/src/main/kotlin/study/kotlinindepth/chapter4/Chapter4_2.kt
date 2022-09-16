package study.kotlinindepth.chapter4

import org.junit.jupiter.api.Test

/**
 * 4.2 널 가능성
 */
class Chapter4_2 {

    /**
     * 4.2.1 널이 될 수 있는 타입
     */
    // null을 받을 수 없는 함수
    fun isLetterString(s: String): Boolean {
        if (s.isEmpty()) return false
        for (ch in s) {
            if (!ch.isLetter()) return false
        }
        return true
    }

    @Test
    fun nullErrorTest() {
        println(isLetterString("abc")) // OK
//        println(isLetterString(null)) // error: null
    }

    // null을 받을 수 있는 함수
    fun isBooleanString(s: String?) = s == "false" || s == "true"

    @Test
    fun nullSafeTest() {
        println(isBooleanString(null)) // OK
        val s: String? = "abc"
//        val ss: String = s // error: type mismatch (String?과 String은 다른 타입으로 간주한다.)
    }

    @Test
    fun boxingExample() {
        val n: Int = 1 // 원시 타입의 값
        val x: Int? = 1 // 박싱한 타입의 값을 참조
    }

    // ?이 붙으면 기존에 제공하는 메서드들의 대부분이 불가능해진다.
//    fun isLetterString(s: String?): Boolean {
//        if (s.isEmpty()) return false
//
//        for (ch in s) {
//            if (!ch.isLetter()) return false
//        }
//        return true
//    }

    // 대신, nullable 타입에 가능한 메서드들이 추가된다.
    fun exclaim(s: String?) {
        println(s + "!")
    }

    @Test
    fun nullableTest() {
        exclaim(null) // null!
    }


    /**
     * 4.2.2 널 가능성과 스마트 캐스트
     */
    fun isLetterStringNullCheck(s: String?): Boolean {
        if (s == null) return false

        // nullable이어도 null-check를 포함하면 원래 메서드와 프로퍼티에 접근 가능하다.
        // 이를 스마트 캐스트라고 한다.
        // s cannot be null here
        if (s.isEmpty()) return false

        for (ch in s) {
            if (!ch.isLetter()) return false
        }

        return true
    }

    // 스마트 캐스트는 when이나 루프 같은 조건 검사가 들어가는 다른 문이나 식 안에서도 작동함
    fun describeNumber(n: Int?) = when (n) {
        null -> "null"
        // 아래부터는 null이 될 수 없다.
        in 0..10 -> "small"
        in 11..100 -> "large"
        else -> "out of range"
    }

    fun isSingleChar(s: String?) = s != null && s.length == 1

    // 스마트 캐스트는 대상 변수의 값이 null 검사 지점과 사용 지점 사이에서 변하지 않는다고 컴파일러가 확신할 수 있어야 한다.
//    @Test
//    fun smartCastTest() {
//        var s = readLine() // String?
//        if (s != null) {
//            s = readLine()
//            println(s.length) // Error
//        }
//    }


    /**
     * 4.2.3 널 아님 단언 연산자
     */
    // 예시 (주석을 풀면 테스트가 안 됨. 클래스 필드 초기화가 안 되기 때문!)
    @Test
    fun nullAssertion() {
        val n = readLine()!!.toInt()
    }

    @Test
    fun nullTest() {
        var name: String? = null

        fun initialize() {
            name = "John"
        }

        fun sayHello() {
            println(name!!.uppercase())
        }

        initialize()
        sayHello()
    }

    /**
     * 4.2.4 안전한 호출 연산자
     */

    // 아래에서는 정상 작동, but 이를 파이프로 연결하면 NPE 가능
    fun readInt() = readLine()!!.toInt()

    // 안전한 방법 (메서드에 ?를 붙인다)
    fun readIntSafe() = readLine()?.toInt()

    // 위의 함수는 다음과 같다.
    fun readIntSafeExplain(): Int? {
        val tmp = readLine()

        return if (tmp != null) tmp.toInt() else null
    }

    // "수신 객체가 널이 아닌 경우에는 의미 있는 일을 하고, 수신 객체가 널인 경우에는 널을 반환하라"를 표현하면,
    fun nullSafeCall() = println(readLine()?.toInt()?.toString(16))

    // 안전한 호출 연산자가 널을 반환할 수 있으므로, 그 반환값 타입도 nullable 함을 인지하자.
    @Test
    fun nullSafeCallTest() {
        val n = readInt() // Int?

        if (n != null) {
            println(n + 1)
        } else {
            println("No Value")
        }
    }


    /**
     * 엘비스 연산자
     */
    // 널이 될 수 없는 값을 다룰 때 유용한 연산자로 널 복합 연산자 ?:
    // 널을 대신할 디폴트 값을 지정할 수 있음
    fun sayHello(name: String?) {
        println("Hello, " + (name ?: "Unknown"))
    }

    // sayHello는 아래와 같다.
    fun sayHelloExplain(name: String?) {
        println("Hello, " + (if (name != null) name else "Unknown"))
    }

    @Test
    fun sayHelloTest() {
        sayHello("John") // Hello, John
        sayHello(null) // Hello, Unknown
    }

    // 입력이 널을 반호나할 경우 0에 n을 대입하는 예시
    @Test
    fun elvisTest() {
        val n = readLine()?.toInt() ?: 0
    }

    // return이나 throw 같은 제어 흐름을 깨는 코드를 엘비스 연산자 오른쪽에 넣는 방법이 있다.
    class Name(val firstName: String, val familyName: String?)

    class Person(val name: Name?) {
        fun describe(): String {
            val currentName = name ?: return "Unknown"
            return "${currentName.firstName} ${currentName.familyName}"
        }
    }

    @Test
    fun nameTest() {
        println(Person(Name("John", "Doe")).describe()) // John Doe
        println(Person(null).describe()) // Unknown
    }

    // 엘비스 연산자는 or 등의 중위 연산자와 in, !in 사이에 위치
    // 비교/동등성 연자나 ||, && 대입보다 더 우선순위가 높다.

}