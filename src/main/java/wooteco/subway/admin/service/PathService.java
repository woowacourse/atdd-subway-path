package wooteco.subway.admin.service;

import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.ShortestPathResponse;

public interface PathService {
	ShortestPathResponse findShortestPath(PathRequest request);
}
