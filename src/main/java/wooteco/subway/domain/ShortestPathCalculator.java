package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import wooteco.subway.exception.SectionNotFoundException;

@Component
public class ShortestPathCalculator implements PathCalculator {

    @Override
    public int calculateShortestDistance(final Sections sections,
                                         final Station startStation,
                                         final Station endStation) {
        ShortestPathAlgorithm<Station, ShortestPathEdge> algorithm = initializePath(sections);
        try {
            return (int) algorithm.getPath(startStation, endStation).getWeight();
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new SectionNotFoundException();
        }
    }

    @Override
    public List<Station> calculateShortestStations(final Sections sections,
                                                   final Station startStation,
                                                   final Station endStation) {
        ShortestPathAlgorithm<Station, ShortestPathEdge> algorithm = initializePath(sections);
        try {
            return algorithm.getPath(startStation, endStation).getVertexList();
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new SectionNotFoundException();
        }
    }

    private DijkstraShortestPath<Station, ShortestPathEdge> initializePath(final Sections sections) {
        WeightedMultigraph<Station, ShortestPathEdge> graph
                = new WeightedMultigraph<>(ShortestPathEdge.class);

        for (Station station : sections.getAllStations()) {
            graph.addVertex(station);
        }
        for (Section section : sections.getValues()) {
            assignWeight(graph, section);
        }

        return new DijkstraShortestPath<>(graph);
    }

    private void assignWeight(final WeightedMultigraph<Station, ShortestPathEdge> graph,
                              final Section section) {
        graph.addEdge(section.getUpStation(),
                section.getDownStation(),
                new ShortestPathEdge(section.getLineId(),
                        section.getDistance()));
    }
}
