package study.modern_java_in_action.part3.chapter9.ex_9_2_5;

public class ProductFactory {

    public static Product createProduct(String name) {
        switch (name) {
            case "loan":
                return new Loan();
            case "stock":
                return new Stock();
            case "bond":
                return new Bond();
            default:
                throw new RuntimeException("No such product" + name);
        }
    }

}


