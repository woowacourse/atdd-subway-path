package wooteco.subway.domain.strategy.fare;

public interface ExtraFareStrategy {

    int FARE_UNIT = 100;

    int calculate(int distance);
}
