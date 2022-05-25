package wooteco.subway.domain.fare.distance;

public interface DistanceFareStrategy {
    int calculate(final double distance);
}
