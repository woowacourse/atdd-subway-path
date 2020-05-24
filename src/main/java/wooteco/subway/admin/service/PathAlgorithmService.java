package wooteco.subway.admin.service;

import wooteco.subway.admin.domain.Stations;
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.ShortestPathResponse;

public interface PathAlgorithmService {
    ShortestPathResponse findShortestPath(PathRequest pathRequest, Stations stations);
}
