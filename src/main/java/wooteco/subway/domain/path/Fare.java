package wooteco.subway.domain.path;

import java.util.Objects;
import wooteco.subway.exception.SubwayException;

public class Fare {

    private static final int BASIC_FARE = 1250;
    private static final int ADDITIONAL_FARE_PER_UNIT = 100;
    private static final int FIRST_RESTRICTION_DISTANCE = 10;
    private static final int SECOND_RESTRICTION_DISTANCE = 50;
    private static final int FIRST_DISTANCE_UNIT = 5;
    private static final int SECOND_DISTANCE_UNIT = 8;

    private final int fare;

    public Fare(int fare) {
        validateNotNegative(fare);
        this.fare = fare;
    }

    private void validateNotNegative(int fare) {
        if (fare < 0) {
            throw new SubwayException("요금은 음수가 될 수 없습니다.");
        }
    }

    public Fare addDefault(int distance) {
        return new Fare(fare
                + BASIC_FARE
                + calculateFareOverFirstRestrictionDistance(distance)
                + calculateFareOverSecondRestrictionDistance(distance));
    }

    private int calculateFareOverFirstRestrictionDistance(int distance) {
        if (distance <= SECOND_RESTRICTION_DISTANCE) {
            return calculateAdditionalFare(distance, FIRST_RESTRICTION_DISTANCE, FIRST_DISTANCE_UNIT);
        }
        return calculateAdditionalFare(SECOND_RESTRICTION_DISTANCE, FIRST_RESTRICTION_DISTANCE, FIRST_DISTANCE_UNIT);
    }

    private int calculateFareOverSecondRestrictionDistance(int distance) {
        return calculateAdditionalFare(distance, SECOND_RESTRICTION_DISTANCE, SECOND_DISTANCE_UNIT);
    }

    private int calculateAdditionalFare(int distance, int restrictionDistance, double standardDistance) {
        if (distance <= restrictionDistance) {
            return 0;
        }
        return (int) Math.ceil((distance - restrictionDistance) / standardDistance) * ADDITIONAL_FARE_PER_UNIT;
    }

    public Fare discountByAge(AgeBoundary ageBoundary) {
        return new Fare(ageBoundary.discountFare(fare));
    }

    public int getFare() {
        return fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fare fare1 = (Fare) o;
        return fare == fare1.fare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }
}
