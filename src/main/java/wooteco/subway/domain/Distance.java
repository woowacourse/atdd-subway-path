package wooteco.subway.domain;

import java.util.function.Consumer;

public class Distance {
    private final double value;

    private Distance(double value) {
        this.value = value;
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
        return new Distance(this.value - distance.value);
    }

    public boolean isSmallerThen(Distance distance) {
        return this.value < distance.value;
    }

    public int calculateFare() {
        return ExtraFare.calculateTotalFare(value);
    }

    public void consumeValueTo(Consumer<Double> consumer) {
        consumer.accept(value);
    }

    public double getValue() {
        return value;
    }
}
