package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

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

    private GraphPath<Station, DefaultWeightedEdge> createSubwayGraphResult(final Station source, final Station target) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> pathFinder = new DijkstraShortestPath<>(subwayGraph);
        return pathFinder.getPath(source, target);
    }

    private int createFare(final double distance) {
        FareCalculator fareCalculator = new FareCalculator(distance);
        return fareCalculator.calculateFare();
    }
}
