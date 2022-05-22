package wooteco.subway.dto.line;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.station.StationResponse;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;
    private int extraFare;

    private LineResponse() {
    }

    public LineResponse(Long id, String name, String color, int extraFare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public LineResponse(LineResponse line, List<Station> stations) {
        this.id = line.id;
        this.name = line.name;
        this.color = line.color;
        this.extraFare = line.extraFare;
        this.stations = stations.stream()
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

    public int getExtraFare() {
        return extraFare;
    }
}
