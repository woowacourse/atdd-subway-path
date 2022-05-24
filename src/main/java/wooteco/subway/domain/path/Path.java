package wooteco.subway.domain.path;

import java.util.List;
import java.util.Set;

public class Path {

    private static final int BASIC_FARE = 1250;
    private static final int ADDITIONAL_FARE = 100;
    private static final int FIRST_RESTRICTION_DISTANCE = 10;
    private static final int SECOND_RESTRICTION_DISTANCE = 50;
    private static final int FIRST_DISTANCE_UNIT = 5;
    private static final int SECOND_DISTANCE_UNIT = 8;

    private final List<Long> stationIds;
    private final int distance;
    private final Set<Long> usedLineIds;

    public Path(List<Long> stationIds, int distance, Set<Long> usedLineIds) {
        this.stationIds = stationIds;
        this.distance = distance;
        this.usedLineIds = usedLineIds;
    }

    public int calculateFare(int age, int extraFare) {
        AgeDiscountStrategy strategy = AgeDiscountStrategy.from(age);
        return strategy.discount(calculateDefaultFare() + extraFare);
    }

    private double calculateDefaultFare() {
        return BASIC_FARE + calculateFareOverFirstRestrictionDistance() + calculateFareOverSecondRestrictionDistance();
    }

    private double calculateFareOverFirstRestrictionDistance() {
        if (distance <= SECOND_RESTRICTION_DISTANCE) {
            return calculateAdditionalFare(distance, FIRST_RESTRICTION_DISTANCE, FIRST_DISTANCE_UNIT);
        }
        return calculateAdditionalFare(SECOND_RESTRICTION_DISTANCE, FIRST_RESTRICTION_DISTANCE, FIRST_DISTANCE_UNIT);
    }

    private double calculateFareOverSecondRestrictionDistance() {
        return calculateAdditionalFare(distance, SECOND_RESTRICTION_DISTANCE, SECOND_DISTANCE_UNIT);
    }

    private double calculateAdditionalFare(int distance, int restrictionDistance, double standardDistance) {
        if (distance <= restrictionDistance) {
            return 0;
        }
        return Math.ceil((distance - restrictionDistance) / standardDistance) * ADDITIONAL_FARE;
    }

    public List<Long> getStationIds() {
        return stationIds;
    }

    public int getDistance() {
        return distance;
    }

    public Set<Long> getUsedLineIds() {
        return usedLineIds;
    }
}
