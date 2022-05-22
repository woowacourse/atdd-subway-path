package wooteco.subway.domain;

public class Fare {

    public Fare() {
    }

    public int calculateFare(int distance, FareStrategy fareStrategy) {
        return fareStrategy.calculate(distance);
    }
}
