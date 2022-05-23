package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.exception.CalculatePathsException;

public class SubwayGraph {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> subwayGraph;

    public SubwayGraph(final Sections sections) {
        subwayGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addStationsToSubwayVertex(sections);
        addSectionsToSubwayEdge(sections);
    }

    private void addStationsToSubwayVertex(final Sections sections) {
        for (Station station : sections.getStations()) {
            subwayGraph.addVertex(station);
        }
    }

    private void addSectionsToSubwayEdge(final Sections sections) {
        for (Section section : sections.getSections()) {
            subwayGraph.setEdgeWeight(subwayGraph.addEdge(section.getUpStation(), section.getDownStation()),
                    section.getDistance());
        }
    }

    public Paths createPathsResult(final Station source, final Station target) {
        GraphPath<Station, DefaultWeightedEdge> subwayGraphResult = createSubwayGraphResult(source, target);
        List<Station> stations = subwayGraphResult.getVertexList();
        double distance = subwayGraphResult.getWeight();
        int fare = createFare(distance);
        return new Paths(stations, distance, fare);
    }

    private GraphPath<Station, DefaultWeightedEdge> createSubwayGraphResult(final Station source,
                                                                            final Station target) {
        try {
            DijkstraShortestPath<Station, DefaultWeightedEdge> pathFinder = new DijkstraShortestPath<>(subwayGraph);
            return pathFinder.getPath(source, target);
        } catch (IllegalArgumentException e) {
            throw new CalculatePathsException("출발역과 도착역 중, 노선에 등록되지 않은 역이 있습니다.");
        }
    }

    private int createFare(final double distance) {
        FareCalculator fareCalculator = new FareCalculator(distance);
        return fareCalculator.calculateFare();
    }
}
