package wooteco.subway.domain;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.exception.NoReachableStationException;
import wooteco.subway.exception.NotExistStationException;

public class Path {

    private static final String NOT_EXIST_STATION = "출발지, 도착지 모두 존재해야 됩니다.";
    private static final String NO_REACHABLE = "출발지에서 도착지로 갈 수 없습니다.";

    private final WeightedMultigraph<Long, ShortestPathEdge> graph = new WeightedMultigraph(
            ShortestPathEdge.class);

    public Path(List<Section> sections) {
        for (Section section : sections) {
            graph.addVertex(section.getUpStationId());
            graph.addVertex(section.getDownStationId());
            graph.addEdge(section.getUpStationId(), section.getDownStationId(),
                    new ShortestPathEdge(section.getLineId(),
                            section.getDistance()));
        }
    }

    public List<Long> calculateShortestPath(Long source, Long target) {
        Optional<GraphPath> path = makeGraphPath(source, target);

        return path.orElseThrow(() -> new NoReachableStationException(NO_REACHABLE)).getVertexList();
    }

    public int calculateShortestDistance(Long source, Long target) {
        Optional<GraphPath> path = makeGraphPath(source, target);

        return (int) path.orElseThrow(() -> new NoReachableStationException(NO_REACHABLE)).getWeight();
    }

    public List<ShortestPathEdge> getEdges(Long source, Long target) {
        Optional<GraphPath> path = makeGraphPath(source, target);
        return path.orElseThrow(() -> new NoReachableStationException(NO_REACHABLE)).getEdgeList();
    }

    // TODO: Section이 Line을 가지고 있지 않고 ID를 들고 있기 때문에 Dao를 한번 거쳐서야 도메인을 만들 수 있다. Section에서 객체를 가지고 있게 변경시켜보자.
    public List<Long> calculateExtraFare(Long source, Long target) {
        List<ShortestPathEdge> edges = getEdges(source, target);

        return edges.stream()
                .distinct()
                .map(shortestPathEdge -> shortestPathEdge.getLineId())
                .collect(Collectors.toList());
    }

    private Optional<GraphPath> makeGraphPath(Long source, Long target) {
        Optional<GraphPath> path;

        try {
            DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
            path = Optional.ofNullable(
                    dijkstraShortestPath.getPath(source, target));
        } catch (IllegalArgumentException exception) {
            throw new NotExistStationException(NOT_EXIST_STATION);
        }

        return path;
    }
}
