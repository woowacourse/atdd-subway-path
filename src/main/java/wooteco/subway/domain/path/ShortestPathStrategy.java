package wooteco.subway.domain.path;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.exception.SectionNotFoundException;

@Component
public class ShortestPathStrategy {

    public Path findPath(final Station startStation, final Station endStation, final Sections sections) {
        final DijkstraShortestPath<Station, DefaultWeightedEdge> graph = createDijkstraGraph(sections);
        final int distance = calculateMinDistance(startStation, endStation, graph);
        final List<Station> shortestStations = findShortestStations(startStation, endStation, graph);
        return new Path(shortestStations, distance);
    }

    private DijkstraShortestPath<Station, DefaultWeightedEdge> createDijkstraGraph(Sections sections) {
        final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(
                DefaultWeightedEdge.class);
        for (Station station : findAllStationByDistinct(sections)) {
            graph.addVertex(station);
        }
        for (Section section : sections.getValues()) {
            assignWeight(graph, section);
        }
        return new DijkstraShortestPath<>(graph);
    }

    private static Set<Station> findAllStationByDistinct(final Sections sections) {
        Set<Station> stations = new HashSet<>();
        stations.addAll(sections.findUpStations());
        stations.addAll(sections.findDownStations());
        return stations;
    }

    private static void assignWeight(final WeightedMultigraph<Station, DefaultWeightedEdge> graph,
            final Section section) {
        graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
    }

    public int calculateMinDistance(final Station startStation, final Station endStation,
            final DijkstraShortestPath<Station, DefaultWeightedEdge> graph) {
        try {
            return (int) graph.getPath(startStation, endStation).getWeight();
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new SectionNotFoundException();
        }
    }

    public List<Station> findShortestStations(final Station startStation, final Station endStation,
            final DijkstraShortestPath<Station, DefaultWeightedEdge> graph) {
        try {
            return graph.getPath(startStation, endStation).getVertexList();
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new SectionNotFoundException();
        }
    }
}
