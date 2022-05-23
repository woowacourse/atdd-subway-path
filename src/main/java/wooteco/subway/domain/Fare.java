package wooteco.subway.domain;

public class Fare {
    private final FareStrategy fareStrategy;

    public Fare(FareStrategy fareStrategy) {
        this.fareStrategy = fareStrategy;
    }

    public int calculate(Path path) {
        int distance = path.getShortestDistance();
        return fareStrategy.calculate(distance);
    }
}
