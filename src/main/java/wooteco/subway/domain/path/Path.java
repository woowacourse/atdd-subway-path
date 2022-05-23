package wooteco.subway.domain.path;

import java.util.List;
import wooteco.subway.domain.element.Line;
import wooteco.subway.domain.element.Station;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.fare.PolicyFactory;

public class Path {
    private final List<Station> stations;
    private final int distance;
    private final Fare fare;

    private Path(List<Station> stations, int distance, Fare fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static Path create(SubwayGraph graph, Station source, Station target, int age) {
        return new Path(graph.getShortestRoute(source, target),
                graph.getShortestDistance(source, target),
                getFare(graph.getLines(source,target), age));
    }

    private static Fare getFare(List<Line> lines, int age) {
        return new Fare(List.of(
                PolicyFactory.createLineFee(lines),
                PolicyFactory.createAgeDiscount(age)
        ));
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare.getFare(distance);
    }
}
