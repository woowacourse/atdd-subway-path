package wooteco.subway.service.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class LineServiceRequest {

    @NotBlank(message = "노선 이름을 입력해주세요.")
    private final String name;

    @NotBlank(message = "노선 색상을 입력해주세요.")
    private final String color;

    @NotNull(message = "노선의 상행역 아이디를 입력해주세요.")
    private final Long upStationId;

    @NotNull(message = "노선의 상행역 아이디를 입력해주세요.")
    private final Long downStationId;

    @Positive(message = "거리를 올바르게 입력해주세요.")
    private final int distance;

    private final int extraFare;

    public LineServiceRequest(String name, String color, Long upStationId, Long downStationId,
                              int distance, int extraFare) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.extraFare = extraFare;
    }

    public LineServiceRequest(String name, String color) {
        this(name, color, null, null, 0, 0);
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
