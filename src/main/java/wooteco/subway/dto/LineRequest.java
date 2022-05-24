package wooteco.subway.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class LineRequest {

    @NotBlank(message = "이름은 빈 값일 수 없습니다.")
    private String name;

    @NotBlank(message = "색깔은 빈 값일 수 없습니다.")
    private String color;

    @NotNull(message = "상행역은 빈 값일 수 없습니다.")
    private Long upStationId;

    @NotNull(message = "하행역은 빈 값일 수 없습니다.")
    private Long downStationId;

    @NotNull(message = "거리는 빈 값일 수 없습니다.")
    private Integer distance;

    @NotNull(message = "추가 요금은 빈 값일 수 없습니다.")
    private Integer extraFare;

    public LineRequest() {
    }

    public LineRequest(String name, String color, Integer extraFare) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId, Integer distance,
                       Integer extraFare) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
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

    public Integer getDistance() {
        return distance;
    }

    public Integer getExtraFare() {
        return extraFare;
    }
}
