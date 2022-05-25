package wooteco.subway.domain.strategy.fare;

public class NonExtraFareStrategy implements ExtraFareStrategy {

    @Override
    public int calculate(int distance) {
        return 0;
    }
}
