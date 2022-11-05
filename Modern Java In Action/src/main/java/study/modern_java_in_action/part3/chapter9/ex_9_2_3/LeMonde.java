package study.modern_java_in_action.part3.chapter9.ex_9_2_3;

public class LeMonde implements Observer {

    @Override
    public void notify(String tweet) {
        if (tweet != null && tweet.contains("wine")) {
            System.out.println("Today cheese, wine and news!  " + tweet);
        }
    }

}
