package wooteco.subway.path.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

public class PathFinder {

    private final List<Line> lines;
    private final List<Station> stations;

    public PathFinder(List<Station> stations, List<Line> lines) {
        this.stations = new ArrayList<>(stations);
        this.lines = new ArrayList<>(lines);
    }

    public Path shortestPath(Station source, Station target) {
        validateStations(source, target);
        WeightedMultigraph<Station, DefaultWeightedEdge> subwayGraph = new WeightedMultigraph<>(
            DefaultWeightedEdge.class);

        initializeVertices(subwayGraph);
        initializeEdges(subwayGraph);

        return shortestPathWithGraph(source, target, subwayGraph);
    }

    private void validateStations(Station source, Station target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발점과 도착점이 같을 수 없습니다.");
        }
    }

    private void initializeVertices(Graph<Station, DefaultWeightedEdge> subwayGraph) {
        for (Station station : stations) {
            subwayGraph.addVertex(station);
        }
    }

    private void initializeEdges(WeightedMultigraph<Station, DefaultWeightedEdge> subwayGraph) {
        for (Line line : lines) {
            initializeEdgesForEachLine(subwayGraph, line);
        }
    }

    private void initializeEdgesForEachLine(
        WeightedMultigraph<Station, DefaultWeightedEdge> subwayGraph, Line line) {
        for (Section section : line.getSections().getSections()) {
            DefaultWeightedEdge edge = subwayGraph
                .addEdge(section.getUpStation(), section.getDownStation());
            subwayGraph.setEdgeWeight(edge, section.getDistance());
        }
    }

    private Path shortestPathWithGraph(Station source, Station target,
        WeightedMultigraph<Station, DefaultWeightedEdge> subwayGraph) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath =
            new DijkstraShortestPath<>(subwayGraph);
        GraphPath<Station, DefaultWeightedEdge> shortestPath =
            dijkstraShortestPath.getPath(source, target);

        validatePath(shortestPath);

        return new Path(shortestPath.getVertexList(), (int) shortestPath.getWeight());
    }

    private void validatePath(GraphPath<Station, DefaultWeightedEdge> shortestPath) {
        if (shortestPath == null) {
            throw new IllegalArgumentException("경로가 존재하지 않습니다.");
        }
    }
}
