package study.modern_java_in_action.part3.chapter9.ex_9_2_2;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Customer {
    private final int id;
    private final String name;
}
