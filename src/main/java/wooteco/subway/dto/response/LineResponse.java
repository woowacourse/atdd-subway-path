package wooteco.subway.dto.response;

import java.util.List;
import wooteco.subway.domain.line.Line;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private Integer extraFare;
    private List<StationResponse> stations;

    private LineResponse() {
    }

    public LineResponse(Long id, String name, String color, Integer extraFare, List<StationResponse> stationResponses) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.stations = stationResponses;
    }

    public static LineResponse from(Long id, Line line, List<StationResponse> stationResponses) {
        String name = line.getName();
        String color = line.getColor();
        Integer extraFare = line.getExtraFare().getValue();

        return new LineResponse(id, name, color, extraFare, stationResponses);
    }

    public static LineResponse from(Line line, List<StationResponse> stationResponses) {
        return from(line.getId(), line, stationResponses);
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

    public Integer getExtraFare() {
        return extraFare;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    @Override
    public String toString() {
        return "LineResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", stations=" + stations +
                '}';
    }
}
