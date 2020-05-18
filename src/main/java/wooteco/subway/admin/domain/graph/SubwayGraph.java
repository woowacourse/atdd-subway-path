package wooteco.subway.admin.domain.graph;

import wooteco.subway.admin.domain.Station;

public interface SubwayGraph {
    SubwayPath generatePath(Station source, Station target);
}
