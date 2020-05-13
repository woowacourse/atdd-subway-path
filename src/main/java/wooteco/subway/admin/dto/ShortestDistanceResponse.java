package wooteco.subway.admin.dto;

import java.util.List;

import wooteco.subway.admin.domain.LineStation;
import wooteco.subway.admin.domain.Station;

public class ShortestDistanceResponse {
    private List<StationResponse> stations;
    private int distance;
    private int duration;

    public ShortestDistanceResponse() {
    }

    public ShortestDistanceResponse(List<StationResponse> stations, int distance, int duration) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
    }

    public static ShortestDistanceResponse of(List<Station> stations,
        List<LineStation> lineStations) {
        int distance = lineStations.stream()
            .mapToInt(LineStation::getDistance)
            .sum();
        int duration = lineStations.stream()
            .mapToInt(LineStation::getDuration)
            .sum();
        List<StationResponse> stationResponses = StationResponse.listOf(stations);
        return new ShortestDistanceResponse(stationResponses, distance, duration);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }
}
