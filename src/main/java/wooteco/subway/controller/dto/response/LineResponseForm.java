package wooteco.subway.controller.dto.response;

import java.util.List;

public class LineResponseForm {

    private Long id;
    private String name;
    private String color;
    private List<StationResponseForm> stations;

    public LineResponseForm() {
    }

    public LineResponseForm(Long id, String name, String color, List<StationResponseForm> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
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

    public List<StationResponseForm> getStations() {
        return stations;
    }
}
