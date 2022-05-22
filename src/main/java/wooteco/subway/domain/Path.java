package wooteco.subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Path {

    private static final String NOT_EXIST_STATION = "출발지, 도착지 모두 존재해야 됩니다.";
    private static final String NO_REACHABLE = "출발지에서 도착지로 갈 수 없습니다.";

    private final WeightedMultigraph<Long, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
    private final Stations stations;

    public Path(Stations stations, Sections sections) {
        this.stations = stations;
        addVertexToGraph(stations);
        addEdgeToGraph(sections);
    }

    private void addEdgeToGraph(Sections sections) {
        for (Section section : sections.getSections()) {
            graph.setEdgeWeight(
                    graph.addEdge(section.getUpStationId(), section.getDownStationId()).addLineInformation(section.getLineId()),
                    section.getDistance()
            );
        }
    }

    private void addVertexToGraph(Stations stations) {
        for (Station station : stations.getStations()) {
            graph.addVertex(station.getId());
        }
    }

    public List<Station> calculateShortestPath(long source, long target) {
        Optional<GraphPath<Long, SectionEdge>> path = makeGraphPath(source, target);

        return path.orElseThrow(() -> new IllegalArgumentException(NO_REACHABLE))
                .getVertexList()
                .stream()
                .map(stations::getStationById)
                .collect(Collectors.toList());
    }

    public int calculateShortestDistance(Long source, Long target) {
        Optional<GraphPath<Long, SectionEdge>> path = makeGraphPath(source, target);

        return (int) path.orElseThrow(() -> new IllegalArgumentException(NO_REACHABLE)).getWeight();
    }

    private Optional<GraphPath<Long, SectionEdge>> makeGraphPath(Long source, Long target) {
        Optional<GraphPath<Long, SectionEdge>> path;

        try {
            DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
            path = Optional.ofNullable(
                    dijkstraShortestPath.getPath(source, target));
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(NOT_EXIST_STATION);
        }

        return path;
    }

    public List<Long> calculateShortestPathLines(long source, long target) {
        List<Long> lines = new ArrayList<>();
        Optional<GraphPath<Long, SectionEdge>> path = makeGraphPath(source, target);
        List<SectionEdge> edges = path.orElseThrow(() -> new IllegalArgumentException(NO_REACHABLE))
                .getEdgeList();

        for (SectionEdge edge : edges) {
            lines.add(edge.getLineId());
        }

        return lines;
    }
}
