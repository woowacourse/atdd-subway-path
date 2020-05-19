package wooteco.subway.admin.domain.strategy;

import java.util.List;
import java.util.Map;

import org.jgrapht.GraphPath;

import wooteco.subway.admin.domain.Edge;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathRequest;

public interface AlgorithmStrategy {
    GraphPath<Station, Edge> getPath(Map<Long, Station> stations, List<Line> lines,
        PathRequest pathRequest);
}
