package wooteco.subway.controller.dto;

import java.util.List;
import java.util.stream.Collectors;

import wooteco.subway.service.dto.LineDto;

public class LineResponse {

    private final Long id;
    private final String name;
    private final String color;
    private final int extraFare;
    private final List<StationResponse> stations;

    public LineResponse(Long id, String name, String color, int extraFare, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.stations = stations;
    }

    public static LineResponse from(LineDto lineDto) {
        return new LineResponse(lineDto.getId(), lineDto.getName(), lineDto.getColor(), lineDto.getExtraFare(),
            lineDto.getStations().stream()
                .map(StationResponse::from)
                .collect(Collectors.toList()));
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
