package wooteco.subway.domain.path;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.exception.SectionNotFoundException;

@Component
public class ShortestPathCalculator {

    public List<ShortestPathEdge> findEdges(final Station startStation, final Station endStation,
            final Sections sections) {
        final DijkstraShortestPath<Station, ShortestPathEdge> graph = createDijkstraGraph(sections);
        try {
            return graph.getPath(startStation, endStation).getEdgeList();
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new SectionNotFoundException();
        }
    }

    public Path findPath(final Station startStation, final Station endStation, final Sections sections) {
        final DijkstraShortestPath<Station, ShortestPathEdge> graphPath = createDijkstraGraph(sections);
        final int distance = calculateMinDistance(startStation, endStation, graphPath);
        final List<Station> shortestStations = findShortestStations(startStation, endStation, graphPath);
        return new Path(shortestStations, distance);
    }

    private DijkstraShortestPath<Station, ShortestPathEdge> createDijkstraGraph(final Sections sections) {
        final WeightedMultigraph<Station, ShortestPathEdge> graph = new WeightedMultigraph<>(
                ShortestPathEdge.class);
        for (Station station : findAllStationByDistinct(sections)) {
            graph.addVertex(station);
        }
        for (Section section : sections.getValues()) {
            graph.addEdge(section.getUpStation(), section.getDownStation(),
                    new ShortestPathEdge(section.getLineId(), section.getDistance()));
        }
        return new DijkstraShortestPath<>(graph);
    }

    private static Set<Station> findAllStationByDistinct(final Sections sections) {
        Set<Station> stations = new HashSet<>();
        stations.addAll(sections.findUpStations());
        stations.addAll(sections.findDownStations());
        return stations;
    }

    private int calculateMinDistance(final Station startStation, final Station endStation,
            final DijkstraShortestPath<Station, ShortestPathEdge> graph) {
        try {
            return (int) graph.getPath(startStation, endStation).getWeight();
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new SectionNotFoundException();
        }
    }

    private List<Station> findShortestStations(final Station startStation, final Station endStation,
            final DijkstraShortestPath<Station, ShortestPathEdge> graph) {
        try {
            return graph.getPath(startStation, endStation).getVertexList();
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new SectionNotFoundException();
        }
    }
}
