package wooteco.subway.domain.pricing.distancepricing.implement;

import wooteco.subway.domain.pricing.distancepricing.PricingByDistance;

public final class PricingUnderTen extends PricingByDistance {

    private static final int BASIC_FEE = 1250;
    private static final PricingUnderTen pricingUnderTen = new PricingUnderTen();

    private PricingUnderTen() {}

    public static PricingUnderTen of() {
        return pricingUnderTen;
    }

    @Override
    public int calculateFee(int distance) {
        return BASIC_FEE;
    }
}
