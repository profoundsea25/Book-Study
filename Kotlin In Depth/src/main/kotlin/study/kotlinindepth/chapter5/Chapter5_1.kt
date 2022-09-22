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

}