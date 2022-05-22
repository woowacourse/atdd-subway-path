package wooteco.subway.ui.dto;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.service.dto.LineServiceResponse;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    private LineResponse() {
    }

    public LineResponse(LineServiceResponse lineServiceResponse) {
        this.id = lineServiceResponse.getId();
        this.name = lineServiceResponse.getName();
        this.color = lineServiceResponse.getColor();
        this.stations = lineServiceResponse.getStations()
                .stream()
                .map(StationResponse::new)
                .collect(Collectors.toList());
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
}
