package wooteco.subway.domain;

public class Fare {

    public static final int BASIC_FARE = 1250;
    public static final int ADDITIONAL_FARE = 100;
    public static final int MINIMUM_DISTANCE_BOUNDARY = 10;
    public static final int MAXIMUM_DISTANCE_BOUNDARY = 50;
    public static final int BELOW_FIFTY_KM_POLICY = 5;
    public static final int ABOVE_FIFTY_KM_POLICY = 8;
    public static final int ADDITIONAL_FARE_50_KM = 800;

    private final int distance;

    public Fare(int distance) {
        validateDistance(distance);
        this.distance = distance;
    }

    private void validateDistance(int distance) {
        if (distance < 0) {
            throw new IllegalArgumentException("거리는 양수여야합니다.");
        }
    }


    public int calculate() {
        if (distance <= MINIMUM_DISTANCE_BOUNDARY) {
            return BASIC_FARE;
        }

        if (distance <= MAXIMUM_DISTANCE_BOUNDARY) {
            return BASIC_FARE
                    + (int) (Math.ceil((double) (distance - MINIMUM_DISTANCE_BOUNDARY) / BELOW_FIFTY_KM_POLICY))
                    * ADDITIONAL_FARE;
        }

        return BASIC_FARE + ADDITIONAL_FARE_50_KM
                + (int) Math.ceil(((double) (distance - MAXIMUM_DISTANCE_BOUNDARY) / ABOVE_FIFTY_KM_POLICY))
                * ADDITIONAL_FARE;
    }
}
