package wooteco.subway.domain.pricing.distancepricing.implement;

import wooteco.subway.domain.pricing.distancepricing.PricingBySection;

public final class PricingInFirstSection extends PricingBySection {

    private static final int BASIC_FEE = 1250;

    public PricingInFirstSection() {}

    @Override
    public int calculateFee(int distance) {
        return BASIC_FEE;
    }
}
