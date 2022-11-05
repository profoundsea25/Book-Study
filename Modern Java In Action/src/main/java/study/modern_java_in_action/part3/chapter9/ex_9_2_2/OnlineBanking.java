package study.modern_java_in_action.part3.chapter9.ex_9_2_2;

abstract class OnlineBanking {

    // 람다 사용하지 않음
    public void processCustomer(int id) { // 온라인 뱅킹 알고리즘이 해야 할 일
        Customer c = Database.getCustomerWithId(id);
        makeCustomerHappy(c);
    }

    abstract void makeCustomerHappy(Customer c);

}
