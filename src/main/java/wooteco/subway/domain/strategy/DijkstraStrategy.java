package wooteco.subway.domain.strategy;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.*;
import wooteco.subway.exception.PathNotExistsException;
import java.util.Optional;

public class DijkstraStrategy implements ShortestPathStrategy {

    @Override
    public Path findPath(final Station source, final Station target, final Sections sections) {
        final WeightedMultigraph<Station, DefaultWeightedEdge> graph =
                new WeightedMultigraph<>(DefaultWeightedEdge.class);
        final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath =
                new DijkstraShortestPath<>(initializeGraph(graph, sections));

        final GraphPath<Station, DefaultWeightedEdge> path =
                Optional.ofNullable(dijkstraShortestPath.getPath(source, target))
                        .orElseThrow(PathNotExistsException::new);

        return new Path(path.getVertexList(), path.getWeight(), Fare.from(path.getWeight()));
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
