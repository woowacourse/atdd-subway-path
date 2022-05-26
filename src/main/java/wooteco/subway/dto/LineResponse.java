package wooteco.subway.dto;

import java.util.Set;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private int extraFare;
    private Set<StationResponse> stations;

    public LineResponse() {
    }

    public LineResponse(final Long id, final String name, final String color, final int extraFare,
                        final Set<StationResponse> stations) {
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

    public Set<StationResponse> getStations() {
        return stations;
    }
}
