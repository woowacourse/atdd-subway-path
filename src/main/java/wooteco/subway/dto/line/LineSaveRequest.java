package wooteco.subway.dto.line;

import wooteco.subway.domain.Line;

public class LineSaveRequest {

    private final String name;
    private final String color;
    private final Long upStationId;
    private final Long downStationId;
    private final int distance;
    private final int extraFare;

    public LineSaveRequest(final String name, final String color, final Long upStationId, final Long downStationId,
                           final int distance, final int extraFare) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.extraFare = extraFare;
    }

    public LineSaveRequest(final String name, final String color, final Long upStationId, final Long downStationId,
                           final int distance) {
        this(name, color, upStationId, downStationId, distance, 0);
    }

    public Line toLine() {
        return new Line(name, color);
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
}
