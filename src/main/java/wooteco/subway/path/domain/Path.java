package wooteco.subway.path.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class Path {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public Path(List<Station> stations, List<Section> sections) {
        graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        initializeGraph(stations, sections);
    }

    private void initializeGraph(List<Station> stations, List<Section> sections) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
        for (Section section : sections) {
            graph.setEdgeWeight(
                    graph.addEdge(section.getUpStation(), section.getDownStation()),
                    section.getDistance());
        }
    }

    public List<Station> shortestPath(Station source, Station target) {
        return shortestGraph(source, target).getVertexList();
    }

    public int shortestDistance(Station source, Station target) {
        return (int) shortestGraph(source, target).getWeight();
    }

    private GraphPath shortestGraph(Station source, Station target) {
        DijkstraShortestPath path = new DijkstraShortestPath(graph);
        return path.getPath(source, target);
    }
}
