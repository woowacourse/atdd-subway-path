package wooteco.subway.service.dto;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;
    private Long extraFare;

    public LineResponse() {
    }

    public LineResponse(Long id, String name, String color,
                        List<StationResponse> stations, Long extraFare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
        this.extraFare = extraFare;
    }

    public LineResponse(Long id, String name, String color, List<StationResponse> stations) {
        this(id, name, color, stations, 0L);
    }

    public static LineResponse of(Line line, List<Station> stations) {
        List<StationResponse> stationResponses = stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return new LineResponse(line.getId(), line.getName(), line.getColor(), stationResponses, line.getExtraFare());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public Long getExtraFare() {
        return extraFare;
    }
}
