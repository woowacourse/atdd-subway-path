package wooteco.subway.domain.pricing;

import wooteco.subway.domain.Fare;
import wooteco.subway.domain.FareCacluateSpecification;

public interface PricingStrategy {

    Fare calculateFare(FareCacluateSpecification specification);
}
