package wooteco.subway.admin.dto.line;

import wooteco.subway.admin.domain.Line;

import java.time.LocalTime;

public class LineRequest {
    private String name;
    private String backgroundColor;
    private LocalTime startTime;
    private LocalTime endTime;
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
