package study.modern_java_in_action.part3.chapter9.ex_9_2_2;

import java.util.function.Consumer;

public class OnlineBankingLambda {

    // 람다 사용
    public void processCustomer(int id, Consumer<Customer> makeCustomerHappy) {
        Customer c = Database.getCustomerWithId(id);
        makeCustomerHappy.accept(c);
    }

}
