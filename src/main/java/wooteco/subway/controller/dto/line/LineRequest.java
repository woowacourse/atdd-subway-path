package wooteco.subway.controller.dto.line;

import org.hibernate.validator.constraints.Length;
import wooteco.subway.service.dto.line.LineRequestDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

public class LineRequest {

    @NotBlank(message = "[ERROR] 라인 이름은 공백일 수 없습니다.")
    @Length(max = 255, message = "[ERROR] 라인 이름은 255자 이하입니다.")
    private String name;
    @NotBlank(message = "[ERROR] 라인 색은 공백일 수 없습니다.")
    @Length(max = 20, message = "[ERROR] 라인 색은 20자 이하입니다.")
    private String color;
    @Positive(message = "[ERROR] 상행선은 양수입니다.")
    private Long upStationId;
    @Positive(message = "[ERROR] 하행선은 양수입니다.")
    private Long downStationId;
    @Positive(message = "[ERROR] 거리는 양수입니다.")
    private int distance;
    @PositiveOrZero(message = "[ERROR] 추가요금은 0 이상의 양수입니다.")
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

    public LineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
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

    public int getExtraFare() {
        return extraFare;
    }

    public LineRequestDto toServiceRequest() {
        return new LineRequestDto(name, color, upStationId, downStationId, distance, extraFare);
    }
}
