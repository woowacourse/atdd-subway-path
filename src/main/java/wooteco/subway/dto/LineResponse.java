package wooteco.subway.dto;

import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;
import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private int extraFare;
    private List<StationResponse> stations;

    private LineResponse() {
    }

    public LineResponse(final Long id,
                        final String name,
                        final String color,
                        final int extraFare,
                        final List<StationResponse> stationResponses) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.stations = stationResponses;
    }

    public static LineResponse from(final Line line) {
        final Long id = line.getId();
        final String name = line.getName();
        final String color = line.getColor();
        final int extraFare = line.getExtraFare();

        return new LineResponse(id, name, color, extraFare, null);
    }

    public static LineResponse from(final Line line, final List<Station> stations) {
        final Long id = line.getId();
        final String name = line.getName();
        final String color = line.getColor();
        final int extraFare = line.getExtraFare();

        final List<StationResponse> stationResponses = stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());

        return new LineResponse(id, name, color, extraFare, stationResponses);
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
