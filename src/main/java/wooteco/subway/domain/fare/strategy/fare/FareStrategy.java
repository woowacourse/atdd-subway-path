package wooteco.subway.domain.fare.strategy.fare;

public interface FareStrategy {

    int calculate(double distance, int extraFare);
}
