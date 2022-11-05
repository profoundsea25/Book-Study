package study.modern_java_in_action.part3.chapter9.ex_9_2_5;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ProductFactoryLambda {

    final static Map<String, Supplier<Product>> map = new HashMap<>();
    static {
        map.put("loan", Loan::new);
        map.put("stock", Stock::new);
        map.put("bond", Bond::new);
    }

    public static Product createProduct(String name) {
        Supplier<Product> p = map.get(name);
        if (p != null) {
            return p.get();
        }
        throw new IllegalArgumentException("No such product " + name);
    }

    // 여기서는 인수를 하나만 받기 때문에 `Supplier`를 사용했지만, 인수가 많아진다면 다른 함수형 인터페이스를 사용해야 한다.
    // 예를 들면, `TriFunction<T, U, V, R>` 등

}
