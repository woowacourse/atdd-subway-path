package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class SubwayGraph {

    private final WeightedMultigraph<Station, DefaultWeightedEdge> subwayGraph;

    public SubwayGraph() {
        subwayGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
    }

    public void init(final Sections sections) {
        addStationsToVertex(sections);
        addSectionsToEdge(sections);
    }

    private void addStationsToVertex(final Sections sections) {
        for (Station station : sections.getStations()) {
            subwayGraph.addVertex(station);
        }
    }

    private void addSectionsToEdge(final Sections sections) {
        for (Section section : sections.getSections()) {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();
            int distance = section.getDistance();
            subwayGraph.setEdgeWeight(subwayGraph.addEdge(upStation, downStation), distance);
        }
    }

    public Path findShortestPath(final Station source, final Station target) {
        GraphPath<Station, DefaultWeightedEdge> shortestPath = graphResult(source, target);
        List<Station> stations = shortestPath.getVertexList();
        double distance = shortestPath.getWeight();
        int fare = new FareCalculator(distance).calculateFare();
        return new Path(stations, distance, fare);
    }

    private GraphPath<Station, DefaultWeightedEdge> graphResult(final Station source, final Station target) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> pathFinder = new DijkstraShortestPath<>(subwayGraph);
        return pathFinder.getPath(source, target);
    }
}
