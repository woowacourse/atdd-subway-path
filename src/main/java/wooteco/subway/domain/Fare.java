package wooteco.subway.domain;

public class Fare {

    private final int fare;

    public Fare() {
        this.fare = 1250;
    }


    public int calculateFare(int distance) {
        if (distance <= 10) {
            return fare;
        }
        if (distance <= 50) {
            return fare + calculateOverTenDistance(distance);
        }
        return fare + calculateOverTenDistance(50) + calculateOverFiftyDistance(distance);
    }

    private int calculateOverTenDistance(int distance) {
        return (int) ((Math.ceil((distance - 10) / 5.0)) * 100);
    }

    private int calculateOverFiftyDistance(int distance) {
        return (int) ((Math.ceil((distance - 50) / 8.0)) * 100);
    }

    public int getFare() {
        return fare;
    }
}
