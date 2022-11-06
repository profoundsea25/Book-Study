package study.modern_java_in_action.part3.chapter10.ex_10_3_5;

import study.modern_java_in_action.part3.chapter10.ex_10_3.Order;

import java.util.function.DoubleUnaryOperator;

public class TaxFunctionCalculator {

    public DoubleUnaryOperator taxFunction = d -> d;

    public TaxFunctionCalculator with(DoubleUnaryOperator f) {
        taxFunction = taxFunction.andThen(f);
        return this;
    }

    public double calculate(Order order) {
        return taxFunction.applyAsDouble(order.getValue());
    }

}
