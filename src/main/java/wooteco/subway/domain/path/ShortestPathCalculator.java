package wooteco.subway.domain.path;

import java.util.List;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.exception.SectionNotFoundException;

@Component
public class ShortestPathCalculator implements PathCalculator {

    @Override
    public int calculateShortestDistance(final Sections sections, final Path path) {
        ShortestPathAlgorithm<Station, ShortestPathEdge> algorithm = initializePath(sections);
        try {
            return (int) algorithm.getPath(path.getStartStation(), path.getEndStation()).getWeight();
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new SectionNotFoundException();
        }
    }

    @Override
    public List<Station> calculateShortestStations(final Sections sections, final Path path) {
        ShortestPathAlgorithm<Station, ShortestPathEdge> algorithm = initializePath(sections);
        try {
            return algorithm.getPath(path.getStartStation(), path.getEndStation()).getVertexList();
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new SectionNotFoundException();
        }
    }

    @Override
    public List<ShortestPathEdge> findPassedEdges(final Sections sections, final Path path) {
        ShortestPathAlgorithm<Station, ShortestPathEdge> algorithm = initializePath(sections);
        try {
            return algorithm.getPath(path.getStartStation(), path.getEndStation()).getEdgeList();
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
