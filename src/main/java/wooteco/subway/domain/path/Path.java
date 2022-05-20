package wooteco.subway.domain.path;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Stations;
import wooteco.subway.exception.DataNotExistException;
import wooteco.subway.exception.SubwayException;

public class Path {

    private final List<Long> path;
    private final int distance;

    private Path(List<Long> path, int distance) {
        this.path = path;
        this.distance = distance;
    }

    public static Path of(Long source, Long target, List<Section> sections, Stations stations) {
        validateStationSame(source, target);
        validateStationExist(stations, source);
        validateStationExist(stations, target);

        WeightedMultigraph<Long, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertexes(stations.getStationIds(), graph);
        setEdgeWeights(sections, graph);

        GraphPath<Long, DefaultWeightedEdge> shortestPath = findPath(graph, source, target);
        return new Path(shortestPath.getVertexList(), (int) shortestPath.getWeight());
    }

    private static void validateStationSame(Long source, Long target) {
        if (source.equals(target)) {
            throw new SubwayException("출발역과 도착역이 같을 수 없습니다.");
        }
    }

    private static void validateStationExist(Stations stations, Long stationId) {
        if (!stations.contains(stationId)) {
            throw new DataNotExistException("존재하지 않는 역입니다.");
        }
    }

    private static void addVertexes(List<Long> stationIds, WeightedMultigraph<Long, DefaultWeightedEdge> graph) {
        for (Long stationId : stationIds) {
            graph.addVertex(stationId);
        }
    }

    private static void setEdgeWeights(List<Section> sections, WeightedMultigraph<Long, DefaultWeightedEdge> graph) {
        for (Section section : sections) {
            graph.setEdgeWeight(
                    graph.addEdge(section.getUpStationId(), section.getDownStationId()),
                    section.getDistance());
        }
    }

    private static GraphPath<Long, DefaultWeightedEdge> findPath(WeightedMultigraph<Long, DefaultWeightedEdge> graph,
                                                                 Long source, Long target) {
        return Optional.ofNullable(new DijkstraShortestPath<>(graph).getPath(source, target))
                .orElseThrow(() -> new SubwayException("경로가 존재하지 않습니다."));
    }

    public List<Long> getPath() {
        return Collections.unmodifiableList(path);
    }

    public int getDistance() {
        return distance;
    }
}
