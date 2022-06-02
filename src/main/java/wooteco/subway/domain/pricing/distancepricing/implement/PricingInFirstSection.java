package wooteco.subway.domain.pricing.distancepricing.implement;

import wooteco.subway.domain.Fare;
import wooteco.subway.domain.pricing.distancepricing.PricingBySection;

public final class PricingInFirstSection implements PricingBySection {

    private static final Fare BASIC_FEE = new Fare(1250);

    public PricingInFirstSection() {}

    @Override
    public Fare calculateFare(int distance) {
        return BASIC_FEE;
    }
}
