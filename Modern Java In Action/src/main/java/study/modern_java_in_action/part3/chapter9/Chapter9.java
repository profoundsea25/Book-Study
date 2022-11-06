package study.modern_java_in_action.part3.chapter9;

import org.junit.jupiter.api.Test;
import study.modern_java_in_action.model.Apple;
import study.modern_java_in_action.model.CaloricLevel;
import study.modern_java_in_action.model.Dish;
import study.modern_java_in_action.part1.chapter2.Chapter2;
import study.modern_java_in_action.part1.chapter3.BufferedReaderProcessor;
import study.modern_java_in_action.part3.chapter9.ex_9_2_1.IsAllLowerCase;
import study.modern_java_in_action.part3.chapter9.ex_9_2_1.IsNumeric;
import study.modern_java_in_action.part3.chapter9.ex_9_2_1.Validator;
import study.modern_java_in_action.part3.chapter9.ex_9_2_2.Customer;
import study.modern_java_in_action.part3.chapter9.ex_9_2_2.OnlineBankingLambda;
import study.modern_java_in_action.part3.chapter9.ex_9_2_3.Feed;
import study.modern_java_in_action.part3.chapter9.ex_9_2_3.Guardian;
import study.modern_java_in_action.part3.chapter9.ex_9_2_3.LeMonde;
import study.modern_java_in_action.part3.chapter9.ex_9_2_3.NYTimes;
import study.modern_java_in_action.part3.chapter9.ex_9_2_4.HeaderTextProcessing;
import study.modern_java_in_action.part3.chapter9.ex_9_2_4.ProcessingObject;
import study.modern_java_in_action.part3.chapter9.ex_9_2_4.SpellCheckerProcessing;
import study.modern_java_in_action.part3.chapter9.ex_9_2_5.*;
import study.modern_java_in_action.part3.chapter9.ex_9_3.Point;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Chapter 9. 리팩터링, 테스팅, 디버깅
 */

public class Chapter9 {

    /**
     * 9.1 가독성과 유연성을 개선하는 리팩터링
     */

    /* 복습
        - 람다 : 익명 클래스보다 코드를 좀 더 간결하게 만든다.
        - 메서드 참조 : 인수로 전달하려는 메서드가 이미 있을 때는 람다보다 더 간결한 코드를 구현할 수 있다.
     */

    /* 9.1.1 코드 가독성 개선
        - 코드 가독성이란?
            - "어떤 코드를 다른 사람도 쉽게 이해할 수 있음"
            - 코드 가독성을 개선한다는 것은 우리가 구현한 코드를 다른 사람이 쉽게 이해하고 유지보수할 수 있게 만드는 것을 의미
            - 코드의 문서화를 잘하고, 표준 코딩 규칙을 준수하는 등의 노력이 필요하다.
        - 코드 가독성을 개선하는 간단한 3가지 예제
            - 익명 클래스를 람다 표현식으로 리팩터링하기
            - 람다 표현식을 메서드 참조로 리팩터링하기
            - 명령형 데이터 처리를 스트림으로 리팩터링하기
     */

    /* 9.1.2 익명 클래스를 람다 표현식으로 리팩터링하기
        - 익명 클래스를 람다로 바꿀 때 주의할 점
            - 익명 클래스에서 사용한 `this`와 `super`는 람다 표현식에서 다른 의미를 갖는다.
                - 익명 클래스 `this` = 익명 클래스 자기 자신
                - 람다 `this` = 람다를 감싸는 클래스
            - 익명 클래스는 감싸고 있는 클래스의 변수를 가릴 수 있다. (섀도우 변수, shadow variable)
                - 람다 표현식으로는 변수를 가릴 수 없다.
            - 익명 클래스를 람다 표현식으로 바꾸면 콘텍스트 오버로딩에 따른 모호함이 초래될 수 있다.
                - 익명 클래스는 인스턴스화할 때 명시적으로 형식이 정해진다.
                - 람다 형식은 콘텍스트에 따라 달라진다.
     */
    @Test
    void anonymousClassToLambdaExample() {
        // 익명 함수
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello");
            }
        };

        // 람다
        Runnable r2 = () -> System.out.println("Hello");
    }

    @Test
    void shadowVariableExample() {
        int a = 10;
        Runnable r1 = () -> {
//            int a = 2; // 컴파일 할 수 없다.
            System.out.println(a);
        };

        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                int a = 2;
                System.out.println(a);
            }
        };
    }

    interface Task {
        public void execute();
    }

    public static void doSomething(Runnable r) {
        r.run();
    }

    public static void doSomething(Task a) {
        a.execute();
    }

    @Test
    void lambdaContextExample() {
        // 익명 클래스는 타입을 명시하여 모호함이 없다.
        doSomething(new Task() {
            @Override
            public void execute() {
                System.out.println("Danger danger!!");
            }
        });

        // 람다는 `Task`를 호출할지 `Runnable`을 호출할지 모호하다.
//        doSomething(() -> System.out.println("Danger danger!!"));
        // 따라서 명시적으로 형변환을 작성해 모호함을 제거할 수 있다.
        doSomething((Task) () -> System.out.println("Danger danger!!"));
    }

    /* 9.1.3 람다 표현식을 메서드 참조로 리팩터링하기
        - 람다 표현식 대신 메서드 참조를 이용하면 가독성을 높일 수 있다.
            - `Collectors.groupingBy`
            - `Comparator.comparing`
            - `Collectors.maxBy`
            - `Collectors.summingInt`
     */
    @Test
    void methodReferenceExample() {
         /*`Collectors.groupingBy` 예제*/
        List<Dish> menu = Dish.exampleDishList();

        // 람다
        Map<CaloricLevel, List<Dish>> dishesByCaloricLevelLambda = menu.stream()
                .collect(
                        Collectors.groupingBy(dish -> {
                            if (dish.getCalories() <= 400) {
                                return CaloricLevel.DIET;
                            } else if (dish.getCalories() <= 700) {
                                return CaloricLevel.NORMAL;
                            } else {
                                return CaloricLevel.FAT;
                            }
                        })
                );

        // 메서드 참조
        Map<CaloricLevel, List<Dish>> dishesByCaloricLevelMethodReference = menu.stream()
                .collect(Collectors.groupingBy(Dish::getCaloricLevel));


        /*`Comparator.comparing` 예제*/
        List<Apple> inventory = Apple.exampleAppleList();
        // 람다
        inventory.sort(
                (a1, a2) -> a1.getWeight().compareTo(a2.getWeight())
        );

        // 메서드 참조
        inventory.sort(Comparator.comparing(Apple::getWeight));


        /*`Collectors.summingInt` 예제*/
        int totalCaloriesLambda = menu.stream().map(Dish::getCalories)
                .reduce(0, (c1, c2) -> c1 + c2);
        int totalCaloriesMethodReference = menu.stream().collect(Collectors.summingInt(Dish::getCalories));
//        int totalCaloriesMethodReference = menu.stream().mapToInt(Dish::getCalories).sum();
    }

    /* 9.1.4 명령형 데이터 처리를 스트림으로 리팩터링하기
        - 이론적으로는 반복자를 이용한 기존의 모든 컬렉션 처리 코드를 스트림 API로 바꿔야 한다.
            - 스트림 API는 데이터 처리 파이프라인의 의도를 더 명확하게 보여준다.
            - 쇼트서킷과 게으름이라는 최적화와 멀티코어 아키텍처를 활용할 수 있는 지름길을 제공한다.
     */
    @Test
    void streamExample() {
        List<Dish> menu = Dish.exampleDishList();

        // 기존 반복문
        List<String> dishNames1 = new ArrayList<>();
        for (Dish dish : menu) {
            if (dish.getCalories() > 300) {
                dishNames1.add(dish.getName());
            }
        }

        // 스트림 활용
        List<String> dishNames2 = menu.parallelStream()
                .filter(d -> d.getCalories() > 300)
                .map(Dish::getName)
                .collect(Collectors.toList());
    }

    /* 9.1.5 코드 유연성 개선
        - 람다를 통해 동작 파라미터화를 쉽게 구현이 가능하다. 람다 전달을 통해 다양한 동작을 표현할 수 있다.
            - 람다 사용을 위해 함수형 인터페이스가 필요하다.
        - 조건부 연기 실행(conditional deferred execution) 패턴
            - 클라이언트 코드에서 객체 상태를 자주 확인하거나, 객체의 일부 메서드를 호출하는 상황이라면
              내부적으로 객체의 상태를 확인한 다음에 메서드를 호출(람다나 메서드 참조를 인수로 사용)하도록
              새로운 메서드를 구현하는 것이 좋다.
                - 그러면 코드 가독성이 좋아질 뿐 아니라 캡슐화도 강화된다. (객체 상태가 클라이언트 코드로 노출되지 않는다.)
        - 실행 어라운드(execute around) 패턴
            - 매번 같은 준비, 종료 과정을 반복적으로 수행하는 코드가 있다면 이를 람다로 변환할 수 있다.
                - 준비, 종료 과정을 처리하는 로직을 재사용함으로써 코드 중복을 줄일 수 있다.
     */

    // 조건부 연기 실행(conditional deferred execution) 패턴 예시
    @Test
    void conditionalDeferredExecutionExample() {
        /* 문제점
            - logger 의 상태가 isLoggable 이라는 메서드에 의해 클라이언트 코드로 노출된다.
            - 메시지를 로깅할 때마다 logger 객체의 상태를 매번 확인해야 할까? 이들은 코드를 어지럽힐 뿐이다.
         */
        Logger logger = Logger.getAnonymousLogger();
        if (logger.isLoggable(Level.FINER)) {
            logger.finer("Problem: " + generateDiagnostic());
        }

        /* 해결책 1
            - if 문 제거, logger 의 상태를 노출할 필요도 없어진다.
            - 그러나 인수로 전달된 메시지 수준에서 logger 가 활성화되어 있지 않더라고 항상 로깅 메시지를 평가하게 된다.
         */
        logger.log(Level.FINER, "Problem: " + generateDiagnostic());

        /* 해결책 2 : 람다사용
            - 특정 조건에서만 메시지가 생성될 수 있도록 메시지 생성 과정을 연기
         */
        logger.log(Level.FINER, () -> "Problem: " + generateDiagnostic());
    }

    public String generateDiagnostic() {
        return "diagnostic example";
    }

    // 실행 어라운드(execute around) 패턴 예시
    @Test
    void executeAroundExample() throws IOException {
        String oneLine = processFile((BufferedReader b) -> b.readLine());
        String twoLine = processFile((BufferedReader b) -> b.readLine() + b.readLine());
    }

    public static String processFile(BufferedReaderProcessor p) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            return p.process(br);
        }
    }


    /**
     * 9.2 람다로 객체지향 디자인 패턴 리팩터링하기
     */

    /*
        - 다자인 패턴은 다양한 패턴을 유형별로 정리한 것이다.
            - 공통적으로 소프트웨어 문제를 설계할 때 재사용할 수 있는 검증된 청사진을 제공한다.
        - 디자인 패턴에 람다 표현식이 더해지면 색다른 기능을 발휘할 수 있다.
            - 즉 람다를 이용하면 이전에 디자인 패턴으로 해결하던 문제를 더 쉽고 간단하게 해결할 수 있다.
        - 또한 람다 표현식으로 기존의 많은 객체지향 디자인 패턴을 제거하거나 간결하게 재구현할 수 있다.
     */

    /* 9.2.1 전략
        - `Validator` 예제
        - 전략 패턴은 한 유형의 알고리즘을 보유한 상태에서 런타임에 적절한 알고리즘을 선택하는 기법이다.
            - 다양한 기준을 갖는 입력값을 검증하거나
            - 다양한 파싱 방법을 사용하거나
            - 입력 형식을 설정하는 등
        - 전략 패턴의 구성
            - 알고리즘을 나타내는 인터페이스
            - 다양한 알고리즘을 나타내는 한 개 이상의 인터페이스 구현 클래스들
            - 전략 객체를 사용하는 한 개 이상의 클라이언트
     */

    // 전략 패턴 예시
    // 오직 소문자 또는 숫자로 이루어져야 하는 등 텍스트 입력이 다양한 조건에 맞게 포맷 되어 있는지 검증
    @Test
    void strategyPatternTest() {
        // 직접 구현
        Validator numericValidator1 = new Validator(new IsNumeric());
        boolean b1 = numericValidator1.validate("aaaa");
        Validator lowerCaseValidator1 = new Validator(new IsAllLowerCase());
        boolean b2 = lowerCaseValidator1.validate("bbbb");

        // 람다 표현식 사용
        Validator numericValidator2 = new Validator((String s) -> s.matches("[a-z]+"));
        boolean b3 = numericValidator2.validate("aaaa");
        Validator lowerCaseValidator2 = new Validator((String s) -> s.matches("\\d+"));
        boolean b4 = lowerCaseValidator2.validate("bbbb");
    }

    /* 9.2.2 템플릿 메서드
        - `OnlineBanking` 예제
        - 알고리즘의 개요를 제시한 다음에 알고리즘의 일부를 고칠 수 있는 유연함을 제공해야 할 때 템플릿 메서드 디자인 패턴을 사용
            - "이 알고리즘을 사용하고 싶은데 그대로는 안 되고 조금 고처야 하는" 상황에 적합
     */
    @Test
    void templateMethodPatternTest() {
        // 람다 표현식 사용
        new OnlineBankingLambda().processCustomer(1337,
                (Customer c) -> System.out.println("Hello" + c.getName()));
    }

    /* 9.2.3 옵저버
        - `Observer`, `Subject` 예제
        - 어떤 이벤트가 발생했을 때 한 객체(주제 subject)가 다른 객체 리스트(옵저버 observer)에 자동으로 알림을 보내야 하는 상황에 사용
            - GUI 애플리캐이션, 주식의 가격 변동에 대응하는 다수의 거래자 예제 등
        - 옵저버 패턴으로 트위터 같은 커스터마이즈된 알림 시스템을 설계하고 구현할 수 있다.
     */
    @Test
    void observerPatternTest() {
        // 람다를 사용하지 않을 경우 = 클래스 생성
        Feed f = new Feed();
        f.registerObserver(new NYTimes());
        f.registerObserver(new Guardian());
        f.registerObserver(new LeMonde());
        f.notifyObservers("The queen said her favourite book is Moder Java in Action!");

        // 람다 표현식 사용
        // 그러나 옵저버가 상태를 가지며, 여러 메서드를 정의하는 등 복잡하다면 람다 표현식보다 기존의 클래스 구현방식이 더 바람직할 수 있다.
        f.registerObserver((String tweet) -> {
            if (tweet != null && tweet.contains("money")) {
                System.out.println("Breaking news in NY! " + tweet);
            }
        });
        f.registerObserver((String tweet) -> {
            if (tweet != null && tweet.contains("queen")) {
                System.out.println("Yet more news from London... " + tweet);
            }
        });
    }

    /* 9.2.4 의무 체인
        - `ProcessingObject` 예제
        - 작업 처리 객체의 체인(동작 체인 등)을 만들 때는 의무 체인 패턴을 사용한다.
            - 한 객체가 어떤 작업을 처리한 다음에 다른 객체로 결과를 전달하고,
              다른 객체도 해야 할 작업을 처리한 다음에 또 다른 객체로 전달하는 식이다.
            - 일반적으로 다음으로 처리할 객체 정보를 유지하는 필드를 포함하는 작업 처리 추상 클래스로 의무 체인 패턴을 구성한다.
            - 작업 처리 객체가 자신의 작업을 끝냈으면 다음 처리 객체로 결과를 전달한다.
     */
    @Test
    void chainPatternTest() {
        // 람다 사용하지 않을 경우
        ProcessingObject<String> p1 = new HeaderTextProcessing();
        ProcessingObject<String> p2 = new SpellCheckerProcessing();
        p1.setSuccessor(p2);
        String result1 = p1.handle("Aren't labdas really sexy?!!");
        System.out.println(result1);

        // 람다 표현식 사용
        UnaryOperator<String> headerProcessing = (String text) -> "From Raoul, Mario and Alan: " + text;
        UnaryOperator<String> spellCheckerProcessing = (String text) -> text.replaceAll("labda", "lambda");
        Function<String, String> pipeline = headerProcessing.andThen(spellCheckerProcessing);
        String result2 = pipeline.apply("Aren't labdas really sexy?!!");
        System.out.println(result2);
    }

    /* 9.2.5 팩토리
        - `ProductFactory` 예제
        - 인스턴스화 로직을 클라이언트에 노출하지 않고 객체를 만들 때 팩토리 디자인 패턴을 사용한다.
            - 은행에서 취급하는 대출, 채권, 주식 등 다양한 상품을 만들어야 하는 경우
     */
    @Test
    void factoryPatterTest() {
        Product p1 = ProductFactory.createProduct("loan");
        Product p2 = ProductFactoryLambda.createProduct("loan");
    }


    /**
     * 9.3 람다 테스팅
     */

    /* 람다 테스팅
        - 개발자의 최종 업무 목표는 제대로 작동하는 코드를 구현하는 것이지, 깔끔한 코드를 구현하는 것이 아니다.
        - 일반적으로 좋은 소프트웨어 공학자라면 프로그램이 의도대로 동작하는지 확인할 수 있는 단위 테스팅(unit testing)을 진행한다.
     */
    @Test
    void testMoveRightBy() {
        Point p1 = new Point(5, 5);
        Point p2 = p1.moveRightBy(10);
        assertEquals(15, p2.getX());
        assertEquals(5, p2.getY());
    }

    /* 9.3.1 보이는 람다 표현식의 동작 테스팅
        - 람다는 익명(익명 함수)이므로 테스트 코드 이름을 호출할 수 없다.
        - 따라서 필요하다면 람다를 필드에 저장해서 재사용할 수 있으며 람다의 로직을 테스트할 수 있다.
            - 메서드를 호출하는 것처럼 람다를 사용할 수 있다.
        - 람다 표현식은 함수형 인터페이스의 인스턴스를 생성한다는 사실을 기억하자.
            - 따라서 생성된 인스턴스의 동작으로 람다 표현식을 테스트할 수 있다.
     */
    @Test
    void testComparingTwoPoints() {
        Point p1 = new Point(10, 15);
        Point p2 = new Point(10, 20);
        int result = Point.compareByXAndThenY.compare(p1, p2);
        assertTrue(result < 0);
    }

    /* 9.3.2 람다를 사용하는 메서드의 동작에 집중하라
        - 람다의 목표는 정해진 동작을 다른 메서드에서 사용할 수 있도록 하나의 조각으로 캡슐화하는 것이다.
            - 그러려면 세부 구현을 포함하는 람다 표현식을 공개하지 말아야 한다.
            - 람다 표현식을 사용하는 메서드의 동작을 테스트함으로써 람다를 공개하지 않으면서도 람다 표현식을 검증할 수 있다.
     */
    @Test
    void testMoveAllPointsRightBy() {
        List<Point> points = Arrays.asList(new Point(5, 5), new Point(10, 5));
        List<Point> expectedPoints = Arrays.asList(new Point(15, 5), new Point(20, 5));
        List<Point> newPoints = Point.moveAllPointsRightBy(points, 10);
        assertEquals(expectedPoints, newPoints);
    }

    /* 9.3.3 복잡한 람다를 개별 메서드로 분할하기
        - 람다 표현식을 테스트 코드에서 참조할 수 없다. 이때 복잡한 람다 표현식을 어떻게 테스트할 것인가?
            - 한 가지 해결책은 람다 표현식을 메서드 참조로 바꾸는 것이다. (새로운 일반 메서드 선언)
     */

    /* 9.3.4 고차원 함수 테스팅
        - 함수를 인수로 받거나 다른 함수를 반환하는 메서드(고차원 함수)는 좀 더 사용하기 어렵다.
            - 메서드가 람다를 인수로 받는다면 다른 람다로 메서드의 동작을 테스트할 수 있다.
        - 테스트해야 할 메서드가 다른 함수를 반환하다면, 함수형 인터페이스의 인스턴스로 간주하고 함수의 동작을 테스트할 수 있다.
     */
    @Test
    void testFilter() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4);
        List<Integer> even = Chapter2.filter(numbers, i -> i % 2 == 0);
        List<Integer> smallerThanThree = Chapter2.filter(numbers, i -> i < 3);
        assertEquals(Arrays.asList(2, 4), even);
        assertEquals(Arrays.asList(1, 2), smallerThanThree);
    }


    /**
     * 9.4 디버깅
     */

    /* 디버깅
        - 문제가 발생한 코드를 디버깅할 때는 두 가지를 가장 먼저 확인해야 한다.
            - 스택 트레이스
            - 로깅
        - 하지만 람다 표현식과 스트림은 기존 디버깅 기법을 무력화한다.
     */

    /* 9.4.1 스택 트레이스 확인
        - 스택 프레임
            - 예외 발생으로 프로그램 실행이 갑자기 중단되었다면 먼저 어디에서 멈췄고 어떻게 멈추게 되었는지를 살펴야 한다.
            - 프로그램이 메서드를 호출할 때마다 프로그램에서 호출 위치, 호출할 때의 인수값, 호출된 메서드의 지역 변수 등을 포함한
              호출 정보가 생성되어 스택 프레임에 저장된다.
        - 람다와 스택 트레이스
            - 람다 표현식은 이름이 없기 때문에 조금 복잡한 스택 트레이스가 생성된다.
            - 람다 표현식은 이름이 없으므로 컴파일러가 람다를 참조하는 이름을 만들어낸다.
            - 메서드 참졸르 사용해도 스택 트레이스에는 메서드명이 나타나지 않는다.
                - 다만 메서드 참조를 사용하는 클래스와 같은 곳에 선언되어 있는 메서드를 참조할 때는 메서드 참조 이름이 스택 트레이스에 나타난다.
            - 즉, 람다표현식과 관련한 스택 트레이스는 이해하기 어려울 수 있다.
     */

    /* 9.4.2 정보 로깅
        - 스트림은 호출하는 순간 소비되어, 파이프라인 중간에 어떤 값들이 만들어지는지 알 수 없다.
            - 이 때 `peek` 연산을 활용할 수 있다.
        - `peek`은 스트림의 각 요소를 소비한 것처럼 동작을 실행한다. 하지만 실제로 스트림의 요소를 소비하지 않는다.
            - `peek`은 자신이 확인한 요소를 파이프라인의 다음 연산으로 그대로 전달한다.

     */
    @Test
    void infoLoggingExample() {
        List<Integer> numbers = Arrays.asList(2, 3, 4, 5);
        numbers.stream()
                .map(x -> x + 17)
                .filter(x -> x % 2 == 0)
                .limit(3)
                .forEach(System.out::println);

        // 위의 예시에서 map, filter, limit 이 어떤 결과를 도출하는지 알고 싶다면 peek 을 사용
        List<Integer> result = numbers.stream()
                .peek(x -> System.out.println("from stream: " + x))
                .map(x -> x + 17)
                .peek(x -> System.out.println("after map: " + x))
                .filter(x -> x % 2 == 0)
                .peek(x -> System.out.println("after filter: " + x))
                .limit(3)
                .peek(x -> System.out.println("after limit: " + x))
                .collect(Collectors.toList());
    }

}
