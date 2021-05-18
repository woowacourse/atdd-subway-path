package wooteco.subway.path.domain;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class Graph {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

    public Graph(List<Line> lines) {

        for (Line line : lines) {
            initVertex(line.getStations());
            initPath(line.getSections().getSections());
        }
    }

    private void initVertex(List<Station> stations) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void initPath(List<Section> sections) {
        for (Section section : sections) {
            initEdge(section);
        }
    }

    private void initEdge(Section section) {
        graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
    }

    public Path shortestPath(Station source, Station target) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        return new Path(dijkstraShortestPath.getPath(source, target).getVertexList(), dijkstraShortestPath.getPathWeight(source, target));
    }
}
