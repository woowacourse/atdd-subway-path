package wooteco.subway.domain;

public class Fare {

    private static final int OVER_FARE_PRICE = 100;
    private static final int MINIMUM_FARE_PRICE = 1250;
    private static final int MINIMUM_DISTANCE_BOUNDARY = 10;
    private static final int MAXIMUM_DISTANCE_BOUNDARY = 50;
    private static final int BELOW_FIFTY_KM_POLICY = 5;
    private static final int ABOVE_FIFTY_KM_POLICY = 8;

    private final int price;

    public Fare(final int price) {
        this.price = price;
    }

    public static Fare from(final double distance, final int extraFare, final int age) {
        return new Fare(calculateFare(distance, extraFare, age));
    }

    private static int calculateFare(final double distance, final int extraFare, final int age) {
        final int price = MINIMUM_FARE_PRICE + extraFare;
        if (distance <= MINIMUM_DISTANCE_BOUNDARY) {
            return price;
        }

        return price + calculateFarePrice(distance);
    }

    private static int calculateFarePrice(final double distance) {
        if (distance <= MAXIMUM_DISTANCE_BOUNDARY) {
            return calculateFareFeeByPolicy(distance - MINIMUM_DISTANCE_BOUNDARY, BELOW_FIFTY_KM_POLICY);
        }

        return 800 + calculateFareFeeByPolicy(distance - MAXIMUM_DISTANCE_BOUNDARY, ABOVE_FIFTY_KM_POLICY);
    }

    private static int calculateFareFeeByPolicy(final double overFaredDistance, final int policy) {
        return (int) ((Math.ceil((overFaredDistance) / policy)) * OVER_FARE_PRICE);
    }

    public int getPrice() {
        return price;
    }
}
