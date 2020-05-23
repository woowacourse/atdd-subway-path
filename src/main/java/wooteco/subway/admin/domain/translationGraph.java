package wooteco.subway.admin.domain;

import wooteco.subway.admin.dto.GraphResultResponse;

public interface translationGraph {
    GraphResultResponse findShortestPath(Long source, Long target);
}
