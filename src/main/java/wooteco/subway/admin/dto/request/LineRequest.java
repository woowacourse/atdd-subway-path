package wooteco.subway.admin.dto.request;

import java.time.LocalTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import wooteco.subway.admin.domain.Line;

public class LineRequest {
    @NotEmpty(message = "노선의 이름을 입력해주세요.")
    private String name;
    @NotNull(message = "노선의 색상을 입력해주세요.")
    private String color;
    @NotNull(message = "노선의 출발 시간을 입력해주세요.")
    private LocalTime startTime;
    @NotNull(message = "노선의 첫차 시간을 입력해주세요.")
    private LocalTime endTime;
    @NotNull(message = "노선의 막차 시간을 입력해주세요.")
    private int intervalTime;

    public LineRequest() {
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
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
        return Line.of(name, color, startTime, endTime, intervalTime);
    }
}
