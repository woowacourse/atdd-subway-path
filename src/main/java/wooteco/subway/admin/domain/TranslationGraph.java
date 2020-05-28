package wooteco.subway.admin.domain;

import wooteco.subway.admin.dto.GraphResultResponse;

public interface TranslationGraph {
    GraphResultResponse findShortestPath(Long source, Long target);
}
