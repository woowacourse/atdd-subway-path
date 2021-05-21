package wooteco.subway.path.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class Path {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath;

    public Path(List<Station> stations, List<Line> lines) {
        this.graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        this.dijkstraShortestPath = new DijkstraShortestPath(graph);
        initializeGraph(stations, lines);
    }

    public List<Station> findShortestPath(Station source, Station target) {
        return dijkstraShortestPath.getPath(source, target).getVertexList();
    }

    public double findShortestDistance(Station source, Station target) {
        return dijkstraShortestPath.getPath(source, target).getWeight();
    }

    private void initializeGraph(List<Station> stations, List<Line> lines) {
        initializeVertex(stations);
        initializeEdgeAndWeight(lines);
    }

    private void initializeVertex(List<Station> stations) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void initializeEdgeAndWeight(List<Line> lines) {
        for (Line line : lines) {
            addSectionsByLine(line);
        }
    }

    private void addSectionsByLine(Line line) {
        List<Section> sections = line.getSections().getSections();
        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
        }
    }
}
