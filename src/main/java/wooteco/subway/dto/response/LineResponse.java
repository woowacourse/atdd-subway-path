package wooteco.subway.dto.response;

import java.util.List;

public class LineResponse {
    private final long id;
    private final String name;
    private final String color;
    private final int extraFare;
    private final List<StationResponse> stations;

    public LineResponse(long id, String name, String color,
        int extraFare, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.stations = stations;
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
}
