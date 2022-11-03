package study.modern_java_in_action.part3.chapter9;

import org.junit.jupiter.api.Test;
import study.modern_java_in_action.model.Apple;
import study.modern_java_in_action.model.CaloricLevel;
import study.modern_java_in_action.model.Dish;
import study.modern_java_in_action.part1.chapter3.BufferedReaderProcessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
    void validatorTest() {
        // 직접 구현
        Validator numericValidator1 = new Validator(new IsNumeric());
        boolean b1 = numericValidator1.validate("aaaa");
        Validator lowerCaseValidator1 = new Validator(new IsAllLowerCase());
        boolean b2 = lowerCaseValidator1.validate("bbbb");

        // 람다 사용
        Validator numericValidator2 = new Validator((String s) -> s.matches("[a-z]+"));
        boolean b3 = numericValidator2.validate("aaaa");
        Validator lowerCaseValidator2 = new Validator((String s) -> s.matches("\\d+"));
        boolean b4 = lowerCaseValidator2.validate("bbbb");
    }

    /* 9.2.2 템플릿 메서드
        -
     */

}
