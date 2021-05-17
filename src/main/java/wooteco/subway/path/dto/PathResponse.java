package wooteco.subway.path.dto;

import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public static PathResponse of(List<Station> stations, double distance) {
        List<StationResponse> stationsResponses = new ArrayList<>();
        for (Station station: stations) {
            stationsResponses.add(StationResponse.of(station));
        }
        return new PathResponse(stationsResponses, (int) distance);
    }
}
