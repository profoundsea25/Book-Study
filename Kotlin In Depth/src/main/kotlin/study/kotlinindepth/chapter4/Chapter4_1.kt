package study.kotlinindepth.chapter4


//class Person {
//    var firstName: String = ""
//    var familyName: String = ""
//    var age: Int = 0
//
//    fun fullName() = "$firstName $familyName"
//
//    fun showMe() {
//        println("${fullName()}: $age")
//    }
//
//    fun setName(firstName: String, familyName: String) {
//        this.firstName = firstName
//        this.familyName = familyName
//    }
//fun showAge(p: Person) = println(p.age)
//fun readAge(p: Person) {
//    p.age = readLine()!!.toInt()
//}
//fun showFullName(p: Person) = println(p.fullName())
//fun main() {
//    val person = Person()
//
//    person.firstName = "John"
//    person.familyName = "Doe"
//    person.age = 25
//
//    person.showMe()
//}


//class Person(firstName: String, familyName: String) {
//    val fullName = "$firstName $familyName"
//
//    init {
//        println("Created new Person Instance: $fullName")
//    }
//
//    init {
//        // error: 'return' is not allowed here
//        if (firstName.isEmpty() && familyName.isEmpty()) return
//        println("Created new Person Instance: $fullName")
//    }
//}
//fun main() {
//    val person = Person("John", "Doe")
//    println(person.fullName)
//}


//class Person(fullName: String) {
//    val firstName: String
//    val familyName: String
//
//    init {
//        val names = fullName.split(" ")
//        if (names.size != 2) {
//            throw java.lang.IllegalArgumentException("Invalid name: $fullName")
//        }
//        firstName = names[0]
//        familyName = names[1]
//    }
//}
//fun main() {
////    val person = Person("John") // Invalid name
//    val person = Person("John Doe")
//    println(person.firstName)
//}


//}


//}


//class Person(val firstName: String, familyName: String) {
//    val fullName = "$firstName $familyName"
//
//    fun printFirstName() {
//        println(firstName) // 프로퍼티 초기화가 필요하다.
//        // 가장 쉬운 방법은 클래스 생성자에 val/var을 붙이는 것
//    }
//// 인텔리J가 원하는 코딩 스타일
//class Person(val firstName: String, val familyName: String = "") {
//    fun fullName() = "$firstName $familyName"
//}
//class Room(vararg val persons: Person) {
//    fun showNames() {
//        for (person in persons) println(person.fullName())
//    }
//}
//fun main() {
//    val room = Room(Person("John"), Person("Jane","Smith"))
//    room.showNames()


//class Person {
//    val firstName: String
//    val familyName: String
//
//    constructor(firstName: String, familyName: String) {
//        this.firstName = firstName
//        this.familyName = familyName
//    }
//
//    constructor(fullName: String) {
//        val names = fullName.split(" ")
//        if (names.size != 2) {
//            throw java.lang.IllegalArgumentException("Invalid name: $fullName")
//        }
//        firstName = names[0]
//        familyName = names[1]
//    }
//}
//// 혹은
//class Person {
//    val fullName: String
//    constructor(firstName: String, familyName: String): this("$firstName $familyName")
//    constructor(fullName: String) {
//        this.fullName = fullName
//    }
//}
//// 혹은
//class Person(val fullName: String) {
//    constructor(firstName: String, familyName: String):
//            this("$firstName $familyName")
//    // constructor 에는 val/var를 인수로 전해줄 수 없다.
//}


//class Person(private val firstName: String, private val familyName: String) {
//    fun fullName() = "$firstName $familyName"
//}
//
//fun main() {
//    val person = Person("John", "Doe")
//    println(person.firstName) // private이므로 해당 클래스 안에서만 활용 가능
//    println(person.fullName())
//}
//
//class Empty private constructor() {
//    // 컴파일 모듈 내부에서만 가능
//    internal constructor(int: Int): this()
//    // 클래스 혹은 하위 클래스에서만 접근 가능
//    protected constructor(string: String): this()
//    fun showMe() = println("Empty")
//}

/**
 * 4.1.4 내포된 클래스
 */
//class Person(val id: Id, val age: Int) {
//    class Id(val firstName: String, val familyName: String)
//    fun showMe() = println("${id.firstName} ${id.familyName}, $age")
//}
//
//fun main() {
//    val id = Person.Id("John", "Doe")
//    val person = Person(id, 25)
//    person.showMe()
//}


//class Person(private val id: Id, private val age: Int) {
//    class Id(private val firstName: String, private val familyName: String) {
//        fun nameSake(person: Person) = person.id.firstName == firstName
//    }
//    // nested class(내포된 클래스)에 private을 붙이면 감싸는 클래스에서 접근이 불가하다.
//    fun showMe() = println("${id.firstName} ${id.familyName}, $age")
//}
//
//fun main() {
//    val id = Person.Id("John", "Doe")
//    val person = Person(id, 25)
//    person.showMe()
//}

//
//// 내부 클래스는 내포된 클래스와 다르다. 내부 클래스는 inner가 붙는다.
//class Person(val firstName: String, val familyName: String) {
//    inner class Possession(val description: String) {
//        fun showOwner() = println(fullName())
//    }
//    val something = Possession("Nested Something")
//    private fun fullName() = "$firstName $familyName"
//}
//
//// 내부 클래스를 호출할 때는 반드시 외부 클래스의 인스턴스가 있어야 한다.
//fun main() {
//    val person = Person("John", "Doe")
//    val wallet = person.Possession("Wallet")
//    wallet.showOwner()
//
//    val something = person.something
//    something.showOwner()
//}


/**
 * 4.1.5 지역 클래스
 */
//fun main() {
//    class Point(val x:Int, val y: Int) {
//        fun shift(dx: Int, dy: Int): Point = Point(x + dx, y + dy)
//        override fun toString() = "($x, $y)"
//    }
//    val p = Point(10, 10)
//    println(p.shift(-1, 3))
//
//}


fun main() {
    var x = 1

    class Counter {
        fun increment() {
            x++
        }
    }

    Counter().increment()

    println(x)
}