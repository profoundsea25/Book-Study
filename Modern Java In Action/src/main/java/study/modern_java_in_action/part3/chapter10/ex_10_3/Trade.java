package study.modern_java_in_action.part3.chapter10.ex_10_3;

import lombok.Data;

@Data
public class Trade {

    public enum Type { BUY, SELL }
    private Type type;

    private Stock stock;
    private int quantity;
    private double price;

    public double getValue() {
        return quantity * price;
    }

}
