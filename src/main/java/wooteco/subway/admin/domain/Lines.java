package wooteco.subway.admin.domain;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import wooteco.subway.admin.domain.path.Graph;
import wooteco.subway.admin.domain.path.GraphStrategy;
import wooteco.subway.admin.domain.path.PathType;
import wooteco.subway.admin.domain.path.SubwayPath;

public class Lines {
    private List<Line> lines;

    private Lines(List<Line> lines) {
        this.lines = lines;
    }

    public static Lines of(List<Line> lines) {
        return new Lines(lines);
    }

    public SubwayPath findPath(GraphStrategy graphStrategy, Long sourceId, Long targetId,
        PathType pathType) {
        Graph graph = graphStrategy.makeGraph(findVertexes(), findEdges(), pathType);
        return graph.findPath(sourceId, targetId);
    }

    private List<Long> findVertexes() {
        return lines.stream()
            .map(Line::getLineStationsId)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    private List<LineStation> findEdges() {
        return lines.stream()
            .map(Line::getStations)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }
}
