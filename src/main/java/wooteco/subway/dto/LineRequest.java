package wooteco.subway.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class LineRequest {
    @NotBlank(message = "노선의 이름을 입력해주세요.")
    @Length(max = 255)
    private String name;

    @NotBlank(message = "노선의 색깔을 정해주세요.")
    @Length(max = 20)
    private String color;

    @NotNull(message = "노선의 상행역을 입력해주세요.")
    private Long upStationId;

    @NotNull(message = "노선의 하행역을 입력해주세요.")
    private Long downStationId;

    @Min(value = 1, message = "거리는 자연수여야 합니다.")
    private int distance;

    private int extraFare;

    private LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance, int extraFare) {
        this(name, color, upStationId, downStationId, distance);
        this.extraFare = extraFare;
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
