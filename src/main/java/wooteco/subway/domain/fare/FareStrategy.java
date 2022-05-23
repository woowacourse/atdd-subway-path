package wooteco.subway.domain.fare;

public interface FareStrategy {
    int calculate(int distance);
}
