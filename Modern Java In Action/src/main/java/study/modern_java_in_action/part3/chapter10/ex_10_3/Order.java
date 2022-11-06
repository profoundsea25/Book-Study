package study.modern_java_in_action.part3.chapter10.ex_10_3;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Order {

    private String customer;
    private List<Trade> trades = new ArrayList<>();

    public void addTrade(Trade trade) {
        trades.add(trade);
    }

    public double getValue() {
        return trades.stream().mapToDouble(Trade::getValue).sum();
    }

}
