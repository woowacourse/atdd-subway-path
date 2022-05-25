package wooteco.subway.domain.strategy.fare;

public class NonExtraFare implements ExtraFareStrategy {

    @Override
    public int calculate(int distance) {
        return 0;
    }
}
