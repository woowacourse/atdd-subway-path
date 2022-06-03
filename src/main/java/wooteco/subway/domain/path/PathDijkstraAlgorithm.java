package wooteco.subway.domain.path;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Stations;
import wooteco.subway.exception.DataNotExistException;
import wooteco.subway.exception.SubwayException;

public class PathDijkstraAlgorithm {

    private final WeightedMultigraph<Long, SubwayPathEdge> graph;

    public PathDijkstraAlgorithm(WeightedMultigraph<Long, SubwayPathEdge> graph) {
        this.graph = graph;
    }

    public static PathDijkstraAlgorithm of(List<Section> sections, Stations stations) {
        WeightedMultigraph<Long, SubwayPathEdge> graph = new WeightedMultigraph<>(SubwayPathEdge.class);
        setGraph(sections, stations, graph);
        return new PathDijkstraAlgorithm(graph);
    }

    private static void setGraph(List<Section> sections, Stations stations,
                                 WeightedMultigraph<Long, SubwayPathEdge> graph) {
        addVertexes(stations.getStationIds(), graph);
        setEdgeWeights(sections, graph);
    }

    private static void addVertexes(List<Long> stationIds, WeightedMultigraph<Long, SubwayPathEdge> graph) {
        for (Long stationId : stationIds) {
            graph.addVertex(stationId);
        }
    }

    private static void setEdgeWeights(List<Section> sections, WeightedMultigraph<Long, SubwayPathEdge> graph) {
        for (Section section : sections) {
            graph.addEdge(section.getUpStationId(), section.getDownStationId(), new SubwayPathEdge(section));
        }
    }

    public Path findPath(Long source, Long target) {
        validateStations(source, target);
        GraphPath<Long, SubwayPathEdge> graphPath = getGraphPath(source, target);
        return new Path(graphPath.getVertexList(), (int) graphPath.getWeight(), getUsedLineIds(graphPath));
    }

    private void validateStations(Long source, Long target) {
        validateStationExist(source);
        validateStationExist(target);
        validateStationSame(source, target);
    }

    private void validateStationExist(Long stationId) {
        if (!graph.containsVertex(stationId)) {
            throw new DataNotExistException("존재하지 않는 역입니다.");
        }
    }

    private void validateStationSame(Long source, Long target) {
        if (source.equals(target)) {
            throw new SubwayException("출발역과 도착역이 같을 수 없습니다.");
        }
    }

    private GraphPath<Long, SubwayPathEdge> getGraphPath(Long source, Long target) {
        DijkstraShortestPath<Long, SubwayPathEdge> path = new DijkstraShortestPath<>(graph);
        return Optional.ofNullable(path.getPath(source, target))
                .orElseThrow(() -> new SubwayException("경로가 존재하지 않습니다."));
    }

    private Set<Long> getUsedLineIds(GraphPath<Long, SubwayPathEdge> graphPath) {
        return graphPath.getEdgeList()
                .stream()
                .map(SubwayPathEdge::getSection)
                .map(Section::getLineId)
                .collect(Collectors.toSet());
    }
}
