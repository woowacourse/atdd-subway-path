package wooteco.subway.domain.path.graph;

import java.util.List;

import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.path.Path;

public interface SubwayGraph {

    Path calculateShortestPath(List<Line> lines, long source , long target);
}
