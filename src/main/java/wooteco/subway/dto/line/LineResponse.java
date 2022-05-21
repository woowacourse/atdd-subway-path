package wooteco.subway.dto.line;

import java.util.List;
import wooteco.subway.domain.line.Line;
import wooteco.subway.dto.station.StationResponse;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private int extraFare;
    private List<StationResponse> stations;

    private LineResponse() {
    }

    public LineResponse(Line line, List<StationResponse> stations) {
        this(line.getId(), line.getName(), line.getColor(), line.getExtraFare(), stations);
    }

    public LineResponse(Long id, LineRequest lineRequest, List<StationResponse> stations) {
        this(id, lineRequest.getName(), lineRequest.getColor(), lineRequest.getExtraFare(), stations);
    }

    public LineResponse(Long id, String name, String color, int extraFare, List<StationResponse> stations) {
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
