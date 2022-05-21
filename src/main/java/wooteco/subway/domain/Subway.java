package wooteco.subway.domain;

import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.domain.vo.Path;
import wooteco.subway.exception.EmptyResultException;

public class Subway {
    private static final int BASE_FARE = 1250;

    private final DijkstraShortestPath<Station, DefaultWeightedEdge> pathFinder;
    private final Fare fare;

    public Subway(DijkstraShortestPath<Station, DefaultWeightedEdge> pathFinder, Fare fare) {
        this.pathFinder = pathFinder;
        this.fare = fare;
    }

    public static Subway of(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        initGraph(lines, graph);

        return new Subway(new DijkstraShortestPath(graph), new Fare(BASE_FARE));
    }

    private static void initGraph(List<Line> lines, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        for (Line line : lines) {
            addVertex(graph, line.getStations());
        }

        for(Line line : lines){
            addEdge(graph, line.getSections());
        }
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

    public Path findShortestPath(Station source, Station target) {
        GraphPath<Station, DefaultWeightedEdge> path = pathFinder.getPath(source, target);

        validateEmptyPath(path);
        return Path.of(path.getVertexList(), path.getWeight());
    }

    private void validateEmptyPath(GraphPath<Station, DefaultWeightedEdge> path) {
        if (path == null) {
            throw new EmptyResultException("출발역과 도착역 사이에 연결된 경로가 없습니다.");
        }
    }

    public int calculateFare(int distance) {
        return fare.calculateFare(distance);
    }
}
