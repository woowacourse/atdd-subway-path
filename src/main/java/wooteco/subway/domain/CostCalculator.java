package wooteco.subway.domain;

public class CostCalculator {

    private static final int BASIC_COST = 1250;
    private static final int CHARGED_COST = 100;
    private static final int CHARGED_DISTANCE_1 = 10;
    private static final int CHARGED_DISTANCE_2 = 50;
    private static final int CHARGED_UNIT_1 = 5;
    private static final int CHARGED_UNIT_2 = 8;

    public static int calculate(int distance, int extraFare) {
        return calculateByDistance(distance) + extraFare;
    }

    private static int calculateByDistance(int distance) {
        int cost = BASIC_COST;
        if (distance > CHARGED_DISTANCE_2) {
            cost += calculateCost(distance, CHARGED_DISTANCE_2, CHARGED_UNIT_2);
            distance = CHARGED_DISTANCE_2;
        }
        if (distance > CHARGED_DISTANCE_1) {
            cost += calculateCost(distance, CHARGED_DISTANCE_1, CHARGED_UNIT_1);
        }
        return cost;
    }

    private static int calculateCost(int distance, int baseDistance, int unit) {
        return ((distance - baseDistance - 1) / unit + 1) * CHARGED_COST;
    }
}
