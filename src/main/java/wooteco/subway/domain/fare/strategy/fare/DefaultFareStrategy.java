package wooteco.subway.domain.fare.strategy.fare;

public class DefaultFareStrategy implements FareStrategy {

    private static final int MINIMUM_FARE = 1250;
    private static final int MINIMUM_ADDITIONAL_FARE = 100;

    private static final int FIRST_ADDITIONAL_FARE_DISTANCE = 10;
    private static final int FIRST_ADDITIONAL_STANDARD_DISTANCE = 5;
    private static final int SECOND_ADDITIONAL_FARE_DISTANCE = 50;
    private static final int SECOND_ADDITIONAL_STANDARD_DISTANCE = 8;

    private static final int DEFAULT_PORTION = 1;

    public int calculate(double distance, int extraFare) {
        return MINIMUM_FARE + addOverTen(distance) + addOverFifty(distance) + extraFare;
    }

    private int addOverTen(double distance) {
        if (isUnderAdditionalFareDistance(distance, FIRST_ADDITIONAL_FARE_DISTANCE)) {
            return 0;
        }
        if (distance >= SECOND_ADDITIONAL_FARE_DISTANCE) {
            distance = SECOND_ADDITIONAL_FARE_DISTANCE;
        }
        int portion = getPortion(distance, FIRST_ADDITIONAL_FARE_DISTANCE, FIRST_ADDITIONAL_STANDARD_DISTANCE);
        return calculateFare(portion);
    }

    private int addOverFifty(double distance) {
        if (isUnderAdditionalFareDistance(distance, SECOND_ADDITIONAL_FARE_DISTANCE)) {
            return 0;
        }
        int portion = getPortion(distance, SECOND_ADDITIONAL_FARE_DISTANCE, SECOND_ADDITIONAL_STANDARD_DISTANCE);
        return calculateFare(portion);
    }

    private boolean isUnderAdditionalFareDistance(double distance, int additionalFareDistance) {
        return distance <= additionalFareDistance;
    }

    private int getPortion(double distance, int additionalFareDistance, int additionalStandardDistance) {
        int portion = (int) (distance - additionalFareDistance) / additionalStandardDistance;
        if (portion == 0) {
            portion = DEFAULT_PORTION;
        }
        return portion;
    }

    private int calculateFare(int portion) {
        return portion * MINIMUM_ADDITIONAL_FARE;
    }
}
