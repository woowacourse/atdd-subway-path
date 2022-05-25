package wooteco.subway.dto.response;

import java.util.List;
import wooteco.subway.domain.line.Line;

public class LineResponse {
    private long id;
    private String name;
    private String color;
    private int extraFare;
    private List<StationResponse> stations;

    private LineResponse() {
    }

    public LineResponse(long id, String name, String color, int extraFare, List<StationResponse> stationResponses) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.stations = stationResponses;
    }

    public static LineResponse from(long id, Line line, List<StationResponse> stationResponses) {
        String name = line.getName();
        String color = line.getColor();
        int extraFare = line.getExtraFare().getValue();

        return new LineResponse(id, name, color, extraFare, stationResponses);
    }

    public static LineResponse from(Line line, List<StationResponse> stationResponses) {
        return from(line.getId(), line, stationResponses);
    }

    public long getId() {
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
