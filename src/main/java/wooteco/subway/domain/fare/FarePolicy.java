package wooteco.subway.domain.fare;

import java.util.List;
import wooteco.subway.domain.Line;

public interface FarePolicy {

    Fare calculateFare(int distance, List<Line> line);
}
