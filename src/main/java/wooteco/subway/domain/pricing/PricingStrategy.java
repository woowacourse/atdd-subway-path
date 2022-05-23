package wooteco.subway.domain.pricing;

import wooteco.subway.domain.FareCacluateSpecification;

public interface PricingStrategy {

    int calculateFee(FareCacluateSpecification specification);
}
