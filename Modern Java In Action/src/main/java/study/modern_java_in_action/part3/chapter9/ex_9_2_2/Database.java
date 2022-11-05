package study.modern_java_in_action.part3.chapter9.ex_9_2_2;

public class Database {

    public static Customer getCustomerWithId(int id) {
        return new Customer(id, String.valueOf(id));
    }

}
