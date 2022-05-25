package wooteco.subway.dto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private int extraFare;
    private List<StationResponse> stations;

    private LineResponse() {
    }

    public LineResponse(Long id, String name, String color, int extraFare, List<Station> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.stations = stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }

    public static LineResponse from(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getExtraFare(), line.getStations());
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

    public int getExtraFare() {
        return extraFare;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
