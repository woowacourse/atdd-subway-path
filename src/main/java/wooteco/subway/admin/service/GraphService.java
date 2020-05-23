package wooteco.subway.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.CriteriaType;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.SubwayGraph;
import wooteco.subway.admin.domain.translationGraph;
import wooteco.subway.admin.dto.GraphResultResponse;

@Service
public class GraphService {
    public GraphResultResponse findPath(List<Line> lines, Long source, Long target, CriteriaType type) {
        translationGraph subwayGraph = new SubwayGraph(lines, type);

        return subwayGraph.findShortestPath(source, target);
    }
}
