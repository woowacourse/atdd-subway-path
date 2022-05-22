package wooteco.subway.domain.strategy;

public interface FareStrategy {

    int calculateFare(int distance, int extraFare, int age);

}
