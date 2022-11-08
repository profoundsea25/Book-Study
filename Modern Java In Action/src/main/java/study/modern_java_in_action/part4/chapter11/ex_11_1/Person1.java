package study.modern_java_in_action.part4.chapter11.ex_11_1;

import java.util.Optional;

public class Person1 {
    private Car1 car;
    public Car1 getCar() {
        return car;
    }

    // 직렬화 모델이 필요하다면 다음과 같은 메서드를 추가하는 방식을 권장
    public Optional<Car1> getCarAsOptional() {
        return Optional.ofNullable(car);
    }
}
