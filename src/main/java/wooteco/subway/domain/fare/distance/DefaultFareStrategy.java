package wooteco.subway.domain.fare.distance;

public class DefaultFareStrategy implements DistanceFareStrategy {

    private static final int baseFare = 1250;

    @Override
    public int calculate(final double distance) {
        return baseFare;
    }
}
