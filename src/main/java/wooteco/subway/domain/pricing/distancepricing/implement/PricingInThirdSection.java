package wooteco.subway.domain.pricing.distancepricing.implement;

import wooteco.subway.domain.pricing.distancepricing.PricingBySection;

public final class PricingInThirdSection implements PricingBySection {

    private static final int UNDER_LIMIT = 50;
    private static final int UNIT_DISTANCE = 8;
    private static final int UNIT_MONEY = 100;

    public PricingInThirdSection() {}

    @Override
    public int calculateFee(int distance) {
        distance = adjustDistance(distance);
        if (distance == 0) {
            return 0;
        }
        return UNIT_MONEY * (distance / UNIT_DISTANCE + distance % UNIT_DISTANCE);
    }

    private int adjustDistance(int distance) {
        if (distance > UNDER_LIMIT) {
            return distance - UNDER_LIMIT;
        }
        return 0;
    }
}
