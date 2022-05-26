package wooteco.subway.domain.fare.strategy;

public interface ExtraFareStrategy {

    int FARE_UNIT = 100;

    int calculate(int distance);

    boolean isMatch(int distance);
}
