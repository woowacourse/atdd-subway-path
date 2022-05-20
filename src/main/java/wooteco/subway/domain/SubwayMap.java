package wooteco.subway.domain;

import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import wooteco.subway.exception.EmptyResultException;

public class SubwayMap {
    private final DijkstraShortestPath<Station, DefaultWeightedEdge> pathFinder;

    private SubwayMap(DijkstraShortestPath<Station, DefaultWeightedEdge> pathFinder) {
        this.pathFinder = pathFinder;
    }

    public static SubwayMap of(List<Line> lines) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<Station, DefaultWeightedEdge>(DefaultWeightedEdge.class);

        for (Line line : lines) {
            addVertex(graph, line.getStations());
        }

        for(Line line : lines){
            addEdge(graph, line.getSections());
        }

        return new SubwayMap(new DijkstraShortestPath<Station, DefaultWeightedEdge>(graph));
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

        checkNoPath(path);
        return Path.of(path.getVertexList(), path.getWeight());
    }

    private void checkNoPath(GraphPath<Station, DefaultWeightedEdge> path) {
        if (path == null) {
            throw new EmptyResultException("출발역과 도착역 사이에 연결된 경로가 없습니다.");
        }
    }
}
