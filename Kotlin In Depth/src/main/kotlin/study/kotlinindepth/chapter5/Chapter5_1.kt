package study.kotlinindepth.chapter5

import org.junit.jupiter.api.Test

/**
 * 5.1 코틀린을 활용한 함수형 프로그래밍
 */

/* 함수형 프로그래밍
    - 프로그램 코드를 불변 값을 변환하는 함수의 합성으로 구성
    - 함수형 언어는 함수를 일급 시민(first class)로 취급
        - 함수를 다른 일반적인 타입의 값과 똑같이 취급한다는 뜻
        - 변수에 값을 대입하거나 변수에서 값을 읽을 수 있고 함수에 값을 전달하거나 함수가 값을 반환할 수 있다.
        - 고차 함수를 정의할 수 있음.
            - 함수인 값을 데이터와 마찬가지로 조작할 수 있음.
            - 코드 추상화와 합성(composition)이 더 쉽게 가능하도록 함.
 */

/* 함수인 값(책 표현상 `함숫값`) (function value, functional value)
    - 함수가 반환하는 값(return value, result)이 아니다. 헷갈리지 말자.
    - 함숫값은 실제 런타임에 힙 메모리상에 존재하는 함수처럼 호출할 수 있는 객체란 점에서 일반 함수와 약간 다르다.
    - 함숫값이나 일반 함수나 호출할 수 있고 호출 시 결과를 돌려준다는 점은 동일하다.
 */

/* 람다
    - 단순한 형태의 문법을 사용해 정의하는 이름이 없는 지역 함수
    - 형태
        - { `파라미터` -> `계산식` }
    - 명시적인 return 불필요
    - 컴파일러는 파라미터 타입을 문맥으로 추론
 */

class Chapter5_1 {
    /**
     * 5.1.1 고차 함수
     */

    // 람다 예시 - 배열 생성자
    val squares = IntArray(5) { n -> n*n } // 0, 1, 4, 9, 16

    // 예시 함수 - 정수 배열의 원소의 합계를 계산하는 함수
    fun sum(numbers: IntArray): Int {
        var result = numbers.firstOrNull() ?: throw java.lang.IllegalArgumentException("Empty array")

        for (i in 1..numbers.lastIndex) result += numbers[i]

        return result
    }

    @Test
    fun sumTest1() {
        println(sum(intArrayOf(1,2,3))) // 6
    }

    // 예시 함수를 일반화해보기
    fun aggregate(numbers: IntArray, op: (Int, Int) -> Int): Int {
        // op를 읽을 때는, 파라미터로 (Int, Int)를 받아 (Int)를 결과로 내놓는 함수이다.
        var result = numbers.firstOrNull() ?: throw IllegalArgumentException("Empty array")

        for (i in 1..numbers.lastIndex) result = op(result, numbers[i])
        // 5.1.2 invoke 메서드 예제
//        for (i in 1..numbers.lastIndex) result = op.invoke(result, numbers[i])

        return result
    }

    fun sumByAggregate(numbers: IntArray) = aggregate(numbers, { result, op -> result + op })
    fun maxByAggregate(numbers: IntArray) = aggregate(numbers, { result, op -> if (op > result) op else result })

    @Test
    fun aggregateTest() {
        println(sumByAggregate(intArrayOf(1,2,3))) // 6
        println(maxByAggregate(intArrayOf(1,2,3))) // 3
    }

    /**
     * 5.1.2 함수 타입
     */

    /* 함수 타입
        - 함수처럼 쓰일 수 있는 값들을 표시하는 타입
        - 함수 시그니처(signature)와 비슷
        - 구성
            1. 괄호로 둘러싸인 파라미터 타입 목록은 함숫값에 전달될 데이터의 종류와 수 정의
            2. 반환 타입은 함수 타입의 함숫값을 호출하면 돌려받게 되는 값의 타입을 정의
                - 반환 타입을 반드시 명시해야 함. 반환값이 없는 경우 Unit을 반환 타입으로 사용
        - 함숫값은 일반 함수처럼 호출할 수 있다. 혹은 invoke() 메서드를 활용
     */

    /* 자바의 단일 추상 메서드(Single Abstract Method)
        - 자바는 SAM 인터페이스를 문맥에 따라 적절히 함수 타입처럼 취급한다.
            - 따라서 람다식이나 메서드 참조로 SAM 인터페이스를 인스턴스화할 수 있다.
        - 하지만 코틀린에서 함숫값은 항상 (a1, a2, a3 ...) -> R 형태이다.
            - 따라서 코틀린은 임의의 SAM 인터페이스를 암시적으로 변환할 수 없다.
        - 자바와의 상호 운용성을 위해 코틀린은 자바에 정의된 SAM 인터페이스 대신에 코틀린 함수 타입을 쓸 수 있게 변환해준다.
            - 쉽게 말해서 @FunctionalInterface 대신에
            - 코틀린 인터페이스인 `fun interface` 를 사용하면 SAM 인터페이스로 취급한다.
     */

    // 코틀린 함수형 인터페이스 (함수형 타입)
    fun interface StringConsumer {
        fun accept(s: String)
    }

    @Test
    fun samTest_unable() {
//        Consumer<String> consume = s -> System.out.println(s); // 자바에서는 컴파일 가능한 코드
//        val consume: Consumer<String> = { s -> println(s) } // 비슷한 코드이지만 코틀린에서는 불가능
        val consume = StringConsumer { s -> println(s) }
        consume.accept("hello")
    }

    // 함수가 인자를 받지 않는 경우에는 함수 타입의 파라미터 목록에 빈 괄호를 사용
    fun measureTime(action: () -> Unit): Long {
        val start = System.nanoTime()
        action()
        return System.nanoTime() - start
    }

    // 파라미터 타입을 둘러싼 괄호는 필수
    // 함수 타입이 파라미터를 하나만 받거나 전혀 받지 않는 경우에도 괄호를 반드시 써야 함
    val inc: (Int) -> Int = { n -> n+1 } // OK
//    val dec: Int -> Int = { n -> n-1 } // Error

    // 함수 타입의 값은 함수의 파라미터 뿐만 아니라 다른 타입이 쓰일 수 있는 모든 장소에 활용 가능하다.
    @Test
    fun funTypeExam() {
        val lessThan: (Int, Int) -> Boolean = { a,b -> a < b }
        println(lessThan(1, 2)) // true
    }

    // 변수 타입을 생략하면 정보가 충분하지 못해 컴파일러가 람다 파라미터의 타입을 추론할 수 없다.
//    val lessThan = { a, b -> a < b} // Error
    val lessThan = { a: Int, b: Int -> a < b } // OK

    // 다른 타입과 마찬가지로 함수 타입도 널이 될 수 있는 타입으로 지정 가능하다.
    // 이럴 때는 함수 타입 전체를 괄호로 둘러싼 다음에 물음표를 붙인다.
    // 괄호로 함수 타입을 둘러싸지 않으면 물음표의 효과가 완전히 달라진다.
    // 예를 들어, `() -> Unit?`는 Unit?타입의 값을 반환하는 함수를 표현하는 타입이다.
    fun measureTimeNullable(action: (() -> Unit)?): Long { // `() -> Unit?`과 주의!
        val start = System.nanoTime()
        action?.invoke()
        return System.nanoTime() - start
    }

    @Test
    fun nullTest() {
        println(measureTimeNullable(null))
    }

    // 함수 타입을 다른 함수 타입 안에 내포시켜서 고차 함수의 타입을 정의할 수 있다.
    @Test
    fun highLevelFunc() {
        val shifter: (Int) -> (Int) -> Int = { n -> { i -> i + n } }
        // `->`는 오른쪽 결합이다.
        // 쉽게 표현하면 `(Int) -> ((Int) -> Int)`와 같다.
        // Int 값을 인자로 받아 함수((Int) -> Int)를 반환하는 함수
        val inc = shifter(1)
        val dec = shifter(-1)

        println(inc(10)) // 11
        println(dec(10)) // 9

        // 만약 Int를 받아서 Int를 내놓는 함수를 인자로 받아 Int를 결과로 돌려주는 함수를 표현하고 싶다면,
        val evalAtZero: ((Int) -> (Int)) -> Int = { f -> f(0) }

        println(evalAtZero { n -> n + 1 }) // 1
        println(evalAtZero { n -> n - 1 }) // -1
    }

    // 함수 타입의 파라미터 목록에 파라미터 이름을 포함시킬 수도 있음.
    // 이때 파라미터 이름은 그냥 문서화를 위한 것이며, 타입이 표현하는 함수값에는 전혀 영향을 미치지 못 함.
    fun aggreagteWithParameterName(
        numbers: IntArray,
        op: (resultSoFar: Int, nextValue: Int) -> Int
    ): Int {
        return 0
    }

    /**
     * 5.1.3 람다와 익명 함수
     */

    /* 함수형 타입의 구체적인 값을 만들려면? - 방법 1. 람다식 : 함수를 묘사하되 이름을 지정하지 않음.
        - `{ result, op -> result + op }`
        - = `{ 파라미터 목록 -> 람다식의 몸통(본문)이 되는 식이나 문의 목록 }`
        - 반환 타입을 지정할 필요가 없다.
        - 람다의 본문으로부터 반환 타입을 자동으로 추론
        - 람다 본문에서 맨 마지막에 있는 식이 람다의 결괏값
        - 람다의 파라미터 목록은 괄호로 둘러싸지 않는다.
            - 파라미터를 괄호로 감싸면 구조 분해(destructuring) 선언이 된다.
        - 람다가 함수의 마지막 파라미터인 경우, 함수를 호출할 때 인자를 둘러싸는 괄호 밖에 이 람다를 위치시킬 수 있다. (권장!)
        - 람다에 인자가 없으면 화살표 기호(->)를 생략할 수 있다.
        - 인자가 하나밖에 없는 람다를 특별히 단순화할 수 있다.
            - 람다 인자가 하나인 경우에는 파라미터 목록과 화살표 기호를 생략 가능하다.
            - 유일한 파라미터는 미리 정해진 it이라는 이름을 사용해 가리킬 수 있다.
        - 람다의 파라미터 목록에서 사용하지 않는 람다 파라미터를 밑줄 기호(_)로 지정
     */

    fun singleParamLambda(s: String, condition: (Char) -> Boolean): Boolean {
        for (c in s) {
            if (!condition(c)) return false
        }
        return true
    }

    @Test
    fun singleParamLambdaTest() {
         println(singleParamLambda("Hello") { c -> c.isLetter() })
         println(singleParamLambda("Hello") { it.isLowerCase() })
    }

    fun underBarParamLambda(s: String, condition: (Int, Char) -> Boolean): Boolean {
        for (i in s.indices) {
            if (!condition(i, s[i])) return false
        }
        return true
    }

    @Test
    fun underBarParamLambdaTest() {
        println(underBarParamLambda("Hello") { _, c -> c.isLetter() })
        println(underBarParamLambda("Hello") { i, c -> i == 0 || c.isLowerCase() })
    }

    /* 함수형 타입의 구체적인 값을 만들려면? - 방법 2. 익명 함수
        - 일반 함수와 차이점
            - 1. 익명 함수에는 이름을 지정하지 않는다. fun 키워드 다음에 바로 파라미터 목록이 나온다.
            - 2. 람다와 마찬가지로 문맥에서 파라미터 타입을 추론할 수 있으면 파라미터 타입을 지정하지 않아도 된다.
            - 3. 함수 정의와 달리, 익명 함수는 식이기 때문에 인자로 함수를 넘기거나 변수에 대입하는 등 일반 값처럼 쓸 수 있다.
                - 이는 객체 정의와 익명 객체 식의 관계와도 비슷하다.
        - 람다와 달리 익명 함수에서는 반환 타입을 적을 수 있다.
            - 함수 본문이 식인 경우에는 반환 타입을 생략할 수 있다. (컴파일러가 추론 가능하다.)
            - 함수 본문이 블록인 경우(특히 함수 반환 타입이 Unit이 아닌 경우) 명시적으로 반환 타입을 지정해야 한다.
        - 람다와 달리 익명 함수를 인자 목록의 밖으로 보낼 수는 없다.
            - 즉, `function(param1, { lambda })` -> `function(param1) { lambda }` 변경 불가능
        - 지역 함수와 마찬가지로 람다나 익명 함수도 클로저, 또는 자신을 포함하는 외부 선언에 정의된 변수에 접근할 수 있다.
            - 특히 람다나 익명 함수도 외부 영역의 가변 변수 값을 변경할 수 있다.
            - 자바 람다는 외부 변수의 값을 변경할 수 없다.
    */

    fun anonymousFuncSum(numbers: IntArray) = aggregate(numbers, fun(result, op): Int { return result + op })

    fun outsideReferenceAnonyFunction(a: IntArray, action: (Int) -> Unit) {
        for (n in a) {
            action(n)
        }
    }

    @Test
    fun outsideReferTest() {
        var sum = 0
        outsideReferenceAnonyFunction(intArrayOf(1, 2, 3, 4)) {
            sum += it
        }
        println(sum) // 10
    }

    /**
     * 5.1.4 호출 가능 참조
     */


}