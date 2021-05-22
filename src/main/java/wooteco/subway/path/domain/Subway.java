package wooteco.subway.path.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class Subway {

    WeightedMultigraph<Station, DefaultWeightedEdge> subway =
        new WeightedMultigraph<Station, DefaultWeightedEdge>(DefaultWeightedEdge.class);

    public Subway(List<Line> lines) {
        createMap(lines);
    }

    private void createMap(List<Line> lines) {
        for (Line line : lines) {
            addToMap(line.getSections());
        }
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
}
