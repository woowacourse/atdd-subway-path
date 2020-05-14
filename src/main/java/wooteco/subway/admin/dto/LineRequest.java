package wooteco.subway.admin.dto;

import wooteco.subway.admin.domain.Line;

import java.time.LocalTime;
import java.util.ArrayList;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class LineRequest {

    @NotBlank(message = "이름은 필수 입력 요소입니다.")
    private String name;

    @NotBlank(message = "이름은 필수 입력 요소입니다.")
    private String backgroundColor;

    @NotNull(message = "이름은 필수 입력 요소입니다.")
    private LocalTime startTime;

    @NotNull(message = "이름은 필수 입력 요소입니다.")
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
        return new Line(name, backgroundColor, startTime, endTime, intervalTime);
    }
}
