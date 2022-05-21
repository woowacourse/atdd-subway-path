package wooteco.subway.domain.path.fare2;

public class DistanceOverFare extends Decorator {

    private static final int OVER_FARE_DIGIT = 100;
    private static final int FIRST_OVER_FARE_AREA_THRESHOLD = 10;
    private static final int FIRST_OVER_FARE_DISTANCE_LIMIT = 5;
    private static final int SECOND_OVER_FARE_AREA_THRESHOLD = 50;
    private static final int SECOND_OVER_FARE_DISTANCE_LIMIT = 8;

    private final int distance;

    public DistanceOverFare(Fare delegate, int distance) {
        super(delegate);
        this.distance = distance;
    }

    @Override
    public int calculate() {
        int fare = super.delegate();
        int firstAreaOverFare = calculateFirstAreaOverFare();
        int secondAreaOverFare = calculateSecondAreaOverFare();
        return fare + firstAreaOverFare + secondAreaOverFare;
    }

    private int calculateFirstAreaOverFare() {
        if (distance <= FIRST_OVER_FARE_AREA_THRESHOLD) {
            return 0;
        }
        int maxDistance = Math.min(distance, SECOND_OVER_FARE_AREA_THRESHOLD);
        int overDistance = maxDistance - FIRST_OVER_FARE_AREA_THRESHOLD;
        return toOverFare(overDistance, FIRST_OVER_FARE_DISTANCE_LIMIT);
    }

    private int calculateSecondAreaOverFare() {
        if (distance <= SECOND_OVER_FARE_AREA_THRESHOLD) {
            return 0;
        }
        int overDistance = distance - SECOND_OVER_FARE_AREA_THRESHOLD;
        return toOverFare(overDistance, SECOND_OVER_FARE_DISTANCE_LIMIT);
    }

    private int toOverFare(int overDistance, int limit) {
        double overDigit = Math.ceil((overDistance - 1) / limit) + 1;
        return (int) (overDigit * OVER_FARE_DIGIT);
    }
}
