package wooteco.subway.domain.section;

import java.util.function.Function;
import wooteco.subway.domain.path.Fare;

public class Distance {
    private final double value;

    private Distance(double value) {
        checkValue(value);
        this.value = value;
    }

    private void checkValue(double value) {
        if (value < 0) {
            throw new IllegalArgumentException("거리는 음수일 수 없습니다.");
        }
    }

    public static Distance fromKilometer(double value) {
        return new Distance(value);
    }

    public static Distance fromMeter(double value) {
        return new Distance(value / 1000.0);
    }

    Distance add(Distance distance) {
        return new Distance(this.value + distance.value);
    }

    Distance subtract(Distance distance) {
        if (this.value < distance.value) {
            throw new IllegalStateException("빼려는 거리가 더 커서 뺄 수 없습니다.");
        }
        return new Distance(this.value - distance.value);
    }

    boolean isSmallerThan(Distance distance) {
        return this.value < distance.value;
    }

    public Fare calculateFare() {
        Function<Double, Fare> fareCalculator = DistanceFare.fareCalculator();
        return fareCalculator.apply(value);
    }

    public double getValue() {
        return value;
    }
}
