package wooteco.subway.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class LineRequest {

    @NotBlank(message = "지하철 노선의 이름을 입력해주세요.")
    private String name;
    @NotBlank(message = "지하철 노선의 색을 입력해주세요.")
    private String color;
    @NotNull(message = "상행 종점을 입력해주세요.")
    private Long upStationId;
    @NotNull(message = "하행 종점을 입력해주세요.")
    private Long downStationId;
    @NotNull(message = "거리를 입력해주세요.")
    private int distance;
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
