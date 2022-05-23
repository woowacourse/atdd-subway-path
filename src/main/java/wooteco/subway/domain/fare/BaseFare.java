package wooteco.subway.domain.fare;

public class BaseFare implements Fare{

    private final int extraFare;

    public BaseFare(final int extraFare) {
        this.extraFare = extraFare;
    }

    @Override
    public double calculateExtraFare() {
        return extraFare + 1250;
    }
}
