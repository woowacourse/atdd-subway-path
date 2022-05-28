package wooteco.subway.domain.age;

import wooteco.subway.domain.Fare;

public interface DiscountByAgePolicy {

    Fare apply(final Fare fare);
}
