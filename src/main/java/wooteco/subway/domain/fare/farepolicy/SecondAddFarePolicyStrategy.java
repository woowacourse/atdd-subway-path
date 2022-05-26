package wooteco.subway.domain.fare.farepolicy;

class SecondAddFarePolicyStrategy implements FarePolicyStrategy {

    private static final int LOWER_BOUND_DISTANCE = 50;
    private static final int KILOMETER_UNIT = 8;
    private static final int ADDED_FARE = 100;

    @Override
    public int calculateFare(int distance) {
        return new FirstAddFarePolicyStrategy().calculateFare(LOWER_BOUND_DISTANCE)
                + calculateOverFare(distance - LOWER_BOUND_DISTANCE);
    }

    private static int calculateOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / KILOMETER_UNIT) + 1) * ADDED_FARE);
    }
}
