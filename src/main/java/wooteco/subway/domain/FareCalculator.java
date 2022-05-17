package wooteco.subway.domain;

public class FareCalculator {

    private final double distance;

    public FareCalculator(double distance) {
        this.distance = distance;
    }

    public int calculateFare() {
        int fare = 1250;
        if (distance > 10 && distance <= 50) {
            fare += (int) ((Math.ceil((distance - 11) / 5)) * 100);
        }
        if (distance > 50) {
            fare = 2050;
            double a = distance - 50;
            fare += (int) ((Math.ceil((a - 1) / 8)) * 100);
        }
        return fare;
    }
}
