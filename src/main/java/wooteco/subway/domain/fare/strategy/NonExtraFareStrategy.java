package wooteco.subway.domain.fare.strategy;

public class NonExtraFareStrategy implements ExtraFareStrategy {

    @Override
    public int calculate(int distance) {
        return 0;
    }
}
