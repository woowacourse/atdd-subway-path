package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Path {
    private final List<Station> stations;
    private final Distance distance;

    private Path(List<Station> stations, Distance distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public static Path from(ShortestPathAlgorithm<Station, DefaultWeightedEdge> shortestPathAlgorithm,
                            Station source, Station target) {
        try {
            GraphPath<Station, DefaultWeightedEdge> path = shortestPathAlgorithm.getPath(source, target);
            return new Path(path.getVertexList(), Distance.fromKilometer(path.getWeight()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("해당 역은 경로에 존재하지 않습니다.");
        }
    }

    public int calculateFare() {
        return distance.calculateFare();
    }

    public List<Station> getStations() {
        return stations;
    }

    public double getDistance() {
        return distance.getValue();
    }
}
