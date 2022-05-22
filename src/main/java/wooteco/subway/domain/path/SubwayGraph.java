package wooteco.subway.domain.path;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.domain.element.Line;
import wooteco.subway.domain.element.Section;
import wooteco.subway.domain.element.Station;

public class SubwayGraph {

    private final DijkstraShortestPath<Station, LineWeightEdge> path;

    public SubwayGraph(List<Section> sections) {
        this.path = createPath(new ArrayList<>(sections));
    }

    private DijkstraShortestPath<Station, LineWeightEdge> createPath(List<Section> sections) {
        WeightedMultigraph<Station, LineWeightEdge> graph = new WeightedMultigraph<>(LineWeightEdge.class);
        for (Section section : sections) {
            graph.addVertex(section.getUpStation());
            graph.addVertex(section.getDownStation());
            LineWeightEdge lineWeightEdge = new LineWeightEdge(section.getLine());
            graph.setEdgeWeight(lineWeightEdge, section.getDistance());
            graph.addEdge(section.getUpStation(), section.getDownStation(), lineWeightEdge);
        }
        return new DijkstraShortestPath<>(graph);
    }

    public List<Station> getShortestRoute(Station source, Station target) {
        GraphPath<Station, LineWeightEdge> result = path.getPath(source, target);
        validateRoute(result);
        return result.getVertexList();
    }

    private void validateRoute(GraphPath<Station, LineWeightEdge> result) {
        if (result == null) {
            throw new IllegalArgumentException("해당 경로가 존재하지 않습니다.");
        }
    }

    public int getShortestDistance(Station source, Station target) {
        return (int) path.getPath(source, target).getWeight();
    }

    public List<Line> getLines(Station source, Station target) {
        Set<Line> lines = new HashSet<>();
        for (LineWeightEdge edge : path.getPath(source, target).getEdgeList()) {
            lines.add(edge.getLine());
        }
        return new ArrayList<>(lines);
    }
}
