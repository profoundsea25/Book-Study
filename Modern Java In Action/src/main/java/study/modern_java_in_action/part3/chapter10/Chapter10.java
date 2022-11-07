package study.modern_java_in_action.part3.chapter10;

import lombok.Data;
import org.junit.jupiter.api.Test;
import study.modern_java_in_action.part3.chapter10.ex_10_2_2.GroupingBuilder;
import study.modern_java_in_action.part3.chapter10.ex_10_3.Order;
import study.modern_java_in_action.part3.chapter10.ex_10_3.Stock;
import study.modern_java_in_action.part3.chapter10.ex_10_3.Trade;
import study.modern_java_in_action.part3.chapter10.ex_10_3_1.MethodChainingOrderBuilder;
import study.modern_java_in_action.part3.chapter10.ex_10_3_3.LambdaOrderBuilder;
import study.modern_java_in_action.part3.chapter10.ex_10_3_4.MixedBuilder;
import study.modern_java_in_action.part3.chapter10.ex_10_3_5.Tax;
import study.modern_java_in_action.part3.chapter10.ex_10_3_5.TaxCalculator;
import study.modern_java_in_action.part3.chapter10.ex_10_3_5.TaxFunctionCalculator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static study.modern_java_in_action.part3.chapter10.ex_10_3_2.NestedFunctionOrderBuilder.*;

/**
 * Chapter 10. 람다를 이용한 도메인 전용 언어
 */

/* 도메인 전용 언어(DSL)
    - 언어의 주요 목표 : 메시지를 명확하고, 안정적인 방식으로 전달하는 것
        - "프로그램은 사람들이 이해할 수 있도록 작성되어야 하는 것이 중요하며 기기가 실행하는 부분은 부찾겅니 뿐"
        - 무엇보다 의도가 명확하게 전달되어야 한다.
    - 애플리케이션의 핵심 비즈니스를 모델링하는 소프트웨어 영역에서 읽기 쉽고, 이해하기 쉬운 코드는 특히 중요하다.
        - 개발팀과 도메인 전문가가 공유하고 이해할 수 있는 코드는 생산성과 직결되기 때문이다.
        - 도메인 전용 언어(DSL)로 애플리케이션의 비즈니스 로직을 표현함으로 이 문제를 해결할 수 있다.
    - DSL = 특정 도메인을 대상으로 만들어진 특수 프로그래밍 언어
        - 도메인의 많은 특성 용어를 사용
            - maven, ant 등 = 빌드 과정을 표현하는 DSL
            - HTML = 웹페이지의 구조를 정의하도록 특화된 언어
        - 람다를 통해 자바 또한 코드가 간결해지면서 DSL 역할이 가능해지고 있다.
        - 플루언트 스타일(fluent style)은 쉽게 DSL 에 적용할 수 있다.
            - 스트림 API 의 특성인 메서드 체인을 보통 자바의 루프의 복잡한 제어와 비교해 유창함을 의미하는 용어
        - 기본적으로 DSL 을 만들려면 애플리케이션 수준 프로그래머에 어떤 동작이 필요하며 이들을 어떻게 프로그래머에게 제공하는지 고민이 필요하다.
            - 동시에 시스템 수준의 개념으로 인해 불필요한 오염이 발생하지 않도록 해야한다.
        - 내부적 DSL 에서는 유창하게 코드를 구현할 수 있도록 적절하게 클래스와 메서드를 노출하는 과정이 필요하다.
        - 외부 DSL 은 DSL 문법 뿐 아니라 DSL 을 평가하는 파서(parser)도 구현해야 한다.
 */

public class Chapter10 {

    /**
     * 10.1 도메인 전용 언어
     */

    /* DSL
        - DSL은 특정 비즈니스 도메인의 문제를 해결하려고 만든 언어다.
            - 특정 비즈니스 도메인을 인터페이스로 만든 API 라고 생각할 수 있다.
        - DSL은 범용 프로그래밍 언어가 아니다.
            - DSL에서 동작과 용어는 특정 도메인에 국한되므로 다른 문제는 걱정할 필요가 없다.
            - 오직 자신 앞에 높인 문제를 어떻게 해결할지에만 집중할 수 있다.
            - DSL을 이용하면 사용자가 특정 도메인의 복잡성을 더 잘 다룰 수 있다.
            - 저수준 구현 세부 사항 메서드는 클래스의 비공개로 만들어서 저수준 구현 세부 내용은 숨길 수 있다.
            - 그렇게 하면 사용자 친화적인 DSL을 만들 수 있다.
        - DSL을 개발할 때 염두할 점
            - 의사소통의 왕
                - 우리의 코드의 의도가 명확히 전달되어야 한다.
                - 프로그래머가 아닌 사람도 이해할 수 있어야 한다.
                - 코드가 비즈니스 요구사항에 부합하는지 확인할 수 있다.
            - 한 번 코드를 구현하지만 여러 번 읽는다.
                - 가독성은 유지보수의 핵심이다.
                - 항상 우리의 동료가 쉽게 이해할 수 있도록 코드를 구현해야 한다.
     */

    /* 10.1.1 DSL의 장점과 단점
        - DSL의 장점
            - 간결함 : 비즈니스 로직을 간편하게 캡슐화, 반복을 피하고 코드를 간결하게 만든다.
            - 가독성 : 비 도메인 전문가도 코드를 쉽게 이해할 수 있다.
            - 유지보수
            - 높은 수준의 추상화 : DSL은 도메인과 같은 추상화 수준에서 동작하므로 도메인의 문제와 직접적으로 관련되지 않은 세부 사항은 숨긴다.
            - 집중 : 프로그래머가 특정 코드에 집중할 수 있다.
            - 관심사 분리 : 애플리케이션의 인프라구조와 관련된 문제와 독립적으로 비즈니스 관련된 코드에서 집중하기가 용이하다.
        - DSL의 단점
            - DSL 설계의 어려움
            - 개발 비용 : 코드에 DSL을 추가하는 작업은 초기 프로젝트에 많은 비용과 시간이 소모된다.
            - 추가 우회 계층 : 계층을 최대한 작게 만들어 성능 문제를 회피해야 한다.
            - 새로 배워야 하는 언어 : 개별 DSL이 독립적으로 진화할 수 있기 때문
            - 호스팅 언어 한계 : 장황한 프로그래밍 언어를 기반으로 만든 DSL은 성가신 문법의 제약을 받고 읽기 어려워진다. (그래서 람다가 강력한 도구가 된다.)
     */

    /* 10.1.2 JVM에서 이용할 수 있는 다른 DSL 해결책
        - DSL의 카테고리
            - 내부 DSL (= 임베디드 DSL)
                - 순수 자바 코드 같은 기존 호스팅 언어를 기반으로 구현
            - 외부 DSL (= 스탠드어론)
                - 호스팅 언어와는 독립적으로 자체의 문법을 가짐
            - 다중 DSL
                - 외부 DSL과 내부 DSL의 중간 카테고리에 해당하는 DSL
                - ex. 스칼라, 그루비 등 자바가 아니지만 JVM에서 실행되며 더 유연하고 표현력이 강한 언어
        - 내부 DSL
            - (자바 기준) 자바로 구현한 DSL
            - 장점
                - 외부 DSL에 비해 새로운 패턴과 기술을 배워 DSL을 구현하는 노력이 현저히 줄어듦
                - 다른 자바 코드와 함께 DSL을 컴파일할 수 있다.
                - 새 언어를 배우거나 복잡한 외부 도구를 배울 필요가 없다.
                - 추가 DSL을 쉽게 합칠 수 있다.
        - 다중 DSL
            - (자바 기준) 같은 자바 바이트코드를 사용하는 JVM 기반 프로그래밍 언어를 이용하여 DSL 합침 문제를 해결
            - 불편한 점
                - 새로운 언어를 배우거나 팀 내에서 이미 해당 기술을 가지고 있어야 한다.
                - 여러 컴파일러로 소스를 빌드하도록 빌드 과전을 개선해야 한다.
                - 자바와 완벽히 호환되지 않을 때가 있다.
        - 외부 DSL
            - 자신만의 문법과 구문으로 새 언어를 설계해야 한다.
                - 새 언어를 파싱하고, 파서의 결과를 분석하고, 외부 DSL을 실행할 코드를 만들어야 한다.
                - 아주 큰 작업이다.
            - 장점
                - 외부 DSL이 제공하는 무한한 유연성
                    - 우리에게 필요한 특성을 완벽하게 제공하는 언어를 설계할 수 있다.
                    - 자바 인프라구조 코드와 외부 DSL로 구현한 비즈니스 코드를 명확히 분리
                        - 그러나 이는 DSL과 호스트 언어 사이에 인공 계층이 생기므로 양날의 검
     */

    /**
     * 10.2 최신 자바 API의 작은 DSL
     */

    /* 최신 자바 API의 작은 DSL
        - 네이티브 자바 API
            - `Comparator`
            - `Stream`
     */
    @Test
    void javaApiExample() {
        // 사람의 나이를 기준으로 객체를 정렬
        List<Person> persons = new ArrayList<>();
        Collections.sort(persons, new Comparator<Person>() {
            @Override
            public int compare(Person p1, Person p2) {
                return p1.getAge() - p2.getAge();
            }
        });
        //
        Collections.sort(persons, (p1, p2) -> p1.getAge() - p2.getAge());
        //
        Collections.sort(persons, Comparator.comparing(p -> p.getAge()));
        //
        Collections.sort(persons, Comparator.comparing(Person::getAge));
        //
        Collections.sort(persons, Comparator.comparing(Person::getAge).reversed());
        //
        Collections.sort(persons, Comparator.comparing(Person::getAge).thenComparing(Person::getName));
        //
        persons.sort(Comparator.comparing(Person::getAge).thenComparing(Person::getName));
    }

    @Data
    public static class Person {
        private final int age;
        private final String name;
    }


    /* 10.2.1 스트림 API는 컬렉션을 조작하는 DSL

     */
    @Test
    void streamExample() throws Exception {
        String fileName = "text";
        List<String> errors1 = new ArrayList<>();
        int errorCount = 0;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
        //
        String line1 = bufferedReader.readLine();
        while (errorCount < 40 && line1 != null) {
            if (line1.startsWith("ERROR")) {
                errors1.add(line1);
                errorCount++;
            }
            line1 = bufferedReader.readLine();
        }
        //
        List<String> errors2 = Files.lines(Paths.get(fileName))
                .filter(line2 -> line2.startsWith("ERROR"))
                .limit(40)
                .collect(Collectors.toList());
    }

    /* 10.2.2 데이터를 수집하는 DSL인 Collectors

     */
    @Test
    void collectorsExample() {
        List<Car> cars = new ArrayList<>();
        // 자동차를 브랜드별, 색상별로 그룹화
        Map<Brand, Map<Color, List<Car>>> carsByBrandAndColor
                = cars.stream().collect(Collectors.groupingBy(Car::getBrand, Collectors.groupingBy(Car::getColor)));
        // `Collectors`를 중첩함으로 다중 수준 `Collector`를 만든다.
        // 가장 안쪽 그룹화가 처음 평가되고 코드에서는 반대로 가장 나중에 등장한다.
        // 이를 쉽게 만들기 위해 `GroupingBuilder` 사용

        // 그러나, 플루언트 형식 빌더의 문제점 (ex. `Comparator`)
        // 중첩된 그룹화 수준에 반대로 그룹화 함수를 구현해야 한다. 유틸리티 사용 코드가 직관적이지 않다.
        // 자바로는 이런 순서 문제를 해결할 수 없다.
        Collector<? super Car, ?, Map<Brand, Map<Color, List<Car>>>> carGroupingCollector
                = GroupingBuilder.groupOn(Car::getColor).after(Car::getBrand).get();
    }

    @Data
    public static class Car {
        private final Brand brand;
        private final Color color;
    }

    public static class Brand {

    }

    enum Color {

    }

    /**
     * 10.3 자바로 DSL을 만드는 패턴과 기법
     */

    /* 예제 설명
        - DSL은 특정 도메인 모델에 적용할 친화적이고 가독성 높은 API를 제공
        - 예제 도메인 모델
            - 주어진 시장에 주식 가격을 모델링하는 순수 자바 빈즈 (`Stock`)
            - 주어진 가격에서 주어진 양의 주식을 사거나 파는 거래 (`Trade`)
            - 고객이 요청한 한 개 이상의 거래의 주문 (`Order`)
        - 어떤 DSL 형식을 사용할 것인지는 각자의 기호에 달렸다.
            - 자신이 만들려는 도메인 언어에 어떤 도메인 모델이 맞는지 찾으려면 실험을 해야 한다.
            - 두개 이상의 형식을 한 DSL에 조합할 수 있다.
     */
    @Test
    void dslExample() {
        Order order = new Order();
        order.setCustomer("BigBank");

        Trade trade1 = new Trade();
        trade1.setType(Trade.Type.BUY);

        Stock stock1 = new Stock();
        stock1.setSymbol("IBM");
        stock1.setMarket("NYSE");

        trade1.setStock(stock1);
        trade1.setPrice(125.00);
        trade1.setQuantity(80);
        order.addTrade(trade1);

        Trade trade2 = new Trade();
        trade2.setType(Trade.Type.BUY);

        Stock stock2 = new Stock();
        stock2.setSymbol("GOOGLE");
        stock2.setMarket("NASDAQ");

        trade2.setStock(stock2);
        trade2.setPrice(375.00);
        trade2.setQuantity(50);
        order.addTrade(trade2);
    }

    /* 10.3.1 메서드 체인
        - 빌더를 구현한다.
        - 장점
            - 메서드 이름이 키워드 인수 역할을 한다.
            - 선택형 파라미터와 잘 동작한다.
            - 사용자가 미리 지정된 절차에 따라 플루언트 API의 메서드를 호출하도록 강제한다.
                - 덕분에 사용자가 값을 올바르게 설정할 수 있도록 할 수 있다.
            - 파라미터가 빌더 내부로 국한된다는 이점도 제공한다.
            - 정적 메서드 사용을 최소화하고 메서드 이름이 인수의 이름을 대신하도록 만듦으로 이런 형식의 DSL의 가독성을 개선하는 효과를 더한다.
            - 문법적 잡음을 최소화한다.
        - 단점
            - 빌더를 구현해야 한다. (장황하다)
            - 상위 수준의 빌더를 하위 수준의 빌더와 연결할 많은 접착 코드가 필요하다.
            - 도메인의 객체 중첩 구조와 일치하게 들여쓰기를 강제하는 방법이 없다는 것도 단점이다.
     */
    @Test
    void methodChainExample() {
        Order order = MethodChainingOrderBuilder.forCustomer("BigBank")
                .buy(80)
                .stock("IBM")
                .on("NYSE")
                .at(125.00)
                .sell(50)
                .stock("GOOGLE")
                .on("NASDAQ")
                .at(375.00)
                .end();
    }

    /* 10.3.2 중첩된 함수 이용
        - 다른 함수 안에 함수를 이용해 도메인 모델을 만든다.
        - 장점
            - 메서드 체인에 비해 함수의 중첩 방식이 도메인 객체 계층 구조에 그대로 반영된다.
            - 구현의 장황함을 줄일 수 있다.
        - 문제점
            - 더 많은 괄호 사용
            - 인수 목록을 정적 메서드에 넘겨줘야 한다.
            - 선택 사항 필드가 있으면 인수를 생략할 수 있으므로 이 가능성을 처리할 수 있도록 여러 메서드 오버라이드를 구현해야 한다.
            - 인수의 의미가 이름이 아니라 위치에 의해 정의된다.
                - 인수의 역할을 확실하게 만드는 여러 더미 메서드를 이용해 조금은 문제를 완화할 수는 있다.
            - 정적 메서드의 사용이 빈번하다.
     */
    @Test
    void nestedFunctionExample() {
        // NestedFunctionOrderBuilder (static imported)
        Order order = order("BigBank",
                buy(80, stock("IBM", on("NYSE")), at(125.00)),
                sell(50, stock("GOOGLE", on("NASDAQ")), at(375.00))
        );
    }

    /* 10.3.3 람다 표현식을 이용한 함수 시퀀싱
        - 람다 표현식을 받아 실행해 도메인 모델을 만들어 내는 여러 빌더를 구현해야 한다.
        - 메서드 체인 패턴을 이용해 만들려는 객체의 중간 상태를 유지한다.
            - 함수형 인터페이스를 빌더가 인수로 받음으로 DSL 사용자가 람다 표현식으로 인수를 구현할 수 있게 한다.
        - 장점
            - 플루언트 방식으로 정의 가능
            - 중첩 함수 형식처럼 다양한 람다 표현식의 중첩 수준과 비슷하게 도메인 객체의 계층 구조를 유지
            - 선택형 파라미터와 잘 동작한다.
            - 정적 메서드를 최소화하거나 없앨 수 있다.
            - 빌더의 접착 코드가 없다.
        - 문제점
            - 많은 설정 코드가 필요 (구현이 장황)
            - 자바 8 람다 표현식 문법에 의한 잡음의 영향을 받는다.
     */
    @Test
    void lambdaExample() {
        Order order = LambdaOrderBuilder.order(o -> {
            o.forCustomer("BigBank");
            o.buy(t -> {
                t.quantity(80);
                t.price(125.00);
                t.stock(s -> {
                    s.symbol("IBM");
                    s.market("NYSE");
                });
            });
            o.sell(t -> {
                t.quantity(50);
                t.price(375.00);
                t.stock(s -> {
                    s.symbol("GOOGLE");
                    s.market("NASDAQ");
                });
            });
        });
    }

    /* 10.3.4 조합하기
        - 여러 개의 DSL 패턴을 조합할 수 있다.
        - 문제점
            - 한 가지 기법을 적용한 DSL에 비해 사용자가 DSL을 배우는데 오랜 시간이 걸린다.
     */
    @Test
    void mixedExample() {
        // `static import`로 가독성 높일 수 있음
        Order order = MixedBuilder.forCustomer("BigBank",
                MixedBuilder.buy(t -> t.quantity(80).stock("IBM").on("NYSE").at(125.00)),
                MixedBuilder.sell(t -> t.quantity(50).stock("GOOGLE").on("NASDAQ").at(125.00)));
    }

    /* 10.3.5 DSL에 메서드 참조 이용하기
        - 메서드 참조를 이용하면 많은 DSL의 가독성을 높일 수 있다.
     */
    @Test
    void methodReferenceExample() {
        Order order = new Order();

        // `Order`에 세금을 적용한다고 가정하자.

        // 1. 불리언 플래그를 인수로 받는 정적 메서드
        // 가독성의 문제가 있다. 불리언 변수의 올바른 순서를 기억하기 어렵다.
        double value1 = Tax.calculate(order, true, false, true);

        // 2. 플루언트 스타일 적용 (`TaxCalculator`)
        // 코드가 장황해진다.
        // 확장성도 제한적이다.
        double value2 = new TaxCalculator()
                .withTaxRegional()
                .withTaxSurcharge()
                .calculate(order);

        // 3. 자바의 함수형 기능 사용
        // 코드가 쉬워지고 간결해진다.
        // 새로운 세금 함수를 `Tax` 클래스에 추가해도 `TaxFunctionalCalculator`를 바꾸지 않고 바로 사용할 수 있는 유연성도 제공한다.
        double value3 = new TaxFunctionCalculator()
                .with(Tax::regional)
                .with(Tax::surcharge)
                .calculate(order);
    }


    /**
     * 10.4 실생활의 자바 8 DSL
     */

    /* 10.4.1 jOOQ
        - SQL을 구현하는 내부적 DSL로 자바에 직접 내장된 형식 안전 언어
        - 자바 컴파일러가 복잡한 SQL 구문의 형식을 확인할 수 있다.
        - 메서드 체인 패턴 사용
            - 선택적 파라미터를 허용하고 미리 정해진 순서로 특정 메서드가 호출될 수 있게 강제
     */

    /* 10.4.2 큐컴버
        - 동작 주도 개발(Behavior-driven development, BDD)
            - 비즈니스 시나리오를 구조적으로 서술하는 간단한 도메인 전용 스트림팅 언어를 사용
        - 명령문을 실행할 수 있는 테스트 케이스로 변환
        - 스크립트 결과물은 실행할 수 있는 테스트임과 동시에 비즈니스 기능의 수용 기준
        - 우선 순위에 따른, 확인할 수 있는 비즈니스 가치를 전달하는 개발 노력에 집중하며 비즈니스 어휘를 공유함으로
          도메인 전문가와 프로그래머 사이의 간격을 줄인다.
        - Given / When / Then
            = 전제 조건 정의 / 시험하려믄 모데인 객체의 실질 호출 / 테스트 케이스의 결과를 확인하는 어설션
        - 외부적 DSL과 내부적 DSL을 효과적으로 합친 케이스.
     */

    /* 10.4.3 스트링 통합
        - 의존성 주입에 기반한 스프링 프로그래밍 모델을 확장
        - 핵심 목표는 복잡한 엔터프라이즈 통합 솔루션을 구현하는 단순한 모델을 제공하고 비동기, 메시지 주도 아키텍처를 쉽게 적용할 수 있게 돕는 것.
     */

}
