package study.modern_java_in_action.part2.chapter4;

import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Chapter 7. 병렬 데이터 처리와 성능
 */

/* 스트림 API를 통해
    - 내부 박복을 통해 네이티브 자바 라이브러리가 스트림 요소의 처리를 제어
    - 자바 개발자는 컬렉션 데이터 처리 속도를 높이려고 따로 고민할 필요가 없어짐
    - 컴퓨터의 멀티코를 활용해서 파이프라인 연산을 실행할 수 있다.
    - 자바 7 이전에는 데이터 컬렉션을 병렬로 처리하기 어려웠다.
        - 데이터를 서브파트로 분할한다.
        - 분할된 서브파트를 각각의 스레드로 할당한다.
        - 스레드 할당 이후 의도치 않은 경쟁 상태(race condition)이 발생하지 않도록 적절한 동기화를 추가한다.
        - 부분 결과를 합쳐준다.
    - 자바 7부터 포크/조인 프레임워크 기능을 제공하여 더 쉽게 병렬화를 수행하면서 에러를 최소화할 수 있게 해준다.
    - 스트림을 통해 순차 스트림을 병렬 스트림으로 자연스럽게 바꿀 수 있다.
 */

public class Chapter7 {

    /**
     * 7.1 병렬 스트림
     */

    /* 병렬 스트림 (parallel stream)
        - 병렬 스트림이란 각각의 스레드에서 처리할 수 있도록 스트림 요소를 여러 청크로 분할한 스트림이다.
            - 이를 통해 모든 멀티코어 프로세서가 각각의 청크를 처리하도록 할당할 수 있다.
     */
    public long sequentialSum_1(long n) {
        return Stream.iterate(1L, i -> i + 1)
                .limit(n)
                .reduce(0L, Long::sum);
    }

    public long iterativeSum(long n) {
        long result = 0;
        for (long i = 1L; i <= n; i++) {
            result += i;
        }
        return result;
    }

    /* 7.1.1 순차 스트림을 병렬 스트림으로 변환하기
        - 순차 스트림에 `parallel`메서드를 호출하면 기존의 함수형 리듀싱 연산(숫자 합계 연산)이 병렬로 처리된다.
            - 스트림이 여러 청크로 분할되는 차이점이 있다.
            - `parallel` 자체는 병렬로 수행해야 함을 의미하는 boolean 플래그가 설정되는 것 뿐이다.
        - 반대로 `sequential`로 병렬 스트림을 순차 스트림으로 바꿀 수 있다.
        - `parallel`과 `sequential` 중 최종적으로 호출된 메서드가 전체 파이프라인에 영향을 미친다.
        - 병렬 스트림은 내부적으로 `ForkJoinPool`을 사용한다.
            - 기본적으로 ForkJoinPool 은 프로세서 수가 반환하는 값에 상응하는 스레드를 갖는다.
                - `Runtime.getRuntime().availableProcessors();`
            - 현재는 하나의 병렬 스트림에 사용할 수 있는 특정한 값을 지정할 수 없다. 전역 설정이 강제된다.
                - `System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "12");`
            - 일반적으로 기기의 프로세서 수와 같으므로 이유가 없다면 ForkJoinPool 의 기본값을 그대로 사용하는 것을 권장.
     */
    public long parallelSum(long n) {
        return Stream.iterate(1L, i -> i + 1)
                .limit(n)
                .parallel()
                .reduce(0L, Long::sum);
    }

    /* 7.1.2 스트림 성능 측정
        - 소프트웨어 공학에서 추측은 위험한 방법이다.
        - 성능 최적화 황금 규칙 3가지
            - 1. 측정
            - 2. 측정
            - 3. 측정
        - 벤치마크 구현; 자바 마이크로벤치마크 하니스(JMH)
            - JVM 으로 실행되는 프로그램을 벤치마크하는 것은 쉽지 않다.
        - 병렬 프로그래밍은 까다롭고 때로는 이해하기 어려운 함정이 숨어 있다.
            - 심지어 병렬 프로그래밍을 오용하면 오히려 전체 프로그램의 성능이 더 나빠질 수 있다.
            - 따라서 `parallel` 메서드가 내부적으로 어떤 일이 일어나는지 이해해야 한다.
        - 자세한 내용은 아래 `ParallelStreamBenchmark` 클래스 참고
        - 다만, 병렬화가 완전 공짜는 아니다.
            - 병렬화를 이용하려면 스트림을 재귀적으로 분할해야 한다.
            - 각 서브스트림을 서로 다른 스레드의 리듀싱 연산으로 할당해야 한다.
            - 이들 결과를 하나의 값으로 합쳐야 한다.
        - 멀티코어 간의 데이터 이동은 생각보다 비싸다.
            - 따라서 코어 간에 데이터 전송 시간보다 오래 걸리는 작업만 병렬로 다른 코어에서 수행하는 것이 바람직하다.
        - 상황에 따라 쉽게 병렬화를 이용할 수 있거나 아예 병렬화를 이용할 수 없을 때도 있다.
            - 스트림을 병렬화해서 코드 실행 속도를 빠르게 하고 싶으면 항상 병렬화를 올바르게 사용하고 있는지 확인해야 한다.
     */

    /* 7.1.3 병렬 스트림의 올바른 활용법
        - 병렬 스트림을 잘못 사용하면서 발생하는 많은 문제는 공유된 상태를 바꾸는 알고리즘을 사용하기 때문에 일어난다.
        - 병렬 스트림이 올바르게 동작하려면 공유된 가변 상태를 피해야 한다.
     */

    public static class Accumulator {
        public long total = 0;
        public void add(long value) { total += value; }
    }

    // n 까지의 자연수를 더하면서 공유된 누적자를 바꾸는 함수
    // 병렬로 실행하면 참사가 일어난다.
    // 특히 total 을 접근할 때마다 다수의 스레드에서 동시에 데이터에 접근하는 데이터 레이스 문제가 발생한다.
    // 동기화 문제를 해결하다보면 결국 병렬화라는 특성이 없어져 버릴 것이다.
    public long sideEffectSum(long n) {
        Accumulator accumulator = new Accumulator();
        LongStream.rangeClosed(1, n).forEach(accumulator::add);
        return accumulator.total;
    }

    // 참사 확인
    public long sideEffectParallelSum(long n) {
        Accumulator accumulator = new Accumulator();
        LongStream.rangeClosed(1, n).parallel().forEach(accumulator::add);
        return accumulator.total;
    }

    // total += value 는 아토믹 연산이 아니다.
    // 따라서 여러 스레드에서 공유하는 객체의 상태를 바꾸는 메서드를 호출하면서 문제가 발생한다.
    // 상태 공유에 따른 부작용을 피해야 한다.
    @Test
    void sideEffectTest() {
        for (int i = 0; i < 5; i++) {
            System.out.println("SideEffect parallel sum : " + sideEffectParallelSum(1_000_000L));
        }
    }

    /* 7.1.4 병렬 스트림 효과적으로 사용하기
        - 양을 기준으로 병렬 스트림 사용을 결정하는 것은 바람직하지 않다.
            - 정해진 기기에서 정해진 연산을 수행할 때는 효과적일 수 있으나, 상황이 달라지면 기준이 제 역할을 못한다.
        - 확신이 서지 않으면 직접 측정하라.
            - 병렬 스트림이 순차 스트림보다 항상 빠른 것이 아니다.
            - 병렬 스트림의 수행과정은 투명하지 않을 때가 많다.
        - 박싱을 주의하라.
            - 자동 박싱과 언박싱은 성능을 크게 저하시킬 수 있는 요소다.
            - 박싱 동작을 피하기 위한 기본형 특화 스틀미을 사용하자.(IntStream, LongStream, DoubleStream)
        - 순차 스트림보다 병렬 스트림에서 성능이 떨어지는 연산이 있다.
            - 특히 `limit`이나 `findFirst`처럼 요소의 순서에 의존하는 연산을 병렬 스틀미에서 수행하려면 비싼 비용을 치러야 한다.
            - `findAny`는 순서와 상관없으므로 `findFirst`보다 성능이 좋다.
            - 정렬된 스트림에 `unordered`를 호출하면 비정렬된 스트림을 얻을 수 있다.
            - 스트림 요소의 순서가 상관없다면 비정렬된 스틀미에 `limit`을 호출하는 것이 더 효율적이다.
        - 스트림에서 수행하는 전체 파이프라인 연산 비용을 고려하라.
            - 전체 스트림 파이프라인 저리 비용 = N(= 처리해야 할 요소 수) * Q(= 하나의 요소를 처리하는데 드는 비용)
            - Q가 높을수록 병렬 스트림으로 성능을 개선할 수 있는 가능성이 있다.
        - 소량의 데이터에서는 병렬 스트림이 도움 되지 않는다.
            - 병렬화 과정에서 생기는 부가 비용을 상쇄할 수 있을 만큼의 이득을 얻지 못하기 때문이다.
        - 스트림을 구성하는 자료구조가 적절한지 확인하라.
            - `ArrayList`가 `LinkedList`보다 효율적으로 분할할 수 있다.
                - `LinkedList`를 분할하려면 모든 요소를 탐색해야 한다.
                - `ArrayList`는 요소를 탐색하지 않고도 리스트를 분할할 수 있기 때문이다.
            - `range` 팩토리 메서드로 만든 기본형 스트림도 쉽게 분해할 수 있다.
            - `Spliterator`를 구현해서 분해 과정을 완벽하게 제어할 수 있다.
        - 스트림의 특성과 파이프라인의 중간 연산이 스트림의 특성을 어떻게 바꾸는지에 따라 분해 과정의 성능이 달라질 수 있다.
            - SIZED 스트림 : 정확히 같은 크기의 두 스트림으로 분할 가능하여 효과적인 병렬 처리 가능
            - 필터 연산이 있으면 스트림의 길이를 예측할 수 없으므로 효과적인 병렬 처리가 불확실
        - 최종 연산의 병합 과정(`Collector.combine` 등) 비용을 살펴보라.
            - 병합 과정의 비용이 비싸다면 병렬 스트림으로 얻은 성능의 이익이 서브스트림의 부분 결과를 합치는 과정에서 상쇄될 수 있다.
        - 병렬 스트림이 수행되는 내부 인프라구조도 살펴봐야 한다.
     */

    /* 스트림 소스와 분해성
        - 훌륭함 : ArrayList, IntStream.range
        - 좋음 : HashSet, TreeSet
        - 나쁨 : LinkedList, Stream.iterate
     */


    /**
     * 7.2 포크/조인 프레임워크
     */

}

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 2, jvmArgs = {"-Xms4G", "-Xms4G"})
class ParallelStreamBenchmark {

    private static final long N = 10_000_000L;

    @Benchmark
    public long iterativeSum() {
        long result = 0;
        for (long i = 1L; i <= N; i++) {
            result += i;
        }
        return result;
    }

    @Benchmark
    public long sequentailSum() {
        return Stream.iterate(1L, i -> i + 1).limit(N).reduce(0L, Long::sum);
        // iterativeSum()보다 더 느리다.
        // 1. 반복 결과로 박싱된 객체가 만들어지므로 숫자를 더하려면 언박싱을 해야 한다.
        // 2. 반복 작업은 병렬로 수행할 수 있는 독립 단위로 나누기가 어렵다.
    }

    // 더 특화된 메서드 사용하기
    // Long 박싱/언박싱을 피하기 위한 메서드 사용
    @Benchmark
    public long rangedSum() {
        return LongStream.rangeClosed(1, N)
                .reduce(0L, Long::sum);
        // iterativeSum()보다 더 빠르다.
        // 특화되지 않은 스트림을 처리할 때는 오토박싱, 언박싱 등의 오버헤드를 수반하기 때문이다.
        // 상황에 따라서는 어떤 알고리즘을 병렬화하는 것보다 적절한 자료구조를 선택하는 것이 더 중요하다.
    }

    @Benchmark
    public long parallelRangedSum() {
        return LongStream.rangeClosed(1, N)
                .parallel()
                .reduce(0L, Long::sum);
        // rangedSum()보다 훨씬 빠르다.
        // 올바른 자료구조를 선택해야 병렬 실행도 최적의 성능을 발휘할 수 있다.
    }

    @TearDown(Level.Invocation)
    public void tearDown() {
        System.gc();
    }
}
