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

public class SubwayMap {

    WeightedMultigraph<Station, DefaultWeightedEdge> subwayMap =
        new WeightedMultigraph<Station, DefaultWeightedEdge>(DefaultWeightedEdge.class);

    public SubwayMap(List<Line> lines) {
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
        subwayMap.addVertex(section.getUpStation());
        subwayMap.addVertex(section.getDownStation());
        subwayMap.setEdgeWeight(
            subwayMap.addEdge(section.getUpStation(), section.getDownStation()),
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
            new DijkstraShortestPath(subwayMap);

        return dijkstraShortestPath.getPath(source, target);
    }
}
