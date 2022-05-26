package wooteco.subway.domain.strategy;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import wooteco.subway.domain.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DijkstraStrategy implements ShortestPathStrategy {

    @Override
    public Optional<Path> findPath(final Station source, final Station target, final Sections sections) {
        final Optional<GraphPath<Station, SubwayPathEdge>> path = createPath(source, target, sections);
        if (path.isEmpty()) {
            return Optional.empty();
        }

        final GraphPath<Station, SubwayPathEdge> graphPath = path.get();
        final int distance = (int) graphPath.getWeight();

        return Optional.of(new Path(graphPath.getVertexList(), getCrossedLines(graphPath), distance));
    }

    private Optional<GraphPath<Station, SubwayPathEdge>> createPath(final Station source,
                                                                    final Station target,
                                                                    final Sections sections) {
        final WeightedMultigraph<Station, SubwayPathEdge> graph =
                new WeightedMultigraph<>(SubwayPathEdge.class);
        final DijkstraShortestPath<Station, SubwayPathEdge> dijkstraShortestPath =
                new DijkstraShortestPath<>(initializeGraph(graph, sections));

        return Optional.ofNullable(dijkstraShortestPath.getPath(source, target));
    }

    private WeightedMultigraph<Station, SubwayPathEdge> initializeGraph(
            final WeightedMultigraph<Station, SubwayPathEdge> graph, final Sections sections) {

        for (Station station : sections.extractStations()) {
            graph.addVertex(station);
        }

        for (Section section : sections.getSections()) {
            graph.addEdge(section.getUpStation(), section.getDownStation(), SubwayPathEdge.from(section));
        }

        return graph;
    }

    private List<Line> getCrossedLines(final GraphPath<Station, SubwayPathEdge> graphPath) {
        return graphPath.getEdgeList()
                .stream()
                .map(SubwayPathEdge::getLine)
                .distinct()
                .collect(Collectors.toList());
    }
}
