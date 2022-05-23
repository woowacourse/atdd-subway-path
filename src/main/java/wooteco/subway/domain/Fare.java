package wooteco.subway.domain;

import java.util.List;
import java.util.NoSuchElementException;
import wooteco.subway.domain.strategy.FareStrategy;

public class Fare {

    private static final int BASIC_FARE = 1250;

    private final FareStrategy fareStrategy;

    public Fare(FareStrategy fareStrategy) {
        this.fareStrategy = fareStrategy;
    }

    public int calculateFare(int distance) {
        return BASIC_FARE + fareStrategy.calculate(distance);
    }

    public int calculateMaxLineExtraFare(List<Line> lines) {
        return lines.stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElseThrow(NoSuchElementException::new);
    }
}
