package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class PathGraph {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(
        DefaultWeightedEdge.class);

    public PathGraph(List<Line> lines) {
        initGraph(lines);
    }

    private void initGraph(List<Line> lines) {
        for (Line line : lines) {
            initVertex(line);
            initEdge(line);
        }
    }

    private void initVertex(Line line) {
        for (Station station : line.getStations()) {
            graph.addVertex(station);
        }
    }

    private void initEdge(Line line) {
        for (Section section : line.getSections().getSections()) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()),
                section.getDistance());
        }
    }

    public Path findShortestPath(Station source, Station target) {
        DijkstraShortestPath dijkstraShortestPath
            = new DijkstraShortestPath(graph);
        GraphPath path = dijkstraShortestPath.getPath(source, target);
        return new Path(path.getVertexList(), (int) path.getWeight());
    }
}
