package wooteco.subway.domain.pricing.distancepricing;

import wooteco.subway.domain.Fare;

public interface PricingBySection {

    Fare calculateFare(int distance);
}
