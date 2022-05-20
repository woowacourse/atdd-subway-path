package wooteco.subway.domain.fare;

public abstract class OverFareStrategy extends FareStrategy{

    protected int calculateOverFare(final int distance, final int unit) {
        return (int) ((Math.ceil((distance - 1) / unit) + 1) * 100);
    }
}
