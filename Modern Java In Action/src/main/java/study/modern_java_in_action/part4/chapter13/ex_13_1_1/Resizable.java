package study.modern_java_in_action.part4.chapter13.ex_13_1_1;

public interface Resizable extends Drawable {
    int getWeight();

    int getHeight();

    void setWidth(int width);

    void setHeight(int height);

    void setAbsoluteSize(int width, int height);
}
