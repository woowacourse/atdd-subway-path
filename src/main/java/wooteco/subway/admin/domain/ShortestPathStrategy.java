package wooteco.subway.admin.domain;

import wooteco.subway.admin.dto.FindPathRequest;

public interface ShortestPathStrategy {
    SubwayPath findShortestPath(Lines lines, Stations stations, FindPathRequest FindPathRequest);
}
