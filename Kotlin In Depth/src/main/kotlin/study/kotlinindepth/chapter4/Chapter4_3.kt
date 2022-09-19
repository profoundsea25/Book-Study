package study.kotlinindepth.chapter4

import org.junit.jupiter.api.Test
import java.io.File

/**
 * 4.3 단순한 변수 이상인 프로퍼티
 */

    /**
     * 4.3.1 최상위 프로퍼티
     */

    // 최 상위 수준에 프로퍼티 정의 가능. 전역 변수나 상수와 비슷한 역할이 가능하다.
    val highLevelProperty = "Hello, " // 최상위 불변 프로퍼티
    // `import study.kotlinindepth.chapter4.highLevelProperty`로 접근이 가능하다. (Application.kt 참고)
    // public/internal/private 지정 가능

    // 4.3.2 늦은 초기화 예시
    lateinit var text: String

class Chapter4_3 {
    @Test
    fun highLevelPropertyTest() {
//        val name = readLine() ?: return
        val name = "Kotlin"
        println("$highLevelProperty$name")
    }

    /**
     * 4.3.2 늦은 초기화
     */

    // 클래스 인스턴스가 생성된 뒤에, 그러나 해당 프로퍼티가 사용되는 시점보다는 이전에 초기화해야 할 때 사용
    // ex) 단위 테스트를 준비하는 코드나 의존 관계 주입에 의해 대입돼야 하는 프로퍼티
    // 초기화 하지 않은 상태는 기본값(null 등)을 대입하고, 실제 값을 필요할 때 대입할 수 있다.
    class Content {
        var text: String? = null

        fun loadFile(file: File) {
            text = file.readText()
        }
    }
    fun getContentSize(content: Content) = content.text?.length ?: 0

    // 위의 코드 : 실제 값이 항상 사용 전에 초기화되므로 절대 널이 될 수 없는 값이라는 사실을 알고 있음에도 늘 널 체크를 해야 함.
    class ContentLazyInit {
        lateinit var text: String

        fun loadFile(file: File) {
            text = file.readText()
        }
    }
    fun getContentSizeLazy(content: ContentLazyInit) = content.text.length

    // lazyinit 표시가 붙은 프로퍼티는 값을 읽으려고 시도할 때 초기화되지 않았을 경우,
    // UninitializedPropertyAccessException을 던지는 것을 제외하면 일반 프로퍼티와 동일. (암시적인 !! 연산자와 비슷)

    // lazyinit을 만들기 위한 조건
    // 1. 프로퍼티가 코드에서 변경될 수 있는 지점이 여러 곳일 수 있으므로 var로 정의해야 한다.
    // 2. 프로퍼티의 타입은 널이 아닌 타입이어야 하고, Int나 Boolean 같은 원시 값을 표현하는 타입이 아니어야 한다.
    // 3. 초기화 식을 지정해 값을 바로 대입할 수 없다. (이런 대입은 애초 lazyinit을 지정하는 의미가 없다.)

    // 최상위 프로퍼티와 지역 변수에서 늦은 초기화를 사용할 수 있다.
    fun readTextLazy() {
        text = readLine()!!
    }

    @Test
    fun highLevelPropertyLazyInitTest() {
        readTextLazy()
        println(text)
    }

    /**
     * 4.3.3 커스텀 접근자 사용하기
     */
}