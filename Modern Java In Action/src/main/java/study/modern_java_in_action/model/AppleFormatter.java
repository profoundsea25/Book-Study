package study.modern_java_in_action.model;

@FunctionalInterface
public interface AppleFormatter {
    String accept(Apple apple);
}
