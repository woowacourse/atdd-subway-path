package wooteco.subway.domain;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Path {

    private final Lines lines;
    private final DijkstraShortestPath<Long, DefaultWeightedEdge> shortestPath;


    public Path(Lines lines) {
        this.lines = lines;
        this.shortestPath = new DijkstraShortestPath<>(multiGraph(lines));
    }

    private WeightedMultigraph<Long, DefaultWeightedEdge> multiGraph(Lines lines) {

        WeightedMultigraph<Long, DefaultWeightedEdge> graph
            = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        lines.allStations().stream().map(Station::getId).forEach(graph::addVertex);
        lines.allSections().forEach(section ->
            graph.setEdgeWeight(graph.addEdge(section.getUpStation().getId(), section.getDownStation().getId()), section.getDistance())
        );

        return graph;
    }

    public List<Station> shortestPath(Long sourceStationId, Long targetStationId) {
        final List<Long> stationIds =
            shortestPath.getPath(sourceStationId, targetStationId).getVertexList();
        return stationIds.stream().map(lines::stationById).collect(Collectors.toList());
    }

    public int distance(Long sourceStationId, Long targetStationId) {
        return (int) shortestPath.getPath(sourceStationId, targetStationId).getWeight();
    }
}
