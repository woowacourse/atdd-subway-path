package wooteco.subway.service.dto.line;

import wooteco.subway.domain.Line;

import java.util.Objects;

public class LineRequestDto {

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    private int extraFare;

    public LineRequestDto() {
    }

    public LineRequestDto(String name, String color, Long upStationId, Long downStationId, int distance, int extraFare) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.extraFare = extraFare;
    }

    public LineRequestDto(String name, String color, Long upStationId, Long downStationId, int distance) {
        this(name, color, upStationId, downStationId, distance, 0);
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }


    public Line toLine() {
        return new Line(name, color, extraFare);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineRequestDto that = (LineRequestDto) o;
        return distance == that.distance && extraFare == that.extraFare && Objects.equals(name, that.name) && Objects.equals(color, that.color) && Objects.equals(upStationId, that.upStationId) && Objects.equals(downStationId, that.downStationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color, upStationId, downStationId, distance, extraFare);
    }
}
