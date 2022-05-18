package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Path {

    public static final int BASIC_FARE = 1250;
    private final GraphPath<Station, DefaultWeightedEdge> graphPath;

    public Path(GraphPath<Station, DefaultWeightedEdge> graphPath) {
        this.graphPath = graphPath;
    }

    public int chargeFare() {
        double distance = graphPath.getWeight();

        if (isUnder10km(distance)) {
            return BASIC_FARE;
        }
        if (isBetween10kmAnd50km(distance)) {
            return calculateOverFareUnder50(distance);
        }
        return calculateOverFareOver50(distance);
    }

    private boolean isBetween10kmAnd50km(double distance) {
        return distance <= 50;
    }

    private boolean isUnder10km(double distance) {
        return distance <= 10;
    }

    private int calculateOverFareUnder50(double distance) {
        return (int) (Math.ceil((distance - 10) / 5) * 100) + BASIC_FARE;
    }

    private int calculateOverFareOver50(double distance) {
        return calculateOverFareUnder50(50) +
         (int) (Math.ceil((distance - 50) / 8) * 100);
    }

    public List<Station> findStationsOnPath() {
        return graphPath.getVertexList();
    }

    public int calculateShortestDistance() {
        return (int) graphPath.getWeight();
    }
}
