package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import wooteco.subway.exception.NotLinkPathException;

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

    public Path findShortestPath(final Station source, final Station target, final int age) {
        GraphPath<Station, DefaultWeightedEdge> shortestPath = graphResult(source, target);
        List<Station> stations = shortestPath.getVertexList();
        double distance = shortestPath.getWeight();
        int fare = new FareCalculator(distance).calculateFare(age);
        return new Path(stations, distance, fare);
    }

    private GraphPath<Station, DefaultWeightedEdge> graphResult(final Station source, final Station target) {
        DijkstraShortestPath<Station, DefaultWeightedEdge> pathFinder = new DijkstraShortestPath<>(subwayGraph);
        GraphPath<Station, DefaultWeightedEdge> path = pathFinder.getPath(source, target);
        validateSourceToTargetLink(path);
        return path;
    }

    private void validateSourceToTargetLink(GraphPath<Station, DefaultWeightedEdge> path) {
        if (path == null) {
            throw new NotLinkPathException("출발역과 도착역이 연결되어 있지 않습니다.");
        }
    }

}
