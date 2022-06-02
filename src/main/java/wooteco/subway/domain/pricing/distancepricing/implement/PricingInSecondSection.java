package wooteco.subway.domain.pricing.distancepricing.implement;

import wooteco.subway.domain.Fare;
import wooteco.subway.domain.pricing.distancepricing.PricingBySection;

public final class PricingInSecondSection implements PricingBySection {

    private static final int UPPER_LIMIT = 50;
    private static final int UNDER_LIMIT = 10;
    private static final int UNIT_DISTANCE = 5;
    private static final Fare UNIT_MONEY = new Fare(100);

    public PricingInSecondSection() {}

    @Override
    public Fare calculateFare(int distance) {
        distance = adjustDistance(distance);
        if (distance == 0) {
            return new Fare(0);
        }
        return UNIT_MONEY.mul(distance / UNIT_DISTANCE + distance % UNIT_DISTANCE);
    }

    private int adjustDistance(int distance) {
        if (distance > UPPER_LIMIT) {
            return UPPER_LIMIT - UNDER_LIMIT;
        }
        if (distance > UNDER_LIMIT) {
            return distance - UNDER_LIMIT;
        }
        return 0;
    }
}
