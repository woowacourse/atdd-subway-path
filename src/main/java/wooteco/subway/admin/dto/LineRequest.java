package wooteco.subway.admin.dto;

import java.time.LocalTime;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import wooteco.subway.admin.domain.Line;

public class LineRequest {

    @NotBlank(message = "노선 이름은 필수 입력 요소입니다.")
    private String name;

    @NotBlank(message = "노선 색상은 필수 입력 요소입니다.")
    private String backgroundColor;

    @NotNull(message = "첫차 시간은 필수 입력 요소입니다.")
    private LocalTime startTime;

    @NotNull(message = "막차 시간은 필수 입력 요소입니다.")
    private LocalTime endTime;

    @Min(value = 1, message = "간격은 1 이상만 입력 가능합니다.")
    private int intervalTime;

    public LineRequest() {
    }

    public String getName() {
        return name;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getIntervalTime() {
        return intervalTime;
    }

    public Line toLine() {
        return Line.of(name, backgroundColor, startTime, endTime, intervalTime);
    }
}
