package study.modern_java_in_action.part2.chapter4;

import org.junit.jupiter.api.Test;
import study.modern_java_in_action.model.Dish;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Chapter 4. 스트림 소개
 */

public class Chapter4 {

    /* 컬렉션
        - 거의 모든 자바 애플리케이션은 컬렉션을 만들고 처리하는 과정을 포함한다.
        - 그러나 완벽한 컬렉션 관련 연산을 지원하려면 한참 멀었다.
            - SQL과 같이 선언형으로 표현하여 연산을 표현할 수 없다.
            - 커다란 컬렉션을 처리하는데 있어, 멀티코어 병렬 처리를 구현하기 까다롭다.
     */

    /**
     * 4.1 스트림이란 무엇인가?
     */
    /* 스트림(Streams)
        - 스트림을 이용하면 선언형으로 데이터를 처리할 수 있다.
            - 즉, 데이터를 처리하는 임시 구현 코드 대신 질의(query)로 표현할 수 있다.
            - 쉽게 생각하면, 스트림은 데이터 컬렉션 반복을 멋지게 처리하는 기능이다.
            - 스트림을 이용하면 멀티스레드 코드를 구현하지 않아도 데이터를 투명하게 병렬로 처리할 수 있다.
        - 스트림의 기능이 주는 이득
            - 선언형으로 크드를 구현할 수 있다.
                - 즉, 루프와 if 조건문 등의 제어 블록을 사용해서 어떻게 동작을 구현할지 지정할 필요 없이
                  동작의 수행을 지정할 수 있다.
                - 선언형 코드와 동작 파라미터화를 활용하면 변하는 요구사항에 쉽게 대응이 가능하다.
            - 여러 빌딩 블록 연산을 연결해서 복잡한 데이터 처리 파이프라인을 만들 수 있다.
                - 여러 연산을 파이프라인으로 연결해도 여전히 가독성과 명확성이 유지된다.
            - `filter`, `sorted`, `map`, `collect` 같은 연산은 고수준 빌딩 블록으로 이루어져 있으므로
              특정 스레딩 모델에 제한되지 않고 자유롭게 어떤 상황에서든 사용할 수 있다.
                - 내부적으로 단일 스레드 모델에 사용할 수 있지만 멀티코어 아키텍처를 최대한 투명하게 활용할 수 있게 구현되어 있다.
                - 결과적으로 데이터 처리 과정을 병렬화하면서 스레드와 락을 걱정할 필요가 없다.
        - 특징 정리
            - 선언형 : 더 간결하고 가독성이 좋아진다.
            - 조립할 수 있음 : 유연성이 좋아진다.
            - 병렬화 : 성능이 좋아진다.
     */
    @Test
    void beforeJava8Example() {
        List<Dish> menu = Dish.exampleDishList();
        List<Dish> lowCaloricDishes = new ArrayList<>();
        for (Dish dish : menu) {
            if (dish.getCalories() < 400) {
                lowCaloricDishes.add(dish);
            }
        }
        Collections.sort(lowCaloricDishes, new Comparator<Dish>() {
            @Override
            public int compare(Dish dish1, Dish dish2) {
                return Integer.compare(dish1.getCalories(), dish2.getCalories());
            }
        });
        List<String> lowCaloricDishesName = new ArrayList<>();
        for (Dish dish : lowCaloricDishes) {
            lowCaloricDishesName.add(dish.getName());
        }
    }

    @Test
    void afterJava8Example() {
        List<Dish> menu = Dish.exampleDishList();
        List<String> lowCaloricDishesName = menu
                .stream()
//                .parallelStream() // 병렬 처리를 한다면 stream() 대신에 사용
                .filter(dish -> dish.getCalories() < 400)
                .sorted(Comparator.comparing(Dish::getCalories))
                .map(Dish::getName)
                .collect(Collectors.toList());
    }

    /**
     * 4.2 스트림 시작하기
     */
    /* 스트림이란 정확히 무엇인가?
        - 스트림이란 '데이터 처리 연산을 지원하도록 소스에서 추출된 연속된 요소(Sequence of elements)'로 정의할 수 있다.
            - 연속된 요소
                - 스트림은 특정 요소 형식으로 이루어진 연속된 값 집합의 인터페이스를 제공한다. (컬렉션과 마찬가지다.)
                - filter, sorted, map 처럼 표현 계산식이 주를 이룬다.
            - 소스
                - 스트림은 컬렉션, 배열, I/O 자원 등의 데이터 제공 소스로부터 데이터를 소비한다.
                - 정렬된 컬렉션으로 스트림을 생성하면 정렬이 그대로 유지된다.
            - 데이터 처리 연산
                - 함수형 프로그래밍 언어에서 일반적으로 지원하는 연산과 데이터베이스와 비슷한 연산을 지원한다.
                - 순차적 또는 병렬로 실행할 수 있다.
        - 스트림의 중요한 2가지 특징
            - 파이프라이닝
                - 스트림 연산끼리 연결해서 커다란 파이프 라인을 구성할 수 있도록 스트림 자신을 반환한다.
                - SQL 과 비슷하다.
            - 내부 반복
     */
    @Test
    void streamExample() {
        List<Dish> menu = Dish.exampleDishList();
        List<String> threeHighCaloricDishNames = menu.stream()
                .filter(dish -> dish.getCalories() > 300)
                .map(Dish::getName)
                .limit(3)
                .collect(Collectors.toList());
        System.out.println(threeHighCaloricDishNames);
    }

    /* 스트림 대표 메서드
        - filter : 람다를 인수로 받아 스트림에서 특정 요소를 제외시킨다.
        - map : 람다를 이용해서 한 요소를 다른 요소로 변환하거나 정보를 추출한다.
        - limit : 정해진 개수 이상의 요소가 스트림에 저장되지 못하게 스트림 크기를 축소한다.
        - collect : 스트림을 다른 형식으로 변환한다.
     */


    /**
     * 4.3 스트림과 컬렉션
     */

    /* 스트림과 컬렉션
        - 컬렉션과 스트림 모두 연속된 요소 형식의 값을 저장하는 자료구조의 인터페이스를 제공한다.
            - '연속된'이란 순서와 상관없이 아무 값에서 접속하는 것이 아니라 순차적으로 값에 접근한다는 것을 의미한다.
        - 데이터를 언제 계산하느냐가 컬렉션과 스트림의 가장 큰 차이다.
            - 컬렉션은 현재 자료구조가 포함하는 모든 값을 메모리에 저장하는 자료구조다.
                - 즉, 모든 요소는 컬렉션에 추가하기 전에 계산되어야 한다.
                - 컬렉션에 요소를 추가하거나 삭제할 수 있다. 이런 연산을 수행할 때마다 컬렉션의 모든 요소를 메모리에 저장해야 한다.
                - 컬렉션에 추가하려는 요소는 미리 계산되어야 한다.
                - 적극적으로 생성된다. (팔기도 전에 창고를 가득 채움)
            - 스트림은 이론적으로 요청할 때만 요소를 계산하는 고정된 자료구조다.
                - 스트림에서 요소를 추가하거나 스트림에서 요소를 제거할 수 없다.
                - 사용자가 요청하는 값만 스트림에서 추출한다.
                - 게으르게 만들어진다.
     */

    /* 4.3.1 딱 한 번만 탐색할 수 있다
        - 반복자와 마찬가지로 스트림도 한 번만 탐색할 수 있다.
            - 즉, 탐색된 스트림의 요소는 소비된다.
            - 한 번 탐색한 요소를 다시 탐색하려면 초기 데이터 소스에서 새로운 스트림을 만들어야 한다.
     */
    @Test
    void streamConsumeTest() {
        List<String> title = Arrays.asList("Java8", "In", "Action");
        Stream<String> s = title.stream();
        s.forEach(System.out::println); // 출력
        s.forEach(System.out::println); // 스트림이 이미 소비되었으므로(닫혔으므로) Exception 발생
    }

    /* 4.3.2 외부 반복과 내부 반복
        - 컬렉션에선 사용자가 직접 요소를 반복해야 한다. 이를 외부 반복이라 한다.
            - 명시적으로 컬렉션 항목을 하나씩 가져와서 처리한다.
            - 병렬성을 스스로 관리해야 한다.
        - 스트림에서는 반복을 알아서 처리하고 결과 스트림값을 어딘가에 저장해주는 내부 반복을 사용한다.
            - 함수에 어떤 작업을 수행할지만 지정하면 모든 것이 알아서 처리된다.
            - 외부 반복보다 더 쉽게 작업을 투명하게 병렬로 처리하거나 더 최적화된 다양한 순서로 처리할 수 있다.
            - 데이터 표현과 하드웨어를 활용한 병렬성 구현을 자동으로 선택한다.
     */
    @Test
    void iterationExample() {
        List<Dish> menu = Dish.exampleDishList();

        // 외부 반복 - for-each 구문
        List<String> names1 = new ArrayList<>();
        for (Dish dish : menu) {
            names1.add(dish.getName());
        }

        // 외부 반복 - Iterator 객체 이용 (명시적 반복)
        List<String> names2 = new ArrayList<>();
        Iterator<Dish> iterator = menu.iterator();
        while (iterator.hasNext()) {
            Dish dish = iterator.next();
            names2.add(dish.getName());
        }

        // 내부 반복 - Stream
        List<String> names3 = menu.stream()
                .map(Dish::getName)
                .collect(Collectors.toList());
    }

    @Test
    void quiz_4_1() {
        List<Dish> menu = Dish.exampleDishList();

        // 문제
        List<String> highCaloricDishes = new ArrayList<>();
        Iterator<Dish> iterator = menu.iterator();
        while (iterator.hasNext()) {
            Dish dish = iterator.next();
            if (dish.getCalories() > 300) {
                highCaloricDishes.add(dish.getName());
            }
        }

        // 답
        List<String> highCaloricDishesByStream = menu.stream()
                .filter(dish -> dish.getCalories() > 300)
                .map(Dish::getName)
                .collect(Collectors.toList());
    }


    /**
     * 4.4 스트림 연산
     */

    /* 연산의 종류
        - 중간 연산(intermediate operation) : 연결할 수 있는 스트림 연산
        - 최종 연산(terminal operation) : 스트림을 닫는 연산
    */

    /* 4.4.1 중간 연산
        - 중간 연산은 다른 스트림을 반환한다.
            - 따라서 중간 연산을 연결해서 질의를 만들 수 있다.
        - 중간 연산의 중요한 특징은 단말 연산을 스트림 파이프라인에 실행하기 전까지는 아무 연산도 수행하지 않는다는 것이다.
            - 즉, 게으르다.(lazy)
            - 중간 연산을 합친 다음에 합쳐진 중간 연산을 최종 연산으로 한 번에 처리하기 때문이다.
        - 게으르기 때문에 얻는 최적화 효과들
            - 쇼트서킷 -> 추후 5장에서 설명
            - 루프 퓨전(loop fusion) : 다른 연산이 한 과정으로 병합되는 것
        - `filter`, `map`, `limit`, `sorted`, `distinct` ...
     */

    @Test
    void intermediateOperationTest() {
        List<Dish> menu = Dish.exampleDishList();

        List<String> names = menu.stream()
                .filter(dish -> {
                    System.out.println("filtering:" + dish.getName());
                    return dish.getCalories() > 300;
                })
                .map(dish -> {
                    System.out.println("mapping:" + dish.getName());
                    return dish.getName();
                })
                .limit(3)
                .collect(Collectors.toList());
        System.out.println(names);
    }

    /* 4.4.2 최종 연산
        - 최종 연산은 스트림 파이프라인에서 결과를 도출한다.
     */

    /* 4.4.3 스트림 이용하기
        - 스트림 이용 과정
            - 1. 질의를 수행할 (컬렉션 같은) 데이터 소스
            - 2. 스트림 파이프라인을 구성할 중간 연산 연결
            - 3. 스트림 파이프라인을 실행하고 결과를 만들 최종 연산
        - 스트림 파이프라인의 개념은 빌더 패턴과 비슷하다.
        - `forEach`, `count`, `collect` ...
     */
}