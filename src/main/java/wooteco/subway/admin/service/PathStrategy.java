package wooteco.subway.admin.service;

import java.util.List;

import wooteco.subway.admin.domain.CriteriaType;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.dto.GraphResultResponse;

public interface PathStrategy {
    GraphResultResponse getPath(List<Line> lines, Long source, Long target, CriteriaType type);
}
