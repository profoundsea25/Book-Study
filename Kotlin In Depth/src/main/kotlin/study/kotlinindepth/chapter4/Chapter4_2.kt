package study.kotlinindepth.chapter4

import org.junit.jupiter.api.Test

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
    // 예시
    val n = readLine()!!.toInt()

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
}