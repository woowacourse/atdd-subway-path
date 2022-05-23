package wooteco.subway.domain.path;

import java.util.List;

import wooteco.subway.domain.property.Distance;
import wooteco.subway.domain.property.fare.Fare;
import wooteco.subway.domain.station.Station;

public class Path {

    private final List<Station> stations;
    private final List<Fare> extraFares;
    private final Distance distance;

    public Path(List<Station> stations, List<Fare> extraFares, Distance distance) {
        this.stations = stations;
        this.extraFares = extraFares;
        this.distance = distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public List<Fare> getExtraFares() {
        return extraFares;
    }

    public Distance getDistance() {
        return distance;
    }
}
