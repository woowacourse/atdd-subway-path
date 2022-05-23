package wooteco.subway.dto.service.response;

import java.util.List;

import wooteco.subway.dto.service.StationDto;

public class LineServiceResponse {
    private final long id;
    private final String name;
    private final String color;
    private final int extraFare;
    private final List<StationDto> stationDtos;

    public LineServiceResponse(long id, String name, String color,
        int extraFare, List<StationDto> stationDtos) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.stationDtos = stationDtos;
    }

    public long getId() {
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

    public List<StationDto> getStationInfos() {
        return stationDtos;
    }
}
