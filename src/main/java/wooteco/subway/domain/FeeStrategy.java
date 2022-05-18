package wooteco.subway.domain;

import java.util.List;

public interface FeeStrategy {

    int calculateFee(List<Section> sections);
}
