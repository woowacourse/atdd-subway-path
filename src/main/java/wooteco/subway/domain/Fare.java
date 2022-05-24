package wooteco.subway.domain;

public class Fare {

    public Fare() {
    }

    public int calculateFare(int distance, int extraFare, FareStrategy fareStrategy) {
        return extraFare + fareStrategy.calculate(distance);
    }
}
