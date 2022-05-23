package wooteco.subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.exception.ClientException;

import java.util.*;

public class Path {

    private final List<SectionWeightEdge> edges;
    private final int distance;

    private Path(List<SectionWeightEdge> edges, int distance) {
        this.edges = edges;
        this.distance = distance;
    }

    public static Path of(List<Section> sections, Long source, Long target) {
        GraphPath path = initPathGraph(sections, gatherStationIds(sections), source, target);
        return new Path(path.getEdgeList(), (int) path.getWeight());
    }

    private static Set<Long> gatherStationIds(List<Section> sections) {
        Set<Long> ids = new HashSet<>();
        for (Section section : sections) {
            ids.add(section.getUpStationId());
            ids.add(section.getDownStationId());
        }
        return ids;
    }

    private static GraphPath initPathGraph(List<Section> sections, Set<Long> ids, Long source, Long target) {
        WeightedMultigraph<Long, SectionWeightEdge> graph = new WeightedMultigraph(SectionWeightEdge.class);
        for (Long id : ids) {
            graph.addVertex(id);
        }

        for (Section section : sections) {
            graph.addEdge(section.getUpStationId(), section.getDownStationId(),
                    new SectionWeightEdge(section.getLineId(), section.getUpStationId(), section.getDownStationId(), section.getDistance()));
        }
        return new DijkstraShortestPath(graph).getPath(source, target);
    }

    public double calculateFare(List<Line> lines, Long age) {
        int lineExtraFare = findMostExpensiveExtraFare(lines);
        Fare fare = Fare.find(distance);
        return Age.find(age).calc(lineExtraFare + fare.calc(distance));
    }

    private int findMostExpensiveExtraFare(List<Line> lines) {
        return edges.stream()
                .mapToInt(edges -> findExtraFare(lines, edges.getLineId()))
                .max()
                .orElseThrow(NoSuchElementException::new);
    }

    private int findExtraFare(List<Line> lines, Long id) {
        return lines.stream()
                .filter(line -> line.getId() == id)
                .findAny().orElseThrow(() -> new ClientException("존재하지 않은 line id입니다."))
                .getExtraFare();
    }

    public Set<Long> getStationIds() {
        Set<Long> stations = new LinkedHashSet<>();
        for (SectionWeightEdge edge : edges) {
            stations.add(edge.getUpStationId());
            stations.add(edge.getDownStationId());
        }
        return Collections.unmodifiableSet(stations);
    }

    public int getDistance() {
        return distance;
    }
}
