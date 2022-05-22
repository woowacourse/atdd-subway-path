package wooteco.subway.service.path;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

@Component
public class ShortestPathFindable implements PathFindable {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public ShortestPathFindable() {
        this.graph = new WeightedMultigraph<>(SectionEdge.class);
    }

    @Override
    public Path findPath(List<Section> sections, Station source, Station target) {
        fillVertexes(sections);
        fillEdge(sections);

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);

        List<Station> stations = getStations(source, target, dijkstraShortestPath);
        List<Section> pathInSections = getSections(source, target, dijkstraShortestPath);
        int distance = getDistance(source, target, dijkstraShortestPath);

        return new Path(stations, pathInSections, distance);
    }

    private void fillVertexes(List<Section> sections) {
        for (Section section : sections) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
        }
    }

    private void fillEdge(List<Section> sections) {
        for (Section section : sections) {
            SectionEdge sectionEdge = new SectionEdge(section);
            graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
        }
    }

    private List<Station> getStations(
            Station source, Station target, DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath) {
        GraphPath<Station, DefaultWeightedEdge> shortestPath = getPath(source, target, dijkstraShortestPath);
        validateExistPath(shortestPath);

        return shortestPath.getVertexList();
    }

    private int getDistance(
            Station source, Station target, DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath) {
        GraphPath<Station, DefaultWeightedEdge> shortestPath = getPath(source, target, dijkstraShortestPath);
        validateExistPath(shortestPath);

        return (int) shortestPath.getWeight();
    }

    private List<Section> getSections(
            Station source, Station target, DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath) {
        GraphPath<Station, SectionEdge> shortestPath = getPath(source, target, dijkstraShortestPath);
        return shortestPath.getEdgeList()
                .stream()
                .map(SectionEdge::getSection)
                .collect(toList());
    }

    private GraphPath getPath(
            Station source, Station target, DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath) {
        try {
            return dijkstraShortestPath.getPath(source, target);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("노선에 등록되지 않는 지하철역입니다.");
        }
    }

    private void validateExistPath(GraphPath<Station, DefaultWeightedEdge> shortestPath) {
        if (Objects.isNull(shortestPath)) {
            throw new IllegalArgumentException("경로가 존재하지 않습니다.");
        }
    }
}
