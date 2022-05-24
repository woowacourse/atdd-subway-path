package wooteco.subway.domain.path;

import org.springframework.stereotype.Component;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;

import java.util.List;

public interface PathStrategy {
    Path findShortestPath(Station source, Station target, List<Line> lines);
}
