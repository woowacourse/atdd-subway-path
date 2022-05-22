package wooteco.subway.domain.pricing.distancepricing.implement;

import wooteco.subway.domain.pricing.distancepricing.PricingByDistance;

public final class PricingTenToFifty extends PricingByDistance {

    private static final int UPPER_LIMIT = 50;
    private static final int UNDER_LIMIT = 10;
    private static final int UNIT_DISTANCE = 5;
    private static final int UNIT_MONEY = 100;
    private static final PricingTenToFifty pricingTenToFifty = new PricingTenToFifty();

    private PricingTenToFifty() {}

    public static PricingTenToFifty of() {
        return pricingTenToFifty;
    }

    @Override
    public int calculateFee(int distance) {
        distance = adjustDistance(distance);
        if (distance == 0) {
            return 0;
        }
        if (distance % UNIT_DISTANCE == 0) {
            return (distance / UNIT_DISTANCE) * UNIT_MONEY;
        }
        return (distance / UNIT_DISTANCE) * UNIT_MONEY + UNIT_MONEY;
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
