package wooteco.subway.domain.path;

import java.util.List;

import wooteco.subway.domain.line.Line;

public interface SubwayGraph {

    Path calculateShortestPath(List<Line> lines, long source , long target);
}
