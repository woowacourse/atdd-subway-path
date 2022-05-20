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
    private final Cost cost;

    private Path(List<Station> stations, int distance, Cost cost) {
        this.stations = stations;
        this.distance = distance;
        this.cost = cost;
    }

    public static Path from(List<Section> sections, Station departure, Station arrival) {
        ShortestPath shortestPath = ShortestPath.generate(sections, departure, arrival);
        final List<Station> stations = shortestPath.getPath();
        final int distance = shortestPath.getDistance();
        final Cost cost = Cost.from(distance);
        return new Path(stations, distance, cost);
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public int getCost() {
        return this.cost.getCost();
    }

    public int getDistance() {
        return distance;
    }
}
