package wooteco.subway.domain;

import java.util.List;

public interface PricingStrategy {

    int calculateFee(List<Section> sections);
}
