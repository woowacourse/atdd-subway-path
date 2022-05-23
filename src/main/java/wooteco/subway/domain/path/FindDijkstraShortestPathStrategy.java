package wooteco.subway.domain.path;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.Sections;
import wooteco.subway.domain.station.Station;

public class FindDijkstraShortestPathStrategy implements FindPathStrategy {

    @Override
    public Path findPath(final Station source, final Station target, final Sections sections) {
        sections.checkExistStations(source, target);
        WeightedMultigraph<Station, SubwayPathEdge> graph = new WeightedMultigraph<>(SubwayPathEdge.class);
        addVertexStation(sections, graph);
        addEdgeWeightStation(sections, graph);

        GraphPath<Station, SubwayPathEdge> shortestPath = calculateShortestPath(source, target, graph);
        return new Path(shortestPath.getVertexList(), pathUsedLines(shortestPath), (int) shortestPath.getWeight());
    }

    private Set<Line> pathUsedLines(final GraphPath<Station, SubwayPathEdge> shortestPath) {
        return shortestPath.getEdgeList()
                .stream()
                .map(SubwayPathEdge::getLine)
                .collect(Collectors.toSet());
    }

    private void addVertexStation(final Sections sections,
                                  final WeightedMultigraph<Station, SubwayPathEdge> graph) {
        List<Station> allStations = sections.getAllStations();
        for (Station station : allStations) {
            graph.addVertex(station);
        }
    }

    private void addEdgeWeightStation(final Sections sections,
                                      final WeightedMultigraph<Station, SubwayPathEdge> graph) {
        List<Section> allSections = sections.getSections();
        for (Section section : allSections) {
            graph.addEdge(section.getUpStation(), section.getDownStation(), SubwayPathEdge.from(section));
        }
    }

    private GraphPath<Station, SubwayPathEdge> calculateShortestPath(final Station source, final Station target,
                                                                          final WeightedMultigraph<Station, SubwayPathEdge> graph) {
        DijkstraShortestPath<Station, SubwayPathEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return Optional.ofNullable(dijkstraShortestPath.getPath(source, target))
                .orElseThrow(this::notFoundPathException);
    }
}
