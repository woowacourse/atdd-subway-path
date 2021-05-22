package wooteco.subway.line.domain;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.path.exception.PathException;
import wooteco.subway.station.domain.Station;

public class Path {
    private final Long source;
    private final Long target;

    public Path(Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    public GraphPath<Long, DefaultWeightedEdge> calculateShortestPath(Map<Long, Station> stationMap,
        List<Section> sections) {
        WeightedMultigraph<Long, DefaultWeightedEdge> stationGraph = new WeightedMultigraph<>(
            DefaultWeightedEdge.class);

        stationMap.keySet().forEach(stationGraph::addVertex);
        sections.forEach(section -> stationGraph.setEdgeWeight(
            stationGraph.addEdge(section.getUpStation().getId(), section.getDownStation().getId()),
            section.getDistance()));

        DijkstraShortestPath<Long, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(
            stationGraph);
        GraphPath<Long, DefaultWeightedEdge> shortestPath = dijkstraShortestPath
            .getPath(source, target);
        validatePathIsExist(shortestPath);
        return shortestPath;
    }

    private void validatePathIsExist(GraphPath<Long, DefaultWeightedEdge> path) {
        if (Objects.isNull(path)) {
            throw new PathException("두 역 사이에 존재하는 경로가 없습니다.");
        }
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }
}
