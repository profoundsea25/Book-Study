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

    /* 호출 가능 참조 (callable reference)
        - 이미 정의된 함수를 함숫값처럼 고차 함수에 넘기고 싶을 때 사용
            - 물론 람다식으로 감싸서 넘길 수도 있다.
        - 메서드 앞에 `::`를 붙여 메서드 참조임을 나타낸다.
        - 호출 가능 참조를 만들 때는 함수 이름을 간단한 형태로만 써야 한다.
            - 다른 패키지에 들어있는 함수의 호출 가능 참조를 만들려먼 먼저 함수를 임포트해야 한다.
            - `::`을 클래스 이름 앞에 적용하면 클래스의 생성자에 대한 호출 가능 참조를 얻는다.
        - 바인딩된 호출 가능 참조(bound callable reference)
            - 주어진 클래스 인스턴스의 문맥 안에서 멤버 함수를 호출하고 싶을 때 사용
        - 특정 인스턴스와 바인딩하지 않고 멤버 함수를 가리키는 호출 가능 참조도 있다. -> 5.5절 참고
        - 호출 가능 참조 자체는 오버로딩된 함수를 구분할 수 없다.
            - 오버로딩된 함수 중 어떤 함수를 참조할지 명확히 하려면 컴파일러에게 타입을 지정해줘야 한다.
        - 호출 가능 참조 뒤에 괄호를 바로 붙이는 문법은 예약되어 있다.
            - 향후 괄호를 사용해 호출 가능 참조를 더 세분화할 수 있는 여지(구체적인 함수 지그니처를 지정)를 남기고자 함.
            - 호출 가능 참조를 직접 호출하고 싶다면 참조 전체를 괄호로 둘러싼 다음에 인자를 지정해야 한다.
        - 코틀린 프로퍼티에 대한 호출 가능 참조를 만들 수도 있다.
            - 이런 참조 자체는 실제로는 함숫값이 아니고, 프로퍼티 정보를 담고 있는 리플렉션(reflection) 객체다.
            - 이 객체의 getter 프로퍼티를 이용하면 게터 함수에 해당하는 함숫값에 접근할 수 있다.
            - var 선언의 경우 리플렉션 객체의 setter 프로퍼티를 통해 세터 함수에 접근할 수 있다.
        - 현재는 지역 변수에 대한 호출 가능 참조를 지원하지 않음. (향후 추가 될 수 있음)
     */

    /*  차이점 : 코틀린 호출 가능 참조 vs 자바 8 메서드 참조
        - 1. 자바에는 없는 종류의 선언을 코틀린이 지원하기 때문에 호출 가능 참조는 자바의 메서드 참조보다 종류가 더 많다.
        - 2. 코틀린의 호출 가능 참조는 일급 시민 식이지만, 자바의 메서드 참조는 함수형 인터페이스 내에서만 의미가 있다.
            - 즉, 자바의 메서드 참조에는 정해진 타입이 없다.
        - 3. 호출 가능 참조는 단순히 함숫값만이 아니라,
             런타임에 함수나 프로퍼티의 속성(애프리뷰트)를 얻을 때 사용할 수 있는 리플렉션 객체이기도 하다.
    */

    // 람다식으로 감싸서 넘기는 방법
    fun referenceExam(s: String, condition: (Char) -> Boolean): Boolean {
        for (c in s) {
            if (!condition(c)) return false
        }
        return true
    }

    fun isCapitalLetter(c: Char) = c.isUpperCase() && c.isLetter()

    @Test
    fun referenceLambdaTest() {
        println(referenceExam("Hello") { c -> isCapitalLetter(c) }) // false
        println(referenceExam("Hello") { isCapitalLetter(it) }) // false
    }

    @Test
    fun callableReferenceTest() {
        println(referenceExam("Hello", ::isCapitalLetter)) // false
    }

    fun evalAtZero(f: (Int) -> Int) = f(0)
    fun inc(n: Int) = n + 1
    fun dec(n: Int) = n - 1

    @Test
    fun callableReferenceTest2() {
        println(evalAtZero(::inc)) // 1
        println(evalAtZero(::dec)) // -1
    }

    class Person(val firstName: String, val familyName: String) {
        fun hasNameOf(name: String) = name.equals(firstName, ignoreCase = true)
    }

    @Test
    fun callableReferenceTest3() {
        val createPerson = ::Person
        createPerson("John", "Doe")
    }

    @Test
    fun boundCallableReferenceTest() {
        val isJohn = Person("John", "Doe")::hasNameOf

        println(isJohn("JOHN")) // true
        println(isJohn("Jake")) // false
    }

    @Test
    fun overloadingCallableReferenceTest() {
        fun max(a: Int, b: Int) = if (a > b) a else b
        fun max(a: Double, b: Double) = if (a > b) a else b
        val f: (Int, Int) -> Int = ::max // OK
//        val g = ::max // error: overload resolution ambiguity
    }

    @Test
    fun callableReferenceBraketTest() {
        fun max(a: Int, b: Int) = if (a > b) a else b

        println((::max)(1,2)) // 2
//        println(::max(1,2)) // error : this syntax is reserved for future use
    }

    @Test
    fun kotlinPropertyCallableReferenceTest() {
        class Person(var firstName: String, var familyName: String)

        val person = Person("John", "Doe")
        val readName = person::firstName.getter // 게터 참조
        val writeFamily = person::familyName.setter // 세터 참조

        println(readName()) // John
        writeFamily("Smith")
        println(person.familyName) // Smith
    }

    /**
     * 5.1.5 인라인 함수와 프로퍼티
     */

    // TODO:복습 필요, 내용 이해가 쉽지 않음
    /* 인라인(inline) 기법
        - 고차 함수와 함숫값을 사용하면 함수가 객체로 표현되기 때문에 성능 차원에서 부가 비용이 발생한다.
            - 또한 익명 함수나 람다가 외부 영역의 변수를 참조하면 고차 함수에 함숫값을 넘길 때마다
              이런 외부 영역의 변수를 포획할 수 있는 구조도 만들어서 넘겨야 한다.
            - 함숫값을 호출할 때는 컴파일러가 함숫값의 정적인 타입을 알 수 없기 때문에
              동적으로 가상 호출을 사용해 어떤 함수 구현을 사용할지 디스패치해야 한다.
        - inline 기법
            - 위와 같은 함숫값을 사용할 때 발생하는 런탕미 비용을 줄이기 위한 해법
            - 기본적인 아니디어는 함숫값을 사용하는 고차 함수를 호출하는 부분을 해당 함수의 본문으로 대헤하는 인라인 기법을 사용
            - 인라인이 될 수 있는 함수를 구별하기 위해 프로그래머는 `inline` 변경자를 함수 앞에 붙인다.
        - 인라인 함수를 쓰면 컴파일된 코드의 크기가 커지지만, 지혜롭게 사용하면 성능을 크게 높일 수 있다.
            - 특히 함수가 상대적으로 작은 경우 성능이 크게 향상된다.
            - 코틀린 표준 라이브러리가 제공하는 여러 고차 함수 중 상당수가 실제로 인라인 함수다.
        - 다른 언어(C++)와 달리, 코틀린의 inline 변경자는 컴파일러가 상황에 따라 무시해도 되는 최적화를 위한 힌트가 아니다.
            - inline이 붙은 코틀린 함수는 가능하면 항상 인라인이 된다.
            - 인라인이 불가능한 경우에는 컴파일 오류로 간주된다.
        - 인라인이 될 수 있는 람다를 사용해 할 수 있는 일은 몇 가지로 제한된다.
            - 1. 람다를 호출하는 경우
            - 2. 다른 인라인 함수에 인라인이 되도록 넘기는 경우
            - 인라인 함수는 실행 시점에 별도의 존재가 아니므로 변수에 저장되거나 인라인 함수가 아닌 함수에 전달될 수 없다.
            - 마찬가지 이유로 인라인 함수가 널이 될 수 있는 함수 타입의 인자를 받을 수도 없다.
                - 이 경우 특정 람다를 인라인하지 말라고 파라미터 앞에 noinline 변경자를 붙일 수 있다.
        - 어떤 함수에 인라인할 수 있는 파라미터가 없다면
            - 이 함수를 호출한 지점을 함수 본문으로 대치해도 런타임에 크게 이득이 없다.
            - 보통 이런 함수는 인라인할 가치가 없는 것으로 여겨 코틀린 컴파일러는 경고를 표시한다.
        - 공개 인라인 함수에 비공개 멤버를 넘기려고 하면
            - 인라인 함수의 본문이 호출 지점을 대신한다.
            - 외부에서 캡슐화를 깰 수 있게 된다.
            - 비공개 코드가 외부로 노출되는 일을 방지하기 위해 코틀린은 인라인 함수에 비공개 멤버를 전달하는 것을 금지한다.
        - 프로퍼티 접근자도 인라인이 가능하다.
            - 이 기능을 사용하면 함수 호출을 없애기 때문에 프로퍼티를 읽고 쓰는 성능을 향상시킬 수 있다.
        - 프로퍼티 자체에 inline 변경자를 붙일 수도 있다.
            - 이렇게 하면 컴파일러가 게터와 (프로퍼티가 가변 프로퍼티인 경우) 세터를 모두 인라인해준다.
            - 프로퍼티에 대한 인라인은 뒷받침하는 필드가 없는 프로퍼티에 대해서만 가능하다.
            - 함수와 비슷하게 프로퍼티가 공개 프로퍼티인 경우,
              프로퍼티의 게터나 세터 안에서 비공개 선언을 참조하면 인라인이 불가능하다.
     */

    inline fun indexOf(numbers: IntArray, condition: (Int) -> Boolean): Int {
        for (i in numbers.indices) {
            if (condition(numbers[i])) return i
        }
        return -1
    }

    @Test
    fun inlineTest() {
        println(indexOf(intArrayOf(4,3,2,1)) { it < 3 }) // 2
    }

    // 위의 함수(`indexOf`)는 아래와 같이 번역된다.
    @Test
    fun nonInlineTest() {
        val numbers = intArrayOf(4,3,2,1)
        var index = -1

        for (i in numbers.indices) {
            if (numbers[i] < 3) {
                index = i
                break
            }
        }
        println(index)
    }

    var lastAction: () -> Unit = {}

    inline fun inlineMethodCannotBeInVar(action: () -> Unit) {
        action()
//        lastAction = action // Error
    }

//    inline fun inlineMethodCannotBeNullable(a: IntArray, action: ((Int) -> Unit)?) {
//        if (action == null) return
//        for (n in a) action(n)
//    }

    inline fun inlineMethodCannotBeNullable(a: IntArray, noinline action: ((Int) -> Unit)?) {
        if (action == null) return
        for (n in a) action(n)
    }

    class InlinePrivateTestClass(private val firstName: String, private val familyName: String) {
        inline fun sendMessage(message: () -> String) {
//            println("$firstName $familyName: ${message()}") // Error
        }
    }

    class InlineGetterAndSetterTestClass(var firstName: String, var familyName: String) {
        var fullName
        inline get() = "$firstName $familyName" // inline 게터
        set(value) {} // inline 이 아닌 세터
    }

    class InlinePropertyTestClass(var firstName: String, var familyName: String) {
        inline var fullName // inline 게터와 세터
            get() = "$firstName $familyName"
            set(value) {}
    }

//    class InlinePrivatePropertyTestClass(private val firstName: String, private val familyName: String) {
//        inline var age = 0 // Error : Property has a backing field
//        // Error: firstName and familyName are private
//        inline val fullName get() = "$firstName $familyName"
//    }

    /**
     * 5.1.6 비지역적 제어 흐름
     */
    /* 비지역적 제어 흐름 (return)
        - 고차 함수를 사용하면 `return` 문 등과 같이 일반적인 제어 흐름을 깨는 명령을 사용할 때 문제가 생긴다.
            - `return` 문은 디폴트로 자신을 둘러싸고 있는 fun, get, set 으로 정의된 가장 안쪽 함수로부터 제어 흐름을 반환시킨다.
            - JVM 에서는 람다가 효율적으로 자신을 둘러싸고 있는 함수를 반환시킬 방법이 없으므로 이를 금지한다.
            - 이 경우 해결 방법은 람다 대신 익명 함수를 사용하는 것이다.
        - 람다 자체로부터 제어 흐름을 반환하고 싶다면 `return` 문에 문맥 이름을 추가해야 한다.
            - 일반적으로 함수 리터럴 식에 이름을 붙여서 문맥 이름을 만들 수 있다.
            - 이런 한정시킨 `return`을 일반 함수에서도 사용할 수 있다. (불필요한 중복인 경우가 많다.)
        - 람다가 인라인될 경우에는 인라인된 코드를 둘러싸고 있는 함수에서 반환할 때 return 문을 사용할 수 있다.
            - 고차 함수가 인라인 함수라면 고차 함수를 호출하는 코드를 고차 함수 본문과 람다 본문으로 대체하기 때문에 가능하다.
            - 고차 함수가 인라인이 될 수 있는 람다를 받는데,
              이 고차 함수의 본문에서 람다를 직접 호출하지는 않고 지역 함수나 지역 클래스의 메서드 등의 다른 문맥에서 간접적으로 호출할 수 있다.
                - 이 경우에도 람다를 인라인할 수는 있다.
            - 그러나 인라인을 한 이후 람다에서 사용하는 `return`문이 고차 함수를 호출하는 쪽의 함수를 반환시킬 수는 없다.
                - 인라인을 했음에도 불구하고 람다의 `return`과 람다를 실행하주는 함수가 서로 다른 실행 스택 프레임을 차지하기 떄문이다.
                - 따라서 이런 식으로 함수 파라미터를 호출하는 일은 디폴트로 금지되어 있다.
                - 이런 호출을 허용하려면 함수형 파라미터 앞에 `crossinline` 변경자를 붙여야 한다.
                - 함숫값을 인라인시키도록 남겨두는 대신 람다 안에서 비지역 `return`을 사용하지 못하게 막는 역할을 한다.
        - `break`이나 `continue`를 쓸 때도 비지역적 제어 흐름을 만들어 낼 수 있다.
            - 이런 경우 `break`이나 `continue`는 람다를 둘러싼 루프를 대상으로 제어 흐름을 변경하게 된다.
            - 현재는 람다가 인라인되더라도 이런 비지역적 `break`이나 `continue`를 사용하지 못한다.
     */
    @Test
    fun nonLocalControlTest() {
        fun forEach(a: IntArray, action: (Int) -> Unit) {
            for (n in a) action(n)
        }

        // 람다 : 불가능
        forEach(intArrayOf(1, 2, 3, 4)) {
//            if (it < 2 || it > 3) return // error : nonLocalControlTest 메서드를 종료시킴
            println(it)
        }

        // 익명 함수 : 가능
        forEach(intArrayOf(1,2,3,4), fun(it: Int) {
            if (it < 2 || it > 3) return
            println(it)
        })

        // 문맥 이름 붙이기
        val action: (Int) -> Unit = myFun@{
            if (it < 2 || it > 3) return@myFun
            println(it)
        }

        // 람다 : 문맥 이름 붙이기
        forEach(intArrayOf(1, 2, 3, 4)) {
            if (it < 2 || it > 3) return@forEach
            println(it)
        }

        // 인라인 람다
        forEachInline(intArrayOf(1, 2, 3, 4)) {
            if (it < 2 || it > 3) return
            println(it)
        }

        // 크로스 인라인 람다
        forEachCrossInline(intArrayOf(1, 2, 3, 4)) {
//            if (it < 2 || it > 3) return // Error
            println(it)
        }
    }

    inline fun forEachInline(a: IntArray, action: (Int) -> Unit) {
        for (n in a) action(n)
    }

//    inline fun forEachInlineError(a: IntArray, action: (Int) -> Unit) = object {
//        for run() {
//            for (n in a) {
//                action(n) // Error
//            }
//        }
//    }

    inline fun forEachCrossInline(a: IntArray, crossinline action: (Int) -> Unit) = object {
        fun run() {
            for (n in a) {
                action(n)
            }
        }
    }


}