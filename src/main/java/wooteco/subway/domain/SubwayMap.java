package wooteco.subway.domain;

import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class SubwayMap {
    private final DijkstraShortestPath pathFinder;

    private SubwayMap(DijkstraShortestPath pathFinder) {
        this.pathFinder = pathFinder;
    }

    public static SubwayMap of(List<Line> lines) {
        WeightedMultigraph graph = new WeightedMultigraph(DefaultWeightedEdge.class);

        for (Line line : lines) {
            addVertex(graph, line.getStations());
        }

        for(Line line : lines){
            addEdge(graph, line.getSections());
        }

        return new SubwayMap(new DijkstraShortestPath(graph));
    }

    private static void addVertex(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Station> stations) {
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private static void addEdge(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Section> sections) {
        for (Section section : sections) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
            graph.setEdgeWeight(graph.addEdge(section.getDownStation(), section.getUpStation()), section.getDistance());
        }
    }

    public Path findShortestPath(Station station1, Station station2) {
        GraphPath path = pathFinder.getPath(station1, station2);

        return Path.of(path.getVertexList(), path.getWeight());
    }
}
