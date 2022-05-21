package wooteco.subway.controller.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import wooteco.subway.domain.Line;

public class LineRequest {

    @NotNull(message = "노선 이름이 비었습니다.")
    private String name;

    @NotNull(message = "노선 색상이 비었습니다.")
    private String color;

    @NotNull(message = "상행 역의 id 가 비었습니다.")
    private Long upStationId;

    @NotNull(message = "하행 역의 id 가 비었습니다.")
    private Long downStationId;

    @Min(value = 1, message = "거리는 1보다 커야합니다.")
    private int distance;

    @Min(value = 0, message = "추가 요금 값은 음수일 수 없습니다.")
    private int extraFare;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance, int extraFare) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.extraFare = extraFare;
    }

    public Line toEntity(Long id) {
        return new Line(id, name, color, extraFare);
    }

    public SectionRequest toSectionRequest() {
        return new SectionRequest(upStationId, downStationId, distance);
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

    public int getExtraFare() {
        return extraFare;
    }
}
