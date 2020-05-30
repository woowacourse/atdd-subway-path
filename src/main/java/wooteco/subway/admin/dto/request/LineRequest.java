package wooteco.subway.admin.dto.request;

import java.time.LocalTime;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import wooteco.subway.admin.domain.Line;

public class LineRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String color;
    @NotNull
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @NotNull
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;
    @Min(0)
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
        return Line.withoutId(name, color, startTime, endTime, intervalTime);
    }
}
