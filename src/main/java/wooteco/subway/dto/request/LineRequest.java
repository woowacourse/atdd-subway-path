package wooteco.subway.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Value;

public class LineRequest {

    private static final String BLANK_ERROR = " 공백일 수 없습니다.";
    private static final String ID_MIN_RANGE_ERROR = " 1보다 커야 합니다.";

    @NotBlank(message = "라인 이름은" + BLANK_ERROR)
    private String name;

    @NotBlank(message = "라인 색은" + BLANK_ERROR)
    private String color;

    @Min(value = 1, message = "상행 종점역 아이디는" + ID_MIN_RANGE_ERROR)
    private Long upStationId;

    @Min(value = 1, message = "하행 종점역 아이디는" + ID_MIN_RANGE_ERROR)
    private Long downStationId;

    @Min(value = 1, message = "라인 구간 거리는" + ID_MIN_RANGE_ERROR)
    private int distance;

    @Value("0")
    private int extraFare;

    private LineRequest() {
    }

    public LineRequest(final String name, final String color, final Long upStationId, final Long downStationId,
                       final int distance, final int extraFare) {
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

    @Override
    public String toString() {
        return "LineRequest{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", upStationId=" + upStationId +
                ", downStationId=" + downStationId +
                ", distance=" + distance +
                ", extraFare=" + extraFare +
                '}';
    }
}
