package wooteco.subway.dto;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.domain.Station;

public class PathResponse {

    private List<StationResponse> stations;
    private Integer distance;
    private int fare;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, Integer distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

//    public static PathResponse of(List<Station> stations, int distance, Fare1 fare1) {
//        List<StationResponse> stationResponses = stations.stream()
//                .map(station -> new StationResponse(station.getId(), station.getName()))
//                .collect(Collectors.toList());
//        return new PathResponse(stationResponses, distance, fare1.getValue());
//    }
    public static PathResponse of(List<Station> stations, int distance, int fare) {
        List<StationResponse> stationResponses = stations.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
        return new PathResponse(stationResponses, distance, fare);
    }
    public List<StationResponse> getStations() {
        return stations;
    }

    public Integer getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
