package wooteco.subway.domain.path;

import java.util.List;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.section.Distance;

public class Path {
    private final List<Station> stations;
    private final Distance distance;
    private final Fare extraFare;

    public Path(List<Station> stations, Distance distance, Fare extraFare) {
        this.stations = stations;
        this.distance = distance;
        this.extraFare = extraFare;
    }

    public Fare calculateFare(Age age) {
        Fare fare = Fare.sum(distance.calculateFare(), extraFare);
        return AgeFare.calculate(fare, age);
    }

    public List<Station> getStations() {
        return stations;
    }

    public double getDistance() {
        return distance.getValue();
    }
}
