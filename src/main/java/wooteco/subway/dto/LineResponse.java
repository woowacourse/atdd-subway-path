package wooteco.subway.dto;

import wooteco.subway.domain.Station;

import java.util.Set;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private int extraFare;
    private Set<Station> stations;

    public LineResponse() {
    }

    public LineResponse(Long id, String name, String color, int extraFare, Set<Station> stations) {
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

    public Set<Station> getStations() {
        return stations;
    }
}
