package wooteco.subway.domain;

import java.util.function.Function;

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

    public Distance add(Distance distance) {
        return new Distance(this.value + distance.value);
    }

    public Distance subtract(Distance distance) {
        if (this.value < distance.value) {
            throw new IllegalStateException("빼려는 거리가 더 커서 뺄 수 없습니다.");
        }
        return new Distance(this.value - distance.value);
    }

    public boolean isSmallerThan(Distance distance) {
        return this.value < distance.value;
    }

    public int calculateFare() {
        Function<Double, Integer> fareCalculator = ExtraFare.fareCalculator();
        return fareCalculator.apply(value);
    }

    public double getValue() {
        return value;
    }
}
