package study.modern_java_in_action.part2.chapter4;

import org.junit.jupiter.api.Test;
import study.modern_java_in_action.model.Dish;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
            -

     */
}
