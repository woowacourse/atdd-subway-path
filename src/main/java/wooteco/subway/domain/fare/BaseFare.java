package wooteco.subway.domain.fare;

public class BaseFare implements Fare {

    private static final int BASIC_FARE = 1250;
    private final int extraFare;

    public BaseFare(final int extraFare) {
        this.extraFare = extraFare;
    }

    @Override
    public double calculateExtraFare() {
        return extraFare + BASIC_FARE;
    }
}
