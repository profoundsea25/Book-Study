package study.modern_java_in_action.part3.chapter9.ex_9_3;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Point {
    private final int x;
    private final int y;

    public Point moveRightBy(int x) {
        return new Point(this.x + x, this.y);
    }

    public final static Comparator<Point> compareByXAndThenY
            = Comparator.comparing(Point::getX).thenComparing(Point::getY);

    public static List<Point> moveAllPointsRightBy(List<Point> points, int x) {
        return points.stream()
                .map(p -> new Point(p.getX() + x, p.getY()))
                .collect(Collectors.toList());
    }

}
