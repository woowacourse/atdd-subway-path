package wooteco.subway.controller.dto.response;

import java.util.List;

public class LineResponseForm {

    private Long id;
    private String name;
    private String color;
    private Long extraFare;
    private List<StationResponseForm> stations;

    public LineResponseForm() {
    }

    public LineResponseForm(Long id, String name, String color, Long extraFare, List<StationResponseForm> stations) {
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

    public Long getExtraFare() {
        return extraFare;
    }

    public List<StationResponseForm> getStations() {
        return stations;
    }
}
