package wooteco.subway.path.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.domain.Station;

import java.util.List;

@Component
public class Subway {

    private WeightedMultigraph<Station, DefaultWeightedEdge> subway =
        new WeightedMultigraph<>(DefaultWeightedEdge.class);

    public Subway() {
    }

    public void initializeSubway(List<Line> lines) {
        for (Line line : lines) {
            addToMap(line.getSections());
        }
    }

    public void deleteLine(Line line) {
        line.getSections()
            .getSections()
            .forEach(section -> deleteSection(section));
    }

    private void deleteSection(Section section) {
        subway.removeEdge(section.getUpStation(), section.getDownStation());
        subway.removeVertex(section.getUpStation());
        subway.removeVertex(section.getDownStation());
    }

    public void addLine(Line line) {
        addToMap(line.getSections());
    }

    private void addToMap(Sections sections) {
        sections.getSections()
            .forEach(section -> insertInMap(section));
    }

    private void insertInMap(Section section) {
        subway.addVertex(section.getUpStation());
        subway.addVertex(section.getDownStation());
        subway.setEdgeWeight(
            subway.addEdge(section.getUpStation(), section.getDownStation()),
            section.getDistance()
        );
    }

    public List<Station> findPathRoute(Station source, Station target) {
        GraphPath shortestPath = findPath(source, target);
        return shortestPath.getVertexList();
    }

    public int findPathDistance(Station source, Station target) {
        return (int) findPath(source, target).getWeight();
    }

    private GraphPath findPath(Station source, Station target) {
        DijkstraShortestPath dijkstraShortestPath =
            new DijkstraShortestPath(subway);

        return dijkstraShortestPath.getPath(source, target);
    }

    public boolean isEmpty() {
        return subway.vertexSet().isEmpty();
    }
}
