package wooteco.subway.service.dto;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;

public class LineServiceResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final List<StationServiceResponse> stations;

    public LineServiceResponse(Line line, List<Station> stations) {
        this.id = line.getId();
        this.name = line.getName();
        this.color = line.getColor();
        this.stations = stations.stream()
                .map(StationServiceResponse::new)
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

    public List<StationServiceResponse> getStations() {
        return stations;
    }
}
