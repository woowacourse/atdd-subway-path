package wooteco.subway.domain;

public class Fare {

    public int calculateFare(int distance) {
        if (distance <= 10) {
            return 1250;
        }
        if (distance <= 50) {
            return 1250 + calculateOverFare(distance-10, 5);
        }
        return 1250 + 800 + calculateOverFare(distance-50, 8);
    }

    private int calculateOverFare(int distance, int farePerKilometre) {
        return (int) ((Math.ceil((distance - 1) / farePerKilometre) + 1) * 100);
    }
}
