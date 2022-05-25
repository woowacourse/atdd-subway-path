package wooteco.subway.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

public class LineRequest {

    @NotBlank(message = "노선 이름은 공백일 수 없습니다.")
    private String name;

    @NotNull(message = "노선 색상은 공백일 수 없습니다.")
    private String color;

    private int extraFare;

    @NotNull(message = "노선 구간의 상행 아이디는 공백일 수 없습니다.")
    private Long upStationId;

    @NotNull(message = "노선 구간의 하행 아이디는 공백일 수 없습니다.")
    private Long downStationId;

    @Positive
    @NotNull(message = "구간의 거리는 공백이거나 음수일 수 없습니다.")
    private int distance;


    private LineRequest() {
    }

    public LineRequest(final String name,
                       final String color,
                       final int extraFare,
                       final Long upStationId,
                       final Long downStationId,
                       final int distance) {
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Line toEntity() {
        return new Line(name, color, extraFare);
    }

    public Section toSectionEntity() {
        return new Section(new Station(upStationId, ""), new Station(downStationId, ""), distance);
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

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }
}
