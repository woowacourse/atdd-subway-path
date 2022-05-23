package wooteco.subway.domain;

import java.util.List;
import java.util.Optional;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.section.Section;
import wooteco.subway.exception.SubwayException;

public class Path {

    private final DijkstraShortestPath<Long, ShortestPathEdge> dijkstraShortestPath;

    public Path(DijkstraShortestPath<Long, ShortestPathEdge> dijkstraShortestPath) {
        this.dijkstraShortestPath = dijkstraShortestPath;
    }

    public static Path of(List<Section> sections, List<Long> stationIds) {
        WeightedMultigraph<Long, ShortestPathEdge> graph = new WeightedMultigraph<>(ShortestPathEdge.class);
        addVertexes(stationIds, graph);
        setEdgeWeights(sections, graph);

        return new Path(new DijkstraShortestPath<>(graph));
    }

    private static void addVertexes(List<Long> stationIds, WeightedMultigraph<Long, ShortestPathEdge> graph) {
        for (Long stationId : stationIds) {
            graph.addVertex(stationId);
        }
    }

    private static void setEdgeWeights(List<Section> sections, WeightedMultigraph<Long, ShortestPathEdge> graph) {
        for (Section section : sections) {
            graph.addEdge(section.getUpStationId(), section.getDownStationId(),
                    new ShortestPathEdge(section.getLineId(), section.getDistance()));
        }
    }

    public List<Long> findPath(Long source, Long target) {
        return getPath(source, target).getVertexList();
    }

    public int findDistance(Long source, Long target) {
        return (int) getPath(source, target).getWeight();
    }

    private GraphPath<Long, ShortestPathEdge> getPath(Long source, Long target) {
        return Optional.ofNullable(dijkstraShortestPath.getPath(source, target))
                .orElseThrow(() -> new SubwayException("경로가 존재하지 않습니다."));
    }
}
