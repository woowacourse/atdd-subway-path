package wooteco.subway.domain.path;

import java.util.List;
import java.util.Optional;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

public class PathFinder {

    final private DijkstraShortestPath<Station, PathEdge> dijkstraShortestPath;

    public PathFinder(final List<Station> stations, final List<Section> sections) {
        final WeightedMultigraph<Station, PathEdge> graph = new WeightedMultigraph(PathEdge.class);
        addVertex(stations, graph);
        addEdge(sections, graph);
        this.dijkstraShortestPath = new DijkstraShortestPath(graph);
    }

    private void addVertex(final List<Station> stations, final WeightedMultigraph<Station, PathEdge> graph) {
        for (final Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void addEdge(final List<Section> sections, final WeightedMultigraph<Station, PathEdge> graph) {
        for (final Section section : sections) {
            graph.addEdge(section.getUpStation(), section.getDownStation(), PathEdge.from(section));
            graph.addEdge(section.getDownStation(), section.getUpStation(), PathEdge.from(section));
        }
    }

    public Path find(final Station source, final Station target) {
        validateSameStation(source, target);
        final GraphPath<Station, PathEdge> graphPath = dijkstraShortestPath.getPath(source, target);
        validateGraphPath(graphPath);
        int maxExtraFare = findMaxExtraFare(graphPath);
        return new Path(graphPath.getVertexList(), (int) graphPath.getWeight(), maxExtraFare);
    }

    private int findMaxExtraFare(GraphPath<Station, PathEdge> graphPath) {
        List<PathEdge> pathEdges = graphPath.getEdgeList();
        return pathEdges.stream()
                .map(PathEdge::getExtraFare)
                .max(Integer::compareTo)
                .orElseThrow(() -> new IllegalStateException("노선 추가 요금이 등록되지 않았습니다."));
    }

    private void validateGraphPath(GraphPath<Station, PathEdge> graphPath) {
        if (null == graphPath) {
            throw new IllegalArgumentException("경로를 찾을 수 없습니다.");
        }
    }

    private void validateSameStation(final Station source, final Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역은 같을 수 없습니다.");
        }
    }
}
