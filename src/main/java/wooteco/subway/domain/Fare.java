package wooteco.subway.domain;

public class Fare {

    private final int basicFare;

    public Fare(int basicFare) {
        this.basicFare = basicFare;
    }

    public int calculateFare(int distance, FareStrategy fareStrategy) {
        return basicFare + fareStrategy.calculate(distance);
    }
}
