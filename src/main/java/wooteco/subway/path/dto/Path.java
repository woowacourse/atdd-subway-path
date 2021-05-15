package wooteco.subway.path.dto;

import java.util.List;

import org.jgrapht.GraphPath;

import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

public class Path {
    private List<Station> stations;
    private int distance;

    public Path() {
    }

    public Path(List<Station> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
