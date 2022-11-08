package study.modern_java_in_action.part4.chapter11.ex_11_2;

import java.util.Optional;

public class Person2 {
    private int age;
    private Optional<Car2> car;
    public Optional<Car2> getCar() {
        return car;
    }
    public int getAge() {
        return age;
    }

}
