package wooteco.subway.domain.pricing.distancepricing.implement;

import wooteco.subway.domain.pricing.distancepricing.PricingBySection;

public final class PricingInFirstSection extends PricingBySection {

    private static final int BASIC_FEE = 1250;
    private static final PricingInFirstSection PRICING_IN_FIRST_SECTION = new PricingInFirstSection();

    private PricingInFirstSection() {}

    public static PricingInFirstSection of() {
        return PRICING_IN_FIRST_SECTION;
    }

    @Override
    public int calculateFee(int distance) {
        return BASIC_FEE;
    }
}
