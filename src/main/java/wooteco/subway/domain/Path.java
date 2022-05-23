package wooteco.subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Path {

    private static final String NOT_EXIST_STATION = "출발지, 도착지 모두 존재해야 됩니다.";
    private static final String NO_REACHABLE = "출발지에서 도착지로 갈 수 없습니다.";
    private static final int DEFAULT_EXTRA_FARE = 0;

    private final WeightedMultigraph<Long, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
    private final Stations stations;
    private final Sections sections;
    private final Lines lines;

    public Path(Stations stations, Sections sections, Lines lines) {
        this.stations = stations;
        this.sections = sections;
        this.lines = lines;
        addVertexToGraph(stations);
        addEdgeToGraph(sections);
    }

    private void addEdgeToGraph(Sections sections) {
        for (Section section : sections.getSections()) {
            graph.setEdgeWeight(
                    graph.addEdge(section.getUpStationId(), section.getDownStationId())
                            .addInformation(section.getLineId(), section.getDistance()),
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

    public int calculateExtraFare(long source, long target) {
        Optional<GraphPath<Long, SectionEdge>> path = makeGraphPath(source, target);
        List<SectionEdge> edges = path.orElseThrow(() -> new IllegalArgumentException(NO_REACHABLE))
                .getEdgeList();

        return edges.stream()
                .mapToInt(this::getExtraFare)
                .max()
                .orElse(DEFAULT_EXTRA_FARE);
    }

    private int getExtraFare(SectionEdge edge) {
        long source = graph.getEdgeSource(edge);
        long target = graph.getEdgeTarget(edge);
        int distance = edge.getDistance();
        int extraFare = calculateMinExtraFare(source, target, distance);

        return extraFare;
    }

    private int calculateMinExtraFare(long source, long target, int distance) {
        return sections.getSections().stream()
                .filter(section -> section.isContainStationId(source) && section.isContainStationId(target))
                .filter(section -> section.isSameDistance(distance))
                .map(section -> lines.getLineByLineId(section.getLineId()))
                .mapToInt(Line::getExtraFare)
                .min()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 구간입니다."));
    }
}
