package wooteco.subway.domain.path;

import java.util.List;
import java.util.Set;

public class Path {
    private static final int OVER_FARE = 100;
    private static final int BASIC_FARE = 1250;
    private static final int FIRST_ADDITIONAL_INTERVAL = 10;
    private static final int SECOND_ADDITIONAL_INTERVAL = 50;
    private static final double FIRST_INTERVAL_UNIT = 5.0;
    private static final double SECOND_INTERVAL_UNIT = 8.0;

    private final int distance;
    private final Set<Long> lineIds;
    private final List<Long> stationIds;

    public Path(int distance, Set<Long> lineIds, List<Long> stationIds) {
        this.distance = distance;
        this.lineIds = lineIds;
        this.stationIds = stationIds;
    }

    public int calculateFare(int age, int extraCharge) {
        AgeDiscountPolicy discount = AgeDiscountPolicy.of(age);
        if (distance <= FIRST_ADDITIONAL_INTERVAL) {
            return discount.apply(BASIC_FARE + extraCharge);
        }
        if (distance <= SECOND_ADDITIONAL_INTERVAL) {
            return discount.apply(BASIC_FARE + calculateFareOverFirstDistance(distance) + extraCharge);
        }
        return discount.apply(BASIC_FARE
                + calculateFareOverFirstDistance(SECOND_ADDITIONAL_INTERVAL)
                + calculateFareOverSecondDistance()
                + extraCharge);
    }

    private int calculateFareOverFirstDistance(int distance) {
        return (int) (Math.ceil((distance - FIRST_ADDITIONAL_INTERVAL) / FIRST_INTERVAL_UNIT) * OVER_FARE);
    }

    private int calculateFareOverSecondDistance() {
        return (int) (Math.ceil((distance - SECOND_ADDITIONAL_INTERVAL) / SECOND_INTERVAL_UNIT) * OVER_FARE);
    }

    public int getDistance() {
        return distance;
    }

    public Set<Long> getLineIds() {
        return lineIds;
    }

    public List<Long> getStationIds() {
        return stationIds;
    }
}
