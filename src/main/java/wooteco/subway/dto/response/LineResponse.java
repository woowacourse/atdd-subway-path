package wooteco.subway.dto.response;

import java.util.List;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private int extraFare;
    private List<StationResponse> stations;

    private LineResponse() {
    }

    public LineResponse(final Long id, final String name, final String color,
                        final int extraFare, final List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.stations = stations;
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
