package wooteco.subway.domain;

public class Fare {

    public static final int BASIC_FARE = 1250;
    public static final int ADDITIONAL_FARE = 100;
    public static final int MINIMUM_DISTANCE_BOUNDARY = 10;
    public static final int MAXIMUM_DISTANCE_BOUNDARY = 50;
    public static final int BELOW_FIFTY_KM_POLICY = 5;
    public static final int ABOVE_FIFTY_KM_POLICY = 8;

    private final int value;


    private Fare(int value) {
        this.value = value;
    }

    public static Fare of(int distance, int lineFare) {
        validateDistance(distance);
        final int value = calculate(distance) + lineFare;
        return new Fare(value);
    }

    private static void validateDistance(int distance) {
        if (distance < 0) {
            throw new IllegalArgumentException("거리는 양수여야합니다.");
        }
    }

    private static int calculate(int distance) {
        if (distance <= MINIMUM_DISTANCE_BOUNDARY) {
            return BASIC_FARE;
        }

        if (distance <= MAXIMUM_DISTANCE_BOUNDARY) {
            return BASIC_FARE + calculateBoundaryFare(distance, MINIMUM_DISTANCE_BOUNDARY, BELOW_FIFTY_KM_POLICY);
        }

        return BASIC_FARE
                + calculateBoundaryFare(MAXIMUM_DISTANCE_BOUNDARY, MINIMUM_DISTANCE_BOUNDARY, BELOW_FIFTY_KM_POLICY)
                + calculateBoundaryFare(distance, MAXIMUM_DISTANCE_BOUNDARY, ABOVE_FIFTY_KM_POLICY);
    }

    private static int calculateBoundaryFare(int distance, int distanceBoundary, int distancePolicy) {
        return (int) Math.ceil(((double) (distance - distanceBoundary) / distancePolicy))
                * ADDITIONAL_FARE;
    }

    public int getValue() {
        return value;
    }

    public int getDiscountedValue(AgeGroup ageGroup) {
        return ageGroup.getDiscountedValue(value);
    }
}
