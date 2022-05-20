package wooteco.subway.domain.pricing;

import java.util.List;
import wooteco.subway.domain.Section;

public interface PricingStrategy {

    int calculateFee(List<Section> sections);
}
