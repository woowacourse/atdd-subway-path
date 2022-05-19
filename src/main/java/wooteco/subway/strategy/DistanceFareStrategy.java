package wooteco.subway.strategy;

public class DistanceFareStrategy implements FareStrategy {

    private static final int BASE_FARE = 1250;
    private static final int EXTRA_FARE = 100;
    private static final int SECOND_STANDARD_DISTANCE_FOR_EXTRA_FARE = 50;
    private static final int FIRST_STANDARD_DISTANCE_FOR_EXTRA_FARE = 10;
    private static final int SECOND_UNIT_DISTANCE_FOR_EXTRA_FARE = 8;
    private static final int FIRST_UNIT_DISTANCE_FOR_EXTRA_FARE = 5;

    @Override
    public int calculate(int distance) {
        int fee = BASE_FARE;

        if (distance > SECOND_STANDARD_DISTANCE_FOR_EXTRA_FARE) {
            fee += getFare(distance - SECOND_STANDARD_DISTANCE_FOR_EXTRA_FARE,
                SECOND_UNIT_DISTANCE_FOR_EXTRA_FARE);
            distance = SECOND_STANDARD_DISTANCE_FOR_EXTRA_FARE;
        }

        if (distance > FIRST_STANDARD_DISTANCE_FOR_EXTRA_FARE) {
            fee += getFare(distance - FIRST_STANDARD_DISTANCE_FOR_EXTRA_FARE,
                FIRST_UNIT_DISTANCE_FOR_EXTRA_FARE);
            distance = FIRST_STANDARD_DISTANCE_FOR_EXTRA_FARE;
        }

        return fee;
    }

    private int getFare(int distance, int unitDistance) {
        int fare = 0;
        fare += distance / unitDistance * EXTRA_FARE;
        if (distance % unitDistance > 0) {
            fare += EXTRA_FARE;
        }
        return fare;
    }

}
