package wooteco.subway.domain.pricing.distancepricing.implement;

import wooteco.subway.domain.pricing.distancepricing.PricingBySection;

public final class PricingInFirstSection extends PricingBySection {

    private static final int BASIC_FEE = 1250;
    private static final PricingInFirstSection STRATEGY = new PricingInFirstSection();

    private PricingInFirstSection() {}

    public static PricingInFirstSection of() {
        return STRATEGY;
    }

    @Override
    public int calculateFee(int distance) {
        return BASIC_FEE;
    }
}
