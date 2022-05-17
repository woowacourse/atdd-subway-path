package wooteco.subway.domain.strategy;

import java.util.List;
import java.util.Optional;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.exception.NotFoundException;

public class FindDijkstraShortestPathStrategy implements FindPathStrategy {

    @Override
    public Path findPath(final Station source, final Station target, final Sections sections) {
        sections.checkExistStations(source, target);
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertexStation(sections, graph);
        addEdgeWeightStation(sections, graph);

        GraphPath<Station, DefaultWeightedEdge> shortestPath = calculateShortestPath(source, target, graph);
        return new Path(shortestPath.getVertexList(), (int) shortestPath.getWeight());
    }

    private void addVertexStation(final Sections sections,
                                  final WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        List<Station> allStations = sections.getAllStations();
        for (Station station : allStations) {
            graph.addVertex(station);
        }
    }

    private void addEdgeWeightStation(final Sections sections,
                                      final WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        List<Section> allSections = sections.getSections();
        for (Section section : allSections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }

    private GraphPath<Station, DefaultWeightedEdge> calculateShortestPath(final Station source, final Station target,
                                                                          final WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return Optional.ofNullable(dijkstraShortestPath.getPath(source, target))
                .orElseThrow(() -> new NotFoundException("갈 수 있는 경로를 찾을 수 없습니다."));
    }
}
