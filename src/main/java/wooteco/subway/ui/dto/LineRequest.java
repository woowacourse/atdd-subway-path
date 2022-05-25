package wooteco.subway.ui.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import wooteco.subway.service.dto.LineServiceRequest;

public class LineRequest {

    @NotBlank(message = "name을 입력해주세요.")
    private String name;
    @NotBlank(message = "color를 입력해주세요.")
    private String color;
    @NotNull(message = "upStationId를 입력해주세요.")
    private Long upStationId;
    @NotNull(message = "downStationId를 입력해주세요.")
    private Long downStationId;
    @NotNull(message = "distance를 입력해주세요.")
    private int distance;
    @NotNull(message = "distance를 입력해주세요.")
    private int extraFare;


    public LineRequest() {
    }

    public LineRequest(String name, String color, Long upStationId, Long downStationId,
        int distance,
        int extraFare) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.extraFare = extraFare;
    }

    public LineRequest(String name, String color) {
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

    public LineServiceRequest toServiceRequest() {
        return new LineServiceRequest(name, color, upStationId, downStationId, distance, extraFare);
    }
}
