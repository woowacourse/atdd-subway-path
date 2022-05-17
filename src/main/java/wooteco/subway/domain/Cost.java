package wooteco.subway.domain;

public class Cost {
    public static final int DEFAULT_FARE = 1250;

    private final int cost;

    private Cost(int cost) {
        this.cost = cost;
    }

    public static Cost from(int distance) {
        return new Cost(DEFAULT_FARE + calculateOverFare(distance));
    }

    private static int calculateOverFare(int distance) {
        if (distance <= 10) {
            return 0;
        }

        if (distance <= 50) {
            distance -= 10;
            return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
        }

        distance -= 50;
        return 800 + (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }

    public int getCost() {
        return cost;
    }
}
