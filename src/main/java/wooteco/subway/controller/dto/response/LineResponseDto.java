package wooteco.subway.controller.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.domain.Line;

public class LineResponseDto {
    private Long id;
    private String name;
    private String color;
    private List<StationResponseDto> stations;

    public LineResponseDto() {
    }

    public LineResponseDto(Long id, String name, String color, List<StationResponseDto> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineResponseDto of(Line line) {
        List<StationResponseDto> stations = line.getStations().stream()
            .map(StationResponseDto::of)
            .collect(Collectors.toList());
        return new LineResponseDto(line.getId(), line.getName(), line.getColor(), stations);
    }

    public static List<LineResponseDto> listOf(List<Line> lines) {
        return lines.stream()
            .map(LineResponseDto::of)
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

    public List<StationResponseDto> getStations() {
        return stations;
    }
}
