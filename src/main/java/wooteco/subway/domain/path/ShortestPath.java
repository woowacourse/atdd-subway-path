package wooteco.subway.domain.path;

import java.util.ArrayList;
import java.util.List;
import wooteco.subway.domain.Fare.Fare;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.distance.Kilometer;

public class ShortestPath {

    private final List<Station> stations;
    private final Kilometer distance;
    private final Fare extraFare;

    public ShortestPath(List<Station> stations, Kilometer distance, Fare extraFare) {
        this.stations = stations;
        this.distance = distance;
        this.extraFare = extraFare;
    }

    public Kilometer getDistance() {
        return distance;
    }

    public List<Station> getStations() {
        return new ArrayList<>(stations);
    }

    public Fare getFare() {
        return extraFare;
    }
}
