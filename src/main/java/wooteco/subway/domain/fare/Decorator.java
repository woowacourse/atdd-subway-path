package wooteco.subway.domain.fare;

public class Decorator implements Fare {
    private final Fare fare;

    public Decorator(final Fare fare) {
        this.fare = fare;
    }

    @Override
    public double calculateExtraFare() {
        return fare.calculateExtraFare();
    }
}
