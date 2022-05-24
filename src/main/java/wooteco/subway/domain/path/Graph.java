package wooteco.subway.domain.path;

import java.util.List;
import wooteco.subway.domain.element.Line;
import wooteco.subway.domain.element.Station;

public interface Graph {
    List<Station> getShortestRoute(Station source, Station target);

    int getShortestDistance(Station source, Station target);

    List<Line> getLines(Station source, Station target);
}
