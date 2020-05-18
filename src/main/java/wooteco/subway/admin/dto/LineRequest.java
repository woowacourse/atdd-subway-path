package wooteco.subway.admin.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

public class LineRequest {
    @NotBlank
    private String name;
    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalTime endTime;

    private int intervalTime;

    private LineRequest() {
    }

    public LineRequest(final String name, final LocalTime startTime, final LocalTime endTime, final int intervalTime) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
    }

    public String getName() {
        return name;
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
}
