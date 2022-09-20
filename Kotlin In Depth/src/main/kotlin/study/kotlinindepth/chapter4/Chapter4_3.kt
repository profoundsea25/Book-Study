package study.kotlinindepth.chapter4

import org.junit.jupiter.api.Test
import java.io.File
import java.lang.IllegalArgumentException
import java.util.*

/**
 * 4.3 단순한 변수 이상인 프로퍼티
 */


    /**
     * 4.3.1 최상위 프로퍼티
     */

    val highLevelProperty = "Hello, " // 최상위 불변 프로퍼티
    // 최 상위 수준에 프로퍼티 정의 가능. 전역 변수나 상수와 비슷한 역할이 가능하다.
    // `import study.kotlinindepth.chapter4.highLevelProperty`로 접근이 가능하다. (Application.kt 참고)
    // public/internal/private 지정 가능

    // 4.3.2 최상위 프로퍼티의 늦은 초기화 예시
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

     /* 늦은 초기화
        - 클래스 인스턴스가 생성된 뒤에, 그러나 해당 프로퍼티가 사용되는 시점보다는 이전에 초기화해야 할 때 사용
        - ex) 단위 테스트를 준비하는 코드나 의존 관계 주입에 의해 대입돼야 하는 프로퍼티
        - 초기화 하지 않은 상태는 기본값(null 등)을 대입하고, 실제 값을 필요할 때 대입할 수 있다.
     */
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

     /* lazyinit을 만들기 위한 조건
         1. 프로퍼티가 코드에서 변경될 수 있는 지점이 여러 곳일 수 있으므로 var로 정의해야 한다.
         2. 프로퍼티의 타입은 널이 아닌 타입이어야 하고, Int나 Boolean 같은 원시 값을 표현하는 타입이 아니어야 한다.
         3. 초기화 식을 지정해 값을 바로 대입할 수 없다. (이런 대입은 애초 lazyinit을 지정하는 의미가 없다.)
     */

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

    // 코틀린의 프로퍼티는 변수와 함수의 동작을 한 선언 안에 조합할 수 있는 기능이 있다. by 커스텀 접근자
    // 커스텀 접근자는 프로퍼티 값을 읽거나 쓸 때 호출되는 특별한 함수다.
    class Person(val firstName: String, val familyName: String, age: Int) {

        // getter
        val fullName1: String
            get() : String {
                return "$firstName $familyName"
            }
        // getter에는 파라미터가 없다.
        // 게터의 반환 타입은 프로퍼티의 타입과 같아야 한다.
        val fullName2: String
            get() = "$firstName $familyName"


         /* 뒷받침 필드(backing field)
            - 프로퍼티에 명시적으로 `field`를 사용하는 디폴트 접근자나 커스텀 접근자가 하나라도 있으면 뒷받침 필드가 생성됨
            - 위의 fullName 들은 뒷받침 필드가 없다. 메모리를 차지하지 않고, 호출될 때마다 연산된다.
            - 프로퍼티에 뒷받침하는 필드가 없으면 필드를 초기화할 수 없다.
         */
        val age1: Int = age
            get(): Int {
                println("Accessing age")
                return field
                // field는 키워드이다. 여기서는 age를 가리킨다.
                // 접근자의 본문 안에서만 유효하다.
            }


         /* setter
            - setter의 파라미터는 단 하나(value), 타입은 프로퍼티 자체의 타입과 같다. (-> 타입 생략)
            - 프로퍼티 초기화시 값을 바로 뒷받침하는 필드에 쓰기 때문에 프로퍼티 초기화는 세터를 호출하지 않는다.
         */
        var age2: Int? = null
            set(value) {
                if (value != null && value <= 0) {
                    throw IllegalArgumentException("Invalid age: $value")
                }
                field = value // -> 뒷받침하는 필드 생성
            }
    }

    class PersonForSetter(var firstName: String, var familyName: String) {

        var fullName3: String
            get(): String = "$firstName $familyName"
            set(value) {
                val names = value.split(" ")
                if (names.size != 2) {
                    throw IllegalArgumentException("Invalid full name: $value")
                }
                firstName = names[0]
                familyName = names[1]
            }
    }

    // 외부에서 프로퍼티 값을 변경하지 못하게 하고 싶을 때
    class PersonForPrivate(name: String) {
        var lastChanged: Date? = null
            private set // 클래스 바깥에서 변경 불가능

        var name: String = name
            set(value) {
                lastChanged = Date()
                field = value
            }
    }

    @Test
    fun getTest() {
        val person = Person("John", "Doe", 22)
        println(person.fullName1)
    }

    @Test
    fun setTest() {
        val person = Person("John", "Doe", 22)
        person.age2 = 20 // 커스텀 세터 호출
        println(person.age2) // 커스텀 게터 호출
    }

    /* 커스텀 연산자 사용법 (공식 답변)
        - 아래와 같은 경우 함수보다 프로퍼티를 사용하는 쪽을 권장함
            - 값을 계산하는 과정에서 에외가 발생할 여지가 없거나
            - 값을 계산하는 비용이 충분히 싸거나
            - 값을 캐시해 두거나
            - 클래스 인스턴스의 상태가 바뀌기 전에는 여러 번 프로퍼티를 읽거나
            - 함수를 호출해도 항상 똑같은 결과를 내는 경우
        - lateinit 프로퍼티의 경우 항상 자동으로 접근자가 생성된다. 따라서 커스텀 정의 불가능
        - 주생성자 파라미터로 선언된 프로퍼티에 대한 접근자도 지원하지 않는다.
            - 일반적인 프로퍼티가 아닌 생성자 파라미터를 사용하고 클래스 본문 안에서 프로퍼티에 그 값을 대입함으로써 해결 가능
    */


    /**
     * 4.3.4 지연 계산 프로퍼티와 위임
     */

    /* lazy 키워드
        - 처음 읽기 까지 그 값에 대한 계산을 미뤄두고 싶을 때 사용
        - 프로퍼티 정의 후 `lazy {프로퍼티를 초기화하는 코드 블록}`
        - 초기화 이후 프로퍼티의 값은 필드에 저장되며, 그 이후로는 프로퍼티 값을 읽을 때마다 저장된 값을 읽음.
        - lateinit과 다르게 lazy는 불변 프로퍼티가 아니다.
        - lazy는 일단 초기화된 다음에는 변경되지 않는다.
        - 스레드 안전함.
        - 스마트 캐스트 불가능
    */
    val textEx1 by lazy {
        File("data.txt").readText()
    }
    // 타입을 명시해주고 싶은 경우
    val textEx2: String by lazy { File("data.txt").readText() }

    // 간단한 초기화 방법 : 프로그램이 시작될 때 바로 파일을 읽음.
//    val textEx3 = File("data.txt").readText()

    // 게터를 사용한 프로퍼티 정의 : 프로퍼티 값을 읽을 때마다 파일을 매번 다시 읽음
    val textEx4 get() = File("data.txt").readText()

    @Test
    fun lazyTest() {
        when (val command = readLine() ?: return) {
            "print data" -> println(textEx1)
            "exit" -> return
        }
    }


    /* 위임 프로퍼티
        - 프로퍼티 처리에 필요한 데이터를 모아 유지하면서,
          읽기와 쓰기를 처리하는 위임 객체(delegate object)를 통해 프로퍼티를 구현
        - 위임 객체는 by 키워드 다음에 위치, 코틀린이 정한 규약을 만족하는 객체를 반환할 수 있는 임의의 식이 될 수 있다.
        - 종류
            - lazy
            - 프로퍼티를 읽거나 쓸 때마다 리스너에게 통지해주는 위임 -> 7장
            - 프로퍼티 값을 필드에 저장하는 대신 맵에 저장하는 위임 -> 11장
        - 현재는 위임 프로퍼티에 대해서 스마트 캐스트를 사용할 수 없음.
        - 위임은 구현이 모두 다를 수 있기 때문에 커스텀 접근자로 정의된 프로퍼티처럼 다뤄짐.
    */

    // 지역 변수에도 위임 가능
    fun longComputation(): Int { return 1234 }

    @Test
    fun testLazy() {
        val args: Array<String> = arrayOf("anything") // -> `anything: 1234` 출력
//        val args: Array<String> = emptyArray() // -> 아무것도 출력되지 않음
        val data by lazy { longComputation() } // lazy 지역 변수
        val name = args.firstOrNull() ?: return
        println("$name: $data") // name이 널이 아닐 때만 data에 접근할 수 있다.
    }
}