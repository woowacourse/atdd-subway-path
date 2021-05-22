package wooteco.subway.line.application.dto;

import wooteco.subway.line.domain.Line;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class LineResponseDto {

    private final Long id;
    private final String name;
    private final String color;
    private final List<StationResponseDto> stations;

    private LineResponseDto(Long id, String name, String color, List<StationResponseDto> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineResponseDto of(Line line) {
        List<StationResponseDto> stations = line.getStations().stream()
                .map(StationResponseDto::of)
                .collect(toList());

        return new LineResponseDto(line.getId(), line.getName(), line.getColor(), stations);
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

    public List<StationResponseDto> getStations() {
        return stations;
    }

}
