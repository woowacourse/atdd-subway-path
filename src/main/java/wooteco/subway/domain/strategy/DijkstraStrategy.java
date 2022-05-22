package wooteco.subway.domain.strategy;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import wooteco.subway.domain.*;
import java.util.Optional;

@Component
public class DijkstraStrategy implements ShortestPathStrategy {

    @Override
    public Optional<Path> findPath(final Station source, final Station target, final Sections sections) {
        final WeightedMultigraph<Station, DefaultWeightedEdge> graph =
                new WeightedMultigraph<>(DefaultWeightedEdge.class);
        final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath =
                new DijkstraShortestPath<>(initializeGraph(graph, sections));

        final Optional<GraphPath<Station, DefaultWeightedEdge>> path =
                Optional.ofNullable(dijkstraShortestPath.getPath(source, target));

        if (path.isEmpty()) {
            return Optional.empty();
        }

        final GraphPath<Station, DefaultWeightedEdge> graphPath = path.get();
        final int distance = (int) graphPath.getWeight();

        return Optional.of(
                new Path(graphPath.getVertexList(), distance, Fare.from(distance))
        );
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> initializeGraph(
            final WeightedMultigraph<Station, DefaultWeightedEdge> graph, final Sections sections) {

        for (Station station : sections.extractStations()) {
            graph.addVertex(station);
        }

        for (Section section : sections.getSections()) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }

        return graph;
    }
}
