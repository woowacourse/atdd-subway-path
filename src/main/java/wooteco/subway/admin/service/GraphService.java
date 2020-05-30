package wooteco.subway.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.CriteriaType;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.SubwayGraph;
import wooteco.subway.admin.domain.TranslationGraph;
import wooteco.subway.admin.dto.GraphResultResponse;

@Service
public class GraphService {
    private final TranslationGraph subwayGraph;

    public GraphService(SubwayGraph subwayGraph) {
        this.subwayGraph = subwayGraph;
    }

    public GraphResultResponse findPath(List<Line> lines, Long source, Long target, CriteriaType type) {
        return subwayGraph.findShortestPath(lines, source, target, type);
    }
}
