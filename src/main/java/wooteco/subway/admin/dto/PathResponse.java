package wooteco.subway.admin.dto;

import java.util.List;

import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.domain.SubwayPath;

public class PathResponse {
    private List<StationResponse> stations;
    private Long distance;
    private Long duration;

    private PathResponse() {
    }

    private PathResponse(List<StationResponse> stations, Long distance, Long duration) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
    }

    public static PathResponse of(List<Station> stations, Long distance, Long duration) {
        List<StationResponse> stationResponses = StationResponse.listOf(stations);
        return new PathResponse(stationResponses, distance, duration);
    }

    public static PathResponse of(SubwayPath shortestDistancePath) {
        return new PathResponse(StationResponse.listOf(shortestDistancePath.getVertices()),
                shortestDistancePath.calculateTotalDistance(), shortestDistancePath.calculateTotalDuration());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public Long getDistance() {
        return distance;
    }

    public Long getDuration() {
        return duration;
    }
}
