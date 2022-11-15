package study.modern_java_in_action.part4.chapter13.ex_13_3;

public interface Rotatable {

    void setRotationAngle(int angleInDegrees);
    int getRotationAngle();

    default void rotateBy(int angleInDegress) {
        setRotationAngle((getRotationAngle() + angleInDegress) % 360);
    }
}
