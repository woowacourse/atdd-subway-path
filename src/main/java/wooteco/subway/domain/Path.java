package wooteco.subway.domain;

import java.util.Collections;
import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Path {
    private final List<Station> stations;
    private final int distance;
    private final Fare fare;


    public Path(List<Section> sections, Station departure, Station arrival) {
        final GraphPath<Station, DefaultWeightedEdge> path = generatePath(sections, departure, arrival);
        this.stations = path.getVertexList();
        this.distance = (int) path.getWeight();
        this.fare = Fare.from(distance);
    }

    private GraphPath<Station, DefaultWeightedEdge> generatePath(List<Section> sections, Station departure,
                                                                 Station arrival) {
        final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(
                DefaultWeightedEdge.class);

        for (Section section : sections) {
            final Station upStation = section.getUpStation();
            final Station downStation = section.getDownStation();

            graph.addVertex(upStation);
            graph.addVertex(downStation);
            graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance());
        }

        final DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(graph);
        return shortestPath.getPath(departure, arrival);
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public int getFare() {
        return this.fare.getFare();
    }

    public int getDistance() {
        return distance;
    }
}
