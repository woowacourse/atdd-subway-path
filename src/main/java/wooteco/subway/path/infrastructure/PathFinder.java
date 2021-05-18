package wooteco.subway.path.infrastructure;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.path.domain.Path;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class PathFinder {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public PathFinder(List<Line> lines) {
        this(makeGraph(lines));
    }

    public PathFinder(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        this.graph = graph;
    }

    private static WeightedMultigraph<Station, DefaultWeightedEdge> makeGraph(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = getGraph();

        for (Line line : lines) {
            setVertex(graph, line);
            setEdge(graph, line);
        }
        return graph;
    }

    private static WeightedMultigraph<Station, DefaultWeightedEdge> getGraph() {
        return new WeightedMultigraph<>(DefaultWeightedEdge.class);
    }

    private static void setVertex(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Line line) {
        for (Station station : line.getStations()) {
            graph.addVertex(station);
        }
    }

    private static void setEdge(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Line line) {
        for (Section section : line.getSections().getSections()) {
            setEdge(graph, section);
        }
    }

    private static void setEdge(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Section section) {
        DefaultWeightedEdge edge = graph.addEdge(
                section.getUpStation(),
                section.getDownStation());

        graph.setEdgeWeight(edge, section.getDistance());
    }

    public Path shortestPath(Station source, Station target) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath
                = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> graphPath
                = shortestPath.getPath(source, target);

        return new Path(
                graphPath.getVertexList(),
                (int) graphPath.getWeight());
    }
}
