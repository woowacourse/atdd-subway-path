package wooteco.subway.domain;

public class Fee {

    private static final int OVER_FARE_FEE = 100;
    private static final int MINIMUM_FARE_FEE = 1250;
    private static final int MINIMUM_DISTANCE_BOUNDARY = 10;
    private static final int MAXIMUM_DISTANCE_BOUNDARY = 50;
    private static final int BELOW_FIFTY_KM_POLICY = 5;
    private static final int ABOVE_FIFTY_KM_POLICY = 8;

    private final int price;

    public Fee(final int distance) {
        this.price = calculateFee(distance);
    }

    public int calculateFee(final double distance) {
        if (distance <= MINIMUM_DISTANCE_BOUNDARY) {
            return MINIMUM_FARE_FEE;
        }

        return MINIMUM_FARE_FEE + calculateFareFee(distance);
    }

    private int calculateFareFee(final double distance) {
        if (distance <= MAXIMUM_DISTANCE_BOUNDARY) {
            return calculateFareFeeByPolicy(distance  - MINIMUM_DISTANCE_BOUNDARY, BELOW_FIFTY_KM_POLICY);
        }

        return 800 + calculateFareFeeByPolicy(distance - MAXIMUM_DISTANCE_BOUNDARY, ABOVE_FIFTY_KM_POLICY);
    }

    private int calculateFareFeeByPolicy(final double overFaredDistance, final int policy) {
        return (int) ((Math.ceil((overFaredDistance) / policy)) * OVER_FARE_FEE);
    }

    public int getPrice() {
        return price;
    }
}
