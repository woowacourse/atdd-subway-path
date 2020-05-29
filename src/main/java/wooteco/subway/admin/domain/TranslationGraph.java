package wooteco.subway.admin.domain;

import java.util.List;

import wooteco.subway.admin.dto.GraphResultResponse;

public interface TranslationGraph {
    GraphResultResponse findShortestPath(List<Line> lines, Long source, Long target, CriteriaType type);
}
