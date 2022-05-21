package wooteco.subway.domain;

public class CostCalculator {

    private static final int BASIC_COST = 1250;
    private static final int EXTRA_COST = 100;
    private static final int DISTANCE_1 = 10;
    private static final int DISTANCE_2 = 50;
    private static final int UNIT_1 = 5;
    private static final int UNIT_2 = 8;

    private final int distance;
    private final int extraFare;

    public CostCalculator(int distance, int extraFare) {
        this.distance = distance;
        this.extraFare = extraFare;
    }

    public int calculate() {
        return calculateByDistance() + extraFare;
    }

    private int calculateByDistance() {
        int cost = BASIC_COST;
        int distance = this.distance;

        if (distance > DISTANCE_2) {
            cost += calculateCost(distance, DISTANCE_2, UNIT_2);
            distance = DISTANCE_2;
        }
        if (distance > DISTANCE_1) {
            cost += calculateCost(distance, DISTANCE_1, UNIT_1);
        }
        return cost;
    }

    private int calculateCost(int distance, int paidDistance, int unit) {
        return ((distance - paidDistance - 1) / unit + 1) * EXTRA_COST;
    }
}
