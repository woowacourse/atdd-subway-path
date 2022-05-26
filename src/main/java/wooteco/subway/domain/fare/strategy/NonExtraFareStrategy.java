package wooteco.subway.domain.fare.strategy;

public class NonExtraFareStrategy implements ExtraFareStrategy {

    private static final int NO_EXTRA_STANDARD_DISTANCE = 10;

    @Override
    public int calculate(int distance) {
        return 0;
    }

    @Override
    public boolean isMatch(int distance) {
        return NO_EXTRA_STANDARD_DISTANCE >= distance && distance < 0;
    }
}
