package study.modern_java_in_action.part3.chapter9.ex_9_2_3;

public interface Subject {
    void registerObserver(Observer o);
    void notifyObservers(String tweet);
}
