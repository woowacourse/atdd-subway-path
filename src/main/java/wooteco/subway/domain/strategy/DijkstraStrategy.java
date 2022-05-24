package wooteco.subway.domain.strategy;

import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;

@Component
public class DijkstraStrategy implements ShortestPathStrategy {

    @Override
    public Path findPath(final Station source, final Station target, final Sections sections) {
        final WeightedMultigraph<Station, ShortestPathEdge> graph =
            new WeightedMultigraph<>(ShortestPathEdge.class);

        final DijkstraShortestPath<Station, ShortestPathEdge> dijkstraShortestPath =
            new DijkstraShortestPath<>(initializeGraph(graph, sections));

        final GraphPath<Station, ShortestPathEdge> path = dijkstraShortestPath.getPath(source, target);
        final List<Long> transferLineIds = path.getEdgeList()
            .stream()
            .map(ShortestPathEdge::getLineId)
            .collect(Collectors.toList());

        return new Path(path.getVertexList(), transferLineIds, (int)path.getWeight());
    }

    private WeightedMultigraph<Station, ShortestPathEdge> initializeGraph(
        final WeightedMultigraph<Station, ShortestPathEdge> graph,
        final Sections sections
    ) {
        for (Station station : sections.extractStations()) {
            graph.addVertex(station);
        }

        for (Section section : sections.getSections()) {
            final ShortestPathEdge edge = new ShortestPathEdge(section.getLineId(), section.getDistance());
            graph.addEdge(section.getUpStation(), section.getDownStation(), edge);
        }

        return graph;
    }
}
