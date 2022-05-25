package wooteco.subway.domain.fare;

import java.util.ArrayList;
import java.util.List;
import wooteco.subway.domain.Line;

public class FareCondition {

    private final int distance;
    private final List<Line> line;
    private final int age;

    public FareCondition(int distance, List<Line> line, int age) {
        this.distance = distance;
        this.line = line;
        this.age = age;
    }

    public int getDistance() {
        return distance;
    }

    public List<Line> getLine() {
        return new ArrayList<>(line);
    }

    public int getAge() {
        return age;
    }
}
