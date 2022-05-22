package wooteco.subway.domain.strategy.fare.basic;

import org.springframework.stereotype.Component;

@Component
public class BasicDistanceFareStrategy implements DistanceFareStrategy {

    private static final int BASIC_FARE = 1250;

    @Override
    public int calculateFare(int distance) {
        return BASIC_FARE;
    }
}
